package com.sistema.clinica.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;

public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API Clínica Médica")
                        .version("1.0.0")
                        .description("Documentação da API da Clínica Médica")
                        .contact(new Contact()
                                .name("Equipe da Clínica")
                                .email("contato@clinica.com")
                        )
                );
    }
}

