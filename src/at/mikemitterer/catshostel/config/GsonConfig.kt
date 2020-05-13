package at.mikemitterer.catshostel.config

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.json.GsonHttpMessageConverter

/**
 *
 *
 * @since   07.05.20, 11:37
 */
@Configuration
@ConditionalOnClass(Gson::class)
class GsonConfig {
    @Bean
    fun gson(): Gson {
        return GsonBuilder()
                .enableComplexMapKeySerialization()
                .setPrettyPrinting()
                // .registerTypeAdapter(Json::class.java, SpringfoxJsonToGsonAdapter())
                .create()
    }

    @Bean
    fun gsonHttpMessageConverter(): GsonHttpMessageConverter? {
        val converter = GsonHttpMessageConverter()
        converter.gson = gson()
        return converter
    }
}