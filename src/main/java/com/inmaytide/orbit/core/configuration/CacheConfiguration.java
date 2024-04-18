package com.inmaytide.orbit.core.configuration;

import com.inmaytide.orbit.core.service.dto.CreateMultipartUploadResult;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author inmaytide
 * @since 2024/4/17
 */
@Configuration
public class CacheConfiguration {

    @Bean
    public RedisTemplate<String, CreateMultipartUploadResult> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, CreateMultipartUploadResult> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        return template;
    }

}
