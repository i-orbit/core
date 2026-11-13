package com.inmaytide.orbit.core.module.operationlog.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.Instant;

/**
 * @author inmaytide
 * @since 2026/11/13
 */
@Data
@Schema(name = "操作日志")
public class OperationLogVO {

    @Schema(description = "唯一标识")
    private String id;

    @Schema(description = "所属租户ID")
    private String tenantId;

    @Schema(description = "操作人用户ID")
    private String operatedBy;

    @Schema(description = "操作人姓名")
    private String operatedByName;

    @Schema(description = "操作时间")
    private Instant operatedAt;

    @Schema(description = "操作类型")
    private String operationType;

    @Schema(description = "操作描述")
    private String operation;

    @Schema(description = "微服务模块名称")
    private String module;

    @Schema(description = "操作对象类型")
    private String resourceType;

    @Schema(description = "操作对象ID")
    private String resourceId;

    @Schema(description = "请求来源IP")
    private String sourceIp;

    @Schema(description = "用户客户端信息")
    private String userAgent;

    @Schema(description = "请求唯一标识")
    private String requestId;

    @Schema(description = "请求链路标识")
    private String traceId;

    @Schema(description = "HTTP请求Method")
    private String httpMethod;

    @Schema(description = "请求路径")
    private String url;

    @Schema(description = "操作结果")
    private Integer operateResult;

    @Schema(description = "请求耗时(ms)")
    private Long duration;

    @Schema(description = "错误描述")
    private String errorDescription;

    @Schema(description = "数据变更内容")
    private String changeData;

    @Schema(description = "请求内容")
    private String requestBody;

    @Schema(description = "响应内容")
    private String responseBody;

    @Schema(description = "入库时间")
    private Instant createdAt;

}
