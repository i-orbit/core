package com.inmaytide.orbit.core.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfiguration {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Orbit Core API")
                        .description("核心业务模块接口文档 - 数据字典、通用功能")
                        .version("1.0.0"));
    }
}
