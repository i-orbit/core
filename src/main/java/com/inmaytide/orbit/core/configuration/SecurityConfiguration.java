package com.inmaytide.orbit.core.configuration;

import com.inmaytide.exception.web.reactive.DefaultExceptionHandler;
import com.inmaytide.exception.web.servlet.DefaultHandlerExceptionResolver;
import com.inmaytide.orbit.commons.security.CustomizedOpaqueTokenIntrospector;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.client.RestTemplate;

/**
 * @author inmaytide
 * @since 2024/4/18
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@DependsOn("exceptionResolver")
public class SecurityConfiguration {

    private final DefaultHandlerExceptionResolver exceptionResolver;

    private final RestTemplate restTemplate;

    public SecurityConfiguration(DefaultHandlerExceptionResolver exceptionResolver, RestTemplate restTemplate) {
        this.exceptionResolver = exceptionResolver;
        this.restTemplate = restTemplate;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .sessionManagement(c -> c.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .headers(c -> c.httpStrictTransportSecurity(HeadersConfigurer.HstsConfig::disable))
                .oauth2ResourceServer(c -> {
                    c.accessDeniedHandler((req, res, e) -> exceptionResolver.resolveException(req, res, null, e));
                    c.authenticationEntryPoint((req, res, e) -> exceptionResolver.resolveException(req, res, null, e));
                    c.opaqueToken(ot -> ot.introspector(new CustomizedOpaqueTokenIntrospector(restTemplate)));
                })
                .exceptionHandling(c -> {
                    c.accessDeniedHandler((req, res, e) -> exceptionResolver.resolveException(req, res, null, e));
                    c.authenticationEntryPoint((req, res, e) -> exceptionResolver.resolveException(req, res, null, e));
                })
                .authorizeHttpRequests(c -> {
                    c.anyRequest().authenticated();
                }).build();
    }

}
