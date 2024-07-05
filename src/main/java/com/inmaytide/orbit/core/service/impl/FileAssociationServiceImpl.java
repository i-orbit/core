package com.inmaytide.orbit.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.inmaytide.orbit.commons.domain.dto.result.AffectedResult;
import com.inmaytide.orbit.core.domain.FileAssociation;
import com.inmaytide.orbit.core.mapper.FileAssociationMapper;
import com.inmaytide.orbit.core.service.FileAssociationService;
import org.springframework.stereotype.Service;

/**
 * @author inmaytide
 * @since 2024/5/15
 */
@Service
public class FileAssociationServiceImpl implements FileAssociationService {

    private final FileAssociationMapper baseMapper;

    public FileAssociationServiceImpl(FileAssociationMapper baseMapper) {
        this.baseMapper = baseMapper;
    }

    @Override
    public AffectedResult delete(FileAssociation entity) {
        LambdaQueryWrapper<FileAssociation> wrapper = new LambdaQueryWrapper<>(entity);
        wrapper.eq(FileAssociation::getFileId, entity.getFileId());
        wrapper.eq(FileAssociation::getBusinessDataId, entity.getBusinessDataId());
        return AffectedResult.withAffected(baseMapper.delete(wrapper));
    }

    @Override
    public FileAssociationMapper getBaseMapper() {
        return baseMapper;
    }
}
