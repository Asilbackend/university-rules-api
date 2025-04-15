package uz.tuit.unirules.config;

import io.swagger.v3.oas.models.*;
import io.swagger.v3.oas.models.info.*;
import io.swagger.v3.oas.models.security.*;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.ExternalDocumentation;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Value("${server.url}")
    private String serverUrl;

    private static final String SCHEME_NAME = "bearerAuth";
    private static final String BEARER_FORMAT = "JWT";
    private static final String SCHEME_TYPE = "bearer";

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(getApiInfo())
                .externalDocs(getExternalDocs())
                .servers(List.of(getServer()))
                .addSecurityItem(new SecurityRequirement().addList(SCHEME_NAME))
                .components(new Components().addSecuritySchemes(SCHEME_NAME, createSecurityScheme()));
    }

    private Info getApiInfo() {
        return new Info()
                .title("Spring Boot Swagger API")
                .description("Modern Swagger documentation for Spring Boot 3")
                .version("1.0.0")
                .termsOfService("https://swagger.io/terms/")
                .contact(new Contact()
                        .name("Asilbek O'ktamov")
                        .email("oktamovasilbek12@gmail.com")
                        .url("https://anvarovich.uz"))
                .license(new License()
                        .name("Apache 2.0")
                        .url("https://www.apache.org/licenses/LICENSE-2.0.html"));
    }

    private ExternalDocumentation getExternalDocs() {
        return new ExternalDocumentation()
                .description("Project Wiki")
                .url("https://github.com/your-repo/wiki");
    }

    private Server getServer() {
        return new Server()
                .url(serverUrl)
                .description("Development server");
    }

    private SecurityScheme createSecurityScheme() {
        return new SecurityScheme()
                .name(SCHEME_NAME)
                .type(SecurityScheme.Type.HTTP)
                .scheme(SCHEME_TYPE)
                .bearerFormat(BEARER_FORMAT)
                .in(SecurityScheme.In.HEADER);
    }
}
