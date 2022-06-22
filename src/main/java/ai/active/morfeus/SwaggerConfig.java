package ai.active.morfeus;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@OpenAPIDefinition(info = @io.swagger.v3.oas.annotations.info.Info(title = "Student Information Integration API's", version = "2.0", description = "Api's to help on integrating to the Student Information API's"))
public class SwaggerConfig {
  @Bean public OpenAPI springShopOpenAPI() {
    return new OpenAPI().info(
            new Info().title("Student Information").description("Starter code to integrate API's of Student Information").version("v1.1.0")
                .license(new License().name("Apache 2.0").url("https://active.ai/")))
        .externalDocs(new ExternalDocumentation().description("Webhook Documentation").url("https://docs.active.ai"));
  }
}
