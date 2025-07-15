package com.inmaytide.orbit.core.service;

import com.inmaytide.orbit.core.domain.FileMetadata;
import com.inmaytide.orbit.core.service.dto.Base64File;
import com.inmaytide.orbit.core.service.dto.CompleteMultipartUpload;
import com.inmaytide.orbit.core.service.dto.CreateMultipartUpload;
import com.inmaytide.orbit.core.service.dto.CreateMultipartUploadResult;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author inmaytide
 * @since 2024/4/9
 */
public interface FileExchangeService {

    FileMetadata upload(MultipartFile file);

    FileMetadata upload(Base64File file);

    List<FileMetadata> upload(List<Base64File> files);

    CreateMultipartUploadResult createMultipartUpload(CreateMultipartUpload params);

    FileMetadata completeMultipartUpload(CompleteMultipartUpload params);

    void download(String id, HttpServletResponse response);
}
