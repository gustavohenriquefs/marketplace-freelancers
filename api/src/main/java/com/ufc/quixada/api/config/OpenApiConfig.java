package com.ufc.quixada.api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API Quixadá")
                        .version("1.0.0")
                        .description("Documentação da API para gestão de Freelancers e Contratantes")
                        .termsOfService("https://swagger.io/terms/")
                        .contact(new Contact()
                                .name("Gustavo Henrique")
                                .email("gustavo@email.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0")));
    }
}
