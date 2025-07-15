package com.inmaytide.orbit.core.utils;

import com.inmaytide.orbit.core.configuration.FileUploaderProperties;
import io.minio.*;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.XmlParserException;
import io.minio.http.Method;
import io.minio.messages.Part;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;

/**
 * @author inmaytide
 * @since 2024/4/9
 */
public class CustomizedMinioClient {

    private final MinioAsyncClient client;

    public CustomizedMinioClient(MinioAsyncClient client) {
        this.client = client;
    }

    public CreateMultipartUploadResponse createMultipartUpload(String bucketName, String objectName) {
        try {
            return client.createMultipartUploadAsync(bucketName, null, objectName, null, null).get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void completeMultipartUpload(String bucketName, String objectName, String uploadId, Part[] parts) {
        try {
            client.completeMultipartUploadAsync(bucketName, null, objectName, uploadId, parts, null, null).get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public ListPartsResponse listParts(String bucketName, String objectName, Integer partNumberMarker, String uploadId) {
        try {
            return client.listPartsAsync(bucketName, null, objectName, null, partNumberMarker, uploadId, null, null).get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String getPresignedObjectUrl(Method method, String bucket, String objectName, Map<String, String> queryParams) {
        try {
            GetPresignedObjectUrlArgs args = GetPresignedObjectUrlArgs.builder()
                    .method(method)
                    .bucket(bucket)
                    .object(objectName)
                    .expiry(12, TimeUnit.HOURS)
                    .extraQueryParams(queryParams)
                    .build();
            return client.getPresignedObjectUrl(args);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public CompletableFuture<GetObjectResponse> getObject(GetObjectArgs args) throws Exception {
        return client.getObject(args);
    }

    public StatObjectResponse statObject(StatObjectArgs args) {
        try {
            return client.statObject(args).get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public CompletableFuture<Void> downloadObject(DownloadObjectArgs args) {
        try {
            return client.downloadObject(args);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void removeObject(RemoveObjectArgs args) {
        try {
            client.removeObject(args).get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void putObject(PutObjectArgs args) {
        try {
            client.putObject(args);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public CompletableFuture<Boolean> bucketExists(BucketExistsArgs args) {
        try {
            return client.bucketExists(args);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void makeBucket(MakeBucketArgs args) {
        try {
            client.makeBucket(args).get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
