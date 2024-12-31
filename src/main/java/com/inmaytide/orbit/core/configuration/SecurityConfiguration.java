package com.inmaytide.orbit.core.configuration;

import com.inmaytide.exception.web.servlet.DefaultHandlerExceptionResolver;
import com.inmaytide.orbit.commons.security.CustomizedBearerTokenResolver;
import com.inmaytide.orbit.commons.security.CustomizedOpaqueTokenIntrospector;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

/**
 * @author inmaytide
 * @since 2024/4/18
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@DependsOn("exceptionResolver")
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, DefaultHandlerExceptionResolver exceptionResolver, ApplicationProperties properties) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .anonymous(AbstractHttpConfigurer::disable)
                .sessionManagement(c -> c.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .headers(c -> c.httpStrictTransportSecurity(HeadersConfigurer.HstsConfig::disable))
                .oauth2ResourceServer(c -> {
                    c.accessDeniedHandler((req, res, e) -> exceptionResolver.resolveException(req, res, null, e));
                    c.authenticationEntryPoint((req, res, e) -> exceptionResolver.resolveException(req, res, null, e));
                    c.bearerTokenResolver(new CustomizedBearerTokenResolver());
                    c.opaqueToken(ot -> ot.introspector(new CustomizedOpaqueTokenIntrospector(properties)));
                })
                .exceptionHandling(c -> {
                    c.accessDeniedHandler((req, res, e) -> exceptionResolver.resolveException(req, res, null, e));
                    c.authenticationEntryPoint((req, res, e) -> exceptionResolver.resolveException(req, res, null, e));
                })
                .authorizeHttpRequests(c -> {
                    c.requestMatchers("/v3/api-docs").permitAll();
                    c.requestMatchers(HttpMethod.GET, "/api/system/properties").permitAll();
                    c.anyRequest().authenticated();
                }).build();
    }

}
