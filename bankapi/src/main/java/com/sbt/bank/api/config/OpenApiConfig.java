package com.sbt.bank.api.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
        info = @Info(
                title = "Bank Api",
                description = "Bank api", version = "1.0.0",
                contact = @Contact(
                        name = "Ivantsov Dmitry",
                        email = "dim22xxx@gmail.com",
                        url = "https://github.com/DeeMMoon"
                )
        )
)
public class OpenApiConfig {
}
