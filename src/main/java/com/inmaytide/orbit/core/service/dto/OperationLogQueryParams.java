package com.inmaytide.orbit.core.service.dto;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.inmaytide.orbit.commons.domain.dto.params.Pageable;
import com.inmaytide.orbit.commons.log.domain.OperationLog;
import io.swagger.v3.oas.annotations.media.Schema;
import org.apache.commons.lang3.StringUtils;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;

/**
 * @author inmaytide
 * @since 2022/8/22
 */
@Schema(title = "分页查询用户操作日志过滤条件DTO")
public class OperationLogQueryParams extends Pageable<OperationLog> implements Serializable {

    @Serial
    private static final long serialVersionUID = 2930192670686893025L;

    @Schema(title = "关键字", description = "操作描述/业务描述/操作人姓名")
    private String queryName;

    @Schema(title = "操作结果")
    private String result;

    @Schema(title = "操作时间-查询时间范围-开始时间")
    private Instant start;

    @Schema(title = "操作时间-查询时间范围-结束时间")
    private Instant end;

    @Override
    public LambdaQueryWrapper<OperationLog> toWrapper() {
        LambdaQueryWrapper<OperationLog> wrapper = Wrappers.lambdaQuery(OperationLog.class)
                .orderByDesc(OperationLog::getOperationTime);
        if (StringUtils.isNotBlank(getQueryName())) {
//            List<Long> userIds = ApplicationContextHolder.getInstance().getBean(UserService.class).getUserIdsByName(getQueryName());
//            wrapper.and(w -> {
//                w.like(OperationLog::getBusiness, getQueryName())
//                        .or().like(OperationLog::getDescription, getQueryName())
//                        .or().in(!userIds.isEmpty(), OperationLog::getOperator, userIds);
//            });
        }
        wrapper.eq(StringUtils.isNotBlank(getResult()), OperationLog::getResult, getResult());
        wrapper.ge(getStart() != null, OperationLog::getOperationTime, getStart());
        wrapper.le(getEnd() != null, OperationLog::getOperationTime, getEnd());
        return wrapper;
    }

    public String getQueryName() {
        return queryName;
    }

    public void setQueryName(String queryName) {
        this.queryName = queryName;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Instant getStart() {
        return start;
    }

    public void setStart(Instant start) {
        this.start = start;
    }

    public Instant getEnd() {
        return end;
    }

    public void setEnd(Instant end) {
        this.end = end;
    }

}
