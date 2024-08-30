package com.inmaytide.orbit.core.utils;

import io.minio.*;
import io.minio.http.Method;
import io.minio.messages.Part;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author inmaytide
 * @since 2024/4/9
 */
public class CustomizedMinioClient extends MinioAsyncClient {

    public CustomizedMinioClient(MinioAsyncClient client) {
        super(client);
    }

    public CreateMultipartUploadResponse createMultipartUpload(String bucketName, String objectName) {
        try {
            CompletableFuture<CreateMultipartUploadResponse> future = super.createMultipartUploadAsync(bucketName, null, objectName, null, null);
            return future.get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void completeMultipartUpload(String bucketName, String objectName, String uploadId, Part[] parts) {
        try {
            super.completeMultipartUploadAsync(bucketName, null, objectName, uploadId, parts, null, null).get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public ListPartsResponse listParts(String bucketName, String objectName, Integer partNumberMarker, String uploadId) {
        try {
            return super.listPartsAsync(bucketName, null, objectName, null, partNumberMarker, uploadId, null, null).get();
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
            return super.getPresignedObjectUrl(args);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
