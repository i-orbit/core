package com.inmaytide.orbit.core.configuration;

import com.inmaytide.orbit.core.module.operationlog.domain.OperationLog;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;

@Configuration
public class KafkaConsumerConfiguration {

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, OperationLog> operationLogKafkaListenerContainerFactory(ConsumerFactory<String, OperationLog> consumerFactory) {
        var factory = new ConcurrentKafkaListenerContainerFactory<String, OperationLog>();
        factory.setConsumerFactory(consumerFactory);
        factory.setBatchListener(true);
        factory.setConcurrency(3);
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);
        return factory;
    }

}
