package com.inmaytide.orbit.core.api;

import com.inmaytide.orbit.commons.log.annotation.OperationLogging;
import com.inmaytide.orbit.core.domain.FileMetadata;
import com.inmaytide.orbit.core.service.FileExchangeService;
import com.inmaytide.orbit.core.service.dto.Base64File;
import com.inmaytide.orbit.core.service.dto.CompleteMultipartUpload;
import com.inmaytide.orbit.core.service.dto.CreateMultipartUpload;
import com.inmaytide.orbit.core.service.dto.CreateMultipartUploadResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author inmaytide
 * @since 2024/4/7
 */
@RestController
@RequestMapping("/api/files")
@Tag(name = "文件库文件交换接口")
public class FileExchangeResource {

    private final FileExchangeService fileExchangeService;

    public FileExchangeResource(FileExchangeService fileExchangeService) {
        this.fileExchangeService = fileExchangeService;
    }

    @OperationLogging
    @PostMapping("/upload/multipart/create")
    @Operation(summary = "请求创建分片上传任务")
    public CreateMultipartUploadResult createMultipartUpload(@RequestBody @Validated CreateMultipartUpload params) throws Exception {
        return fileExchangeService.createMultipartUpload(params);
    }

    @OperationLogging
    @PostMapping("/upload/multipart/complete")
    @Operation(summary = "完成创建分片上传任务")
    public FileMetadata completeMultipartUpload(@RequestBody @Validated CompleteMultipartUpload params) throws Exception {
        return fileExchangeService.completeMultipartUpload(params);
    }

    @OperationLogging
    @PostMapping("upload")
    @Operation(summary = "上传文件")
    public FileMetadata upload(@RequestPart MultipartFile file) {
        return fileExchangeService.upload(file);
    }

    @OperationLogging
    @PostMapping("/upload/base64")
    @Operation(summary = "上传Base64编码的文件")
    public List<FileMetadata> uploadBase64File(@RequestBody @Validated List<Base64File> files) {
        return fileExchangeService.upload(files);
    }

    @GetMapping("download")
    @Operation(summary = "下载文件")
    public void download(@RequestParam String id, HttpServletResponse response) {
        fileExchangeService.download(id, response);
    }

}
