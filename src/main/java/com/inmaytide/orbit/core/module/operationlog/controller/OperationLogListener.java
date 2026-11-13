package com.inmaytide.orbit.core.module.operationlog.controller;

import com.inmaytide.orbit.core.module.operationlog.domain.OperationLog;
import com.inmaytide.orbit.core.module.operationlog.service.OperationLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author inmaytide
 * @since 2026/11/12
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class OperationLogListener {

    private final OperationLogService operationLogService;

    @KafkaListener(topics = "operation-log", groupId = "operation-log-consumer", containerFactory = "operationLogKafkaListenerContainerFactory")
    public void consume(List<OperationLog> instances, Acknowledgment acknowledgment) {
        if (instances.isEmpty()) {
            acknowledgment.acknowledge();
            return;
        }
        try {
            operationLogService.batchCreate(instances);
            // 数据库写入成功后提交 Offset
            acknowledgment.acknowledge();
            log.debug("操作日志批量入库成功，数量：{}", instances.size());
        } catch (Exception e) {
            log.error("操作日志批量入库失败，数量：{}", instances.size(), e);
            // 不提交 Offset，等待重新消费
            throw e;
        }
    }

}
