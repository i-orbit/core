package com.inmaytide.orbit.core.service;

import com.inmaytide.orbit.commons.domain.GeographicCoordinate;
import com.inmaytide.orbit.commons.domain.dto.params.BatchUpdate;
import com.inmaytide.orbit.commons.domain.dto.result.AffectedResult;

import java.util.List;

/**
 * @author inmaytide
 * @since 2024/5/16
 */
public interface GeographicCoordinateService {

    List<GeographicCoordinate> persist(BatchUpdate<GeographicCoordinate> body);

    AffectedResult deleteByBusinessDataId(Long businessDataId);

    List<GeographicCoordinate> findByBusinessDataId(Long businessDataId);

}
