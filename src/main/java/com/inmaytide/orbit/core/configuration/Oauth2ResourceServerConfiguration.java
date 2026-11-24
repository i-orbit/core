package com.inmaytide.orbit.core.configuration;

import com.inmaytide.orbit.framework.security.OAuth2ResourceServerConfigurationAdapter;
import com.inmaytide.orbit.framework.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class Oauth2ResourceServerConfiguration extends OAuth2ResourceServerConfigurationAdapter {

    private static final String[] OPENAPI_WHITELIST = {
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/actuator/health",
            "/actuator/health/**"
    };

    public Oauth2ResourceServerConfiguration(SecurityProperties properties) {
        super(properties);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {
        Customizer<AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry> authorizeHttpRequestsCustomizer = authorize -> {
            authorize.requestMatchers(OPENAPI_WHITELIST).permitAll()
                    .anyRequest().authenticated();
        };
        return super.configure(http).authorizeHttpRequests(authorizeHttpRequestsCustomizer).build();
    }

}
