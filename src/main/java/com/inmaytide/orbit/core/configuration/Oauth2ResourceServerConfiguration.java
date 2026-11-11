package com.inmaytide.orbit.core.configuration;

import com.inmaytide.orbit.framework.security.OAuth2ResourceServerConfigurationAdapter;
import com.inmaytide.orbit.framework.security.SecurityProperties;
import com.inmaytide.orbit.framework.security.service.RefreshTokenService;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class Oauth2ResourceServerConfiguration extends OAuth2ResourceServerConfigurationAdapter {

    private static final String[] OPENAPI_WHITELIST = {
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/api/dictionaries/categories"
    };

    public Oauth2ResourceServerConfiguration(SecurityProperties properties, RedisConnectionFactory redisConnectionFactory, RedissonClient redissonClient, @Autowired(required = false) RefreshTokenService refreshTokenService) {
        super(properties, redisConnectionFactory, redissonClient, refreshTokenService);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {
        http.csrf(AbstractHttpConfigurer::disable);
        http.formLogin(AbstractHttpConfigurer::disable);
        http.anonymous(AbstractHttpConfigurer::disable);
        http.headers((c) -> c.httpStrictTransportSecurity(HeadersConfigurer.HstsConfig::disable));
        http.sessionManagement((c) -> c.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return http.authorizeHttpRequests(authorize -> {
                    authorize
                            .requestMatchers(OPENAPI_WHITELIST).permitAll()
                            .anyRequest().permitAll();
                })
                .build();
    }

}
