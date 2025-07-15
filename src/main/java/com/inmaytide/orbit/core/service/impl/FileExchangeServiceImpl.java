package com.inmaytide.orbit.core.service.impl;

import com.inmaytide.exception.web.BadRequestException;
import com.inmaytide.exception.web.HttpResponseException;
import com.inmaytide.exception.web.ObjectNotFoundException;
import com.inmaytide.orbit.commons.utils.CodecUtils;
import com.inmaytide.orbit.core.configuration.ErrorCode;
import com.inmaytide.orbit.core.domain.FileMetadata;
import com.inmaytide.orbit.core.executor.Base64FileUploader;
import com.inmaytide.orbit.core.executor.MultipartFileUploader;
import com.inmaytide.orbit.core.service.FileExchangeService;
import com.inmaytide.orbit.core.service.FileMetadataService;
import com.inmaytide.orbit.core.service.dto.Base64File;
import com.inmaytide.orbit.core.service.dto.CompleteMultipartUpload;
import com.inmaytide.orbit.core.service.dto.CreateMultipartUpload;
import com.inmaytide.orbit.core.service.dto.CreateMultipartUploadResult;
import com.inmaytide.orbit.core.utils.CustomizedMinioClient;
import com.inmaytide.orbit.core.utils.MinioUtils;
import io.minio.*;
import io.minio.http.Method;
import io.minio.messages.Part;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * @author inmaytide
 * @since 2024/4/9
 */
@Service
public class FileExchangeServiceImpl implements FileExchangeService {

    private static final Logger log = LoggerFactory.getLogger(FileExchangeServiceImpl.class);

    private final ThreadPoolTaskExecutor executor;

    private final FileMetadataService fileMetadataService;

    private final CustomizedMinioClient minioClient;

    private final RedisTemplate<String, CreateMultipartUploadResult> cache;

    public FileExchangeServiceImpl(ThreadPoolTaskExecutor executor, FileMetadataService fileMetadataService, CustomizedMinioClient minioClient, RedisTemplate<String, CreateMultipartUploadResult> cache) {
        this.executor = executor;
        this.fileMetadataService = fileMetadataService;
        this.minioClient = minioClient;
        this.cache = cache;
    }

    @Override
    public FileMetadata upload(MultipartFile file) {
        Future<FileMetadata> future = executor.submit(new MultipartFileUploader(file));
        return fileMetadataService.persist(future);
    }

    @Override
    public List<FileMetadata> upload(List<Base64File> files) {
        if (files == null || files.isEmpty()) {
            return Collections.emptyList();
        }
        return files.stream().map(this::upload).toList();
    }

    @Override
    public FileMetadata upload(Base64File file) {
        Future<FileMetadata> future = executor.submit(new Base64FileUploader(file));
        return fileMetadataService.persist(future);
    }

    @Override
    public CreateMultipartUploadResult createMultipartUpload(CreateMultipartUpload params) {
        CreateMultipartUploadResult res = new CreateMultipartUploadResult(params.getPartCount());
        Optional<FileMetadata> exist = fileMetadataService.findBySHA256(params.getSha256());
        if (exist.isPresent()) {
            res.setFileMetadata(exist.get());
            return res;
        }
        String filename = params.getPath() + "/" + CodecUtils.generateRandomString(32) + "." + FilenameUtils.getExtension(params.getFilename());
        CreateMultipartUploadResponse response = minioClient.createMultipartUpload(params.getBucket(), filename);
        res.setOriginalFilename(params.getFilename());
        res.setUploadId(response.result().uploadId());
        res.setObjectName(filename);
        res.setBucket(params.getBucket());
        // 根据前端的分片数量生成分片的上传地址
        Map<String, String> queryParams = new HashMap<>(4);
        queryParams.put("uploadId", res.getUploadId());
        queryParams.put("Content-Type", MediaType.APPLICATION_OCTET_STREAM_VALUE);
        // 一定要注意partNumber从1开始, 从0开始会丢失0这个分片
        IntStream.rangeClosed(1, params.getPartCount()).parallel().forEach(i -> {
            queryParams.put("partNumber", String.valueOf(i));
            res.addPart(i, minioClient.getPresignedObjectUrl(Method.PUT, params.getBucket(), filename, queryParams));
        });
        cache.opsForValue().set(res.getUploadId(), res, 12, TimeUnit.HOURS);
        return res;
    }

    @Override
    public FileMetadata completeMultipartUpload(CompleteMultipartUpload params) {
        CreateMultipartUploadResult info = cache.opsForValue().get(params.getUploadId());
        if (info == null) {
            throw new BadRequestException(ErrorCode.E_0x00300004);
        }
        try {
            ListPartsResponse listPartsResponse = minioClient.listParts(info.getBucket(), info.getObjectName(), 0, info.getUploadId());
            minioClient.completeMultipartUpload(info.getBucket(), info.getObjectName(), info.getUploadId(), listPartsResponse.result().partList().toArray(new Part[0]));
        } catch (Exception e) {
            throw new HttpResponseException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.E_0x00300005, e);
        }
        try {
            StatObjectArgs args = StatObjectArgs.builder()
                    .bucket(info.getBucket())
                    .object(info.getObjectName())
                    .build();
            StatObjectResponse statObjectResponse = minioClient.statObject(args);
            FileMetadata res = FileMetadata.builder()
                    .filename(info.getOriginalFilename())
                    .size(statObjectResponse.size())
                    .sha256(info.getSha265())
                    .address(info.getBucket() + "/" + info.getObjectName())
                    .build();
            fileMetadataService.create(res);
            return res;
        } catch (Exception e) {
            throw new HttpResponseException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.E_0x00300006, e);
        }
    }

    @Override
    public void download(String id, HttpServletResponse response) {
        FileMetadata metadata = fileMetadataService.get(id).orElseThrow(() -> new ObjectNotFoundException(ErrorCode.E_0x00300007, String.valueOf(id)));
        try {
            GetObjectArgs args = GetObjectArgs.builder()
                    .bucket(MinioUtils.getBucket(metadata.getAddress()))
                    .object(MinioUtils.getObjectName(metadata.getAddress()))
                    .build();
            String displayFileName = new String((metadata.getName() + "." + metadata.getExtension()).getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
            response.reset();
            response.setContentLength(metadata.getSize().intValue());
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            response.addHeader("Content-Disposition", "attachment;filename=" + displayFileName);
            minioClient.getObject(args).thenAccept(res -> {
                try (ReadableByteChannel ic = Channels.newChannel(res); WritableByteChannel oc = Channels.newChannel(response.getOutputStream())) {
                    ByteBuffer buffer = ByteBuffer.allocateDirect(4096);
                    while (ic.read(buffer) != -1) {
                        buffer.flip();
                        while (buffer.hasRemaining()) {
                            oc.write(buffer);
                        }
                        buffer.clear();
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (Exception e) {
            log.error("下载文件{id = {}}失败, Cause by: ", id, e);
            throw new HttpResponseException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.E_0x00300008, e);
        }
    }
}
