package com.inmaytide.orbit.core.configuration;

import com.inmaytide.exception.web.servlet.DefaultHandlerExceptionResolver;
import com.inmaytide.orbit.commons.security.Oauth2ResourceServerConfigurationAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * @author inmaytide
 * @since 2024/4/18
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@DependsOn("exceptionResolver")
public class Oauth2ResourceServerConfiguration extends Oauth2ResourceServerConfigurationAdapter {

    public Oauth2ResourceServerConfiguration(DefaultHandlerExceptionResolver exceptionResolver, ApplicationProperties properties) {
        super(exceptionResolver, properties);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return super.configure(http).authorizeHttpRequests(c -> {
            c.requestMatchers("/v3/api-docs").permitAll();
            c.requestMatchers(HttpMethod.GET, "/api/system/properties").permitAll();
            c.anyRequest().authenticated();
        }).build();
    }

}
