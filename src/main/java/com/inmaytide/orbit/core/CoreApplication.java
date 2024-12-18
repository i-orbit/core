package com.inmaytide.orbit.core;

import com.inmaytide.orbit.commons.business.SystemUserService;
import com.inmaytide.orbit.commons.domain.SystemUser;
import com.inmaytide.orbit.core.configuration.ApplicationProperties;
import com.inmaytide.orbit.core.utils.CustomizedMinioClient;
import io.minio.MinioAsyncClient;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.SpecVersion;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.lionsoul.ip2region.xdb.Searcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.Serializable;

@SpringBootApplication(scanBasePackages = {"com.inmaytide.orbit.commons", "com.inmaytide.orbit.core"})
public class CoreApplication {

    private static final Logger LOG = LoggerFactory.getLogger(CoreApplication.class);

    private final ApplicationProperties props;

    public CoreApplication(ApplicationProperties props) {
        this.props = props;
    }

    public static void main(String[] args) {
        SpringApplication.run(CoreApplication.class, args);
    }

    @Bean("ipRegionSearcher")
    public Searcher searcher() throws IOException {
        try {
            return Searcher.newWithBuffer(new ClassPathResource("ip2region.xdb").getContentAsByteArray());
        } catch (Exception e) {
            LOG.error("Failed to create content cached searcher, Cause by: \n", e);
            throw e;
        }
    }

    @Bean
    public CustomizedMinioClient minioClient() {
        MinioAsyncClient client = MinioAsyncClient.builder()
                .endpoint(props.getMinIO().getEndpoint())
                .credentials(props.getMinIO().getAccessKey(), props.getMinIO().getSecretKey())
                .build();
        return new CustomizedMinioClient(client);
    }

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .specVersion(SpecVersion.V30)
                .info(
                        new Info().title("Orbit Core API")
                                .version("1.0.0")
                                .license(new License().name("MIT").url("https://opensource.org/licenses/MIT"))
                )
                .externalDocs(
                        new ExternalDocumentation()
                                .description("Library Wiki Documentation")
                                .url("https://github.com/i-orbit/library")
                );
    }


}
