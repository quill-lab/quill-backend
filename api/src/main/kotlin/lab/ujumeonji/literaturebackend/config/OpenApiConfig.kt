package lab.ujumeonji.literaturebackend.config

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration(proxyBeanMethods = false)
class OpenApiConfig {

    @Bean
    fun openAPI(): OpenAPI {
        val securityScheme = SecurityScheme()
            .type(SecurityScheme.Type.HTTP)
            .scheme("bearer")
            .bearerFormat("JWT")
            .`in`(SecurityScheme.In.HEADER)
            .name("Authorization")

        val securityRequirement = SecurityRequirement()
            .addList("bearerAuth")

        return OpenAPI()
            .components(Components().addSecuritySchemes("bearerAuth", securityScheme))
            .security(listOf(securityRequirement))
            .info(
                Info()
                    .title("작가의 정원 API")
                    .description("작가의 정원 API 명세서입니다.")
                    .version("1.0.0")
            )
    }
}
