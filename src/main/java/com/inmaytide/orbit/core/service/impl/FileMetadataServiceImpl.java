package com.inmaytide.orbit.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.inmaytide.orbit.core.domain.FileMetadata;
import com.inmaytide.orbit.core.mapper.FileMetadataMapper;
import com.inmaytide.orbit.core.service.FileMetadataService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * @author inmaytide
 * @since 2024/4/8
 */
@Service
public class FileMetadataServiceImpl implements FileMetadataService {

    private final FileMetadataMapper baseMapper;

    public FileMetadataServiceImpl(FileMetadataMapper baseMapper) {
        this.baseMapper = baseMapper;
    }

    @Override
    public FileMetadata persist(Future<FileMetadata> future) {
        try {
            FileMetadata entity = future.get();
            if (entity.getId() != null) {
                return entity;
            }
            return create(entity);
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<FileMetadata> findBySHA256(String sha256) {
        LambdaQueryWrapper<FileMetadata> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(FileMetadata::getSha256, sha256);
        List<FileMetadata> entities = baseMapper.selectList(wrapper);
        return entities.isEmpty() ? Optional.empty() : Optional.of(entities.getFirst());
    }

    @Override
    public FileMetadataMapper getBaseMapper() {
        return baseMapper;
    }
}
