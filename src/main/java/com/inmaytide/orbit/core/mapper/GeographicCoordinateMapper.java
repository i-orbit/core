package com.inmaytide.orbit.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.inmaytide.orbit.commons.domain.GeographicCoordinate;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author inmaytide
 * @since 2024/5/16
 */
@Mapper
public interface GeographicCoordinateMapper extends BaseMapper<GeographicCoordinate> {
}
