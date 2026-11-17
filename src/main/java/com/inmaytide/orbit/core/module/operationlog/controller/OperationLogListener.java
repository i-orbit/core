package com.inmaytide.orbit.core.module.operationlog.controller;

import com.inmaytide.orbit.core.module.operationlog.domain.OperationLog;
import com.inmaytide.orbit.core.module.operationlog.service.OperationLogService;
import com.inmaytide.orbit.framework.log.Constants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Objects;

/**
 * @author inmaytide
 * @since 2026/11/12
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class OperationLogListener {

    private final OperationLogService operationLogService;

    private final ObjectMapper objectMapper;

    @KafkaListener(topics = Constants.KAFKA_TOPIC_OPERATION_LOG, groupId = "operation-log-consumer", containerFactory = "operationLogKafkaListenerContainerFactory")
    public void consume(List<String> messages, Acknowledgment acknowledgment) {
        if (messages.isEmpty()) {
            acknowledgment.acknowledge();
            return;
        }
        try {
            List<OperationLog> instances = messages.stream()
                    .filter(Objects::nonNull)
                    .map(node -> objectMapper.readValue(node, OperationLog.class))
                    .toList();
            operationLogService.batchCreate(instances);
            // 数据库写入成功后提交 Offset
            acknowledgment.acknowledge();
            log.debug("操作日志批量入库成功，数量：{}", messages.size());
        } catch (Exception e) {
            log.error("操作日志批量入库失败，数量：{}", messages.size(), e);
            // 不提交 Offset，等待重新消费
            throw e;
        }
    }

}
