package at.mikemitterer.catshostel.config

import at.mikemitterer.catshostel.gson.GsonAdapterForCatsHostel
import com.google.gson.Gson
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.json.GsonHttpMessageConverter

// Reminder für Swagger / SpringFox
// import springfox.documentation.spring.web.json.Json

/**
 * Basis-Config für Gson (+ Swagger)
 *
 * @since   07.05.20, 11:37
 */
@Configuration
@ConditionalOnClass(Gson::class)
class GsonConfig {
    @Bean
    fun gson() = GsonAdapterForCatsHostel.gson

    @Bean
    fun gsonHttpMessageConverter(): GsonHttpMessageConverter? {
        val converter = GsonHttpMessageConverter()
        converter.gson = gson()
        return converter
    }
}
