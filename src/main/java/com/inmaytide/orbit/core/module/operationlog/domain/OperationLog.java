package com.inmaytide.orbit.core.module.operationlog.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.inmaytide.orbit.framework.core.domain.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.Instant;

/**
 * 操作审计日志
 */
@Data
@TableName("operation_log")
@EqualsAndHashCode(callSuper = true)
public class OperationLog extends Entity {

    /**
     * 所属租户ID
     */
    private String tenantId;

    /**
     * 操作人用户ID
     */
    private String operatedBy;

    /**
     * 操作时间
     */
    private Instant operatedAt;

    /**
     * 操作类型
     */
    private String operationType;

    /**
     * 操作描述
     */
    private String operation;

    /**
     * 微服务模块名称
     */
    private String module;

    /**
     * 操作对象类型
     */
    private String resourceType;

    /**
     * 操作对象ID
     */
    private String resourceId;

    /**
     * 请求来源IP
     */
    private String sourceIp;

    /**
     * 用户客户端信息
     */
    private String userAgent;

    /**
     * 请求唯一标识
     */
    private String requestId;

    /**
     * 请求链路标识
     */
    private String traceId;

    /**
     * HTTP请求Method
     */
    private String httpMethod;

    /**
     * 请求路径
     */
    private String url;

    /**
     * 操作结果
     */
    private Integer operateResult;

    /**
     * 请求耗时(ms)
     */
    private Long duration;

    /**
     * 错误描述
     */
    private String errorDescription;

    /**
     * 数据变更内容
     */
    private String changeData;

    /**
     * 请求内容
     */
    private String requestBody;

    /**
     * 响应内容
     */
    private String responseBody;

    /**
     * 入库时间
     */
    private Instant createdAt;
}
