package at.mikemitterer.catshostel.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2

/**
 * Swagger wird nur ausgeführt wenn im "application.properties"
 * das Setting NICHT "spring.profiles.active=production" ist
 *
 * Weitere Infos:
 *      https://www.baeldung.com/spring-profiles
 *      https://dzone.com/articles/spring-boot-profiles-1
 *
 * @since   06.05.20, 14:17
 */
@EnableSwagger2
@Configuration
@Profile("!production") 
class SwaggerConfig {
    @Bean
    fun productApi(): Docket? {
        return Docket(DocumentationType.SWAGGER_2).select()
                .apis(RequestHandlerSelectors
                        .basePackage("at.mikemitterer.catshostel")).build()
    }

}