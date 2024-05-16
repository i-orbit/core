package com.inmaytide.orbit.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inmaytide.orbit.commons.domain.GeographicCoordinate;
import com.inmaytide.orbit.commons.domain.dto.params.BatchUpdate;
import com.inmaytide.orbit.commons.domain.dto.result.AffectedResult;
import com.inmaytide.orbit.core.mapper.GeographicCoordinateMapper;
import com.inmaytide.orbit.core.service.GeographicCoordinateService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * @author inmaytide
 * @since 2024/5/16
 */
@Service
public class GeographicCoordinateServiceImpl extends ServiceImpl<GeographicCoordinateMapper, GeographicCoordinate> implements GeographicCoordinateService {

    @Override
    @Transactional
    public List<GeographicCoordinate> persist(BatchUpdate<GeographicCoordinate> body) {
        if (body == null || body.getElements() == null || body.getElements().isEmpty()) {
            return List.of();
        }
        Objects.requireNonNull(body.getBusinessDataId());
        deleteByBusinessDataId(body.getBusinessDataId());
        body.getElements().forEach(e -> e.setBusinessDataId(body.getBusinessDataId()));
        saveBatch(body.getElements());
        return findByBusinessDataId(body.getBusinessDataId());
    }

    @Override
    public AffectedResult deleteByBusinessDataId(Long businessDataId) {
        LambdaQueryWrapper<GeographicCoordinate> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(GeographicCoordinate::getBusinessDataId, businessDataId);
        return AffectedResult.withAffected(baseMapper.delete(wrapper));
    }

    @Override
    public List<GeographicCoordinate> findByBusinessDataId(Long businessDataId) {
        LambdaQueryWrapper<GeographicCoordinate> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(GeographicCoordinate::getBusinessDataId, businessDataId);
        return baseMapper.selectList(wrapper);
    }
}
