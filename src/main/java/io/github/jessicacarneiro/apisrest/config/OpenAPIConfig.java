package io.github.jessicacarneiro.apisrest.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI openAPIDocumentation() {
        return new OpenAPI()
                .info(
                        new Info()
                                .title("C.A.R. API")
                                .description("C.A.R.'s system API for easy urban mobility")
                                .version("v1.0")
                                .contact(
                                        new Contact()
                                                .name("Jéssica Carneiro")
                                                .url("https://jessicacarneiro.github.io")
                                )
                );
    }
}
