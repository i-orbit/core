package com.inmaytide.orbit.core.service;

import com.inmaytide.orbit.commons.business.BasicService;
import com.inmaytide.orbit.core.domain.FileMetadata;

import java.util.Optional;
import java.util.concurrent.Future;

/**
 * @author inmaytide
 * @since 2024/4/8
 */
public interface FileMetadataService extends BasicService<FileMetadata> {

    FileMetadata persist(Future<FileMetadata> future);

    Optional<FileMetadata> findBySHA256(String sha256);

}
