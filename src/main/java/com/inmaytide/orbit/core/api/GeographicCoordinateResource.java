package com.inmaytide.orbit.core.api;

import com.inmaytide.orbit.commons.domain.GeographicCoordinate;
import com.inmaytide.orbit.commons.domain.dto.params.BatchUpdate;
import com.inmaytide.orbit.commons.domain.dto.result.AffectedResult;
import com.inmaytide.orbit.core.service.GeographicCoordinateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author inmaytide
 * @since 2024/5/16
 */
@RestController
@RequestMapping("/api/geographic/coordinates")
@Tag(name = "业务数据地理坐标信息")
public class GeographicCoordinateResource {

    private final GeographicCoordinateService service;

    public GeographicCoordinateResource(GeographicCoordinateService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "覆盖保存指定业务数据的关联地理坐标信息")
    public List<GeographicCoordinate> persist(@RequestBody @Validated BatchUpdate<GeographicCoordinate> body) {
        return service.persist(body);
    }

    @DeleteMapping
    @Operation(summary = "清除指定业务数据的关联地理坐标信息")
    public AffectedResult remove(@RequestParam Long businessDataId) {
        return service.deleteByBusinessDataId(businessDataId);
    }

    @GetMapping
    @Operation(summary = "获取指定业务数据的关联地理坐标信息列表")
    public List<GeographicCoordinate> findByBusinessDataId(@RequestParam Long businessDataId) {
        return service.findByBusinessDataId(businessDataId);
    }

}
