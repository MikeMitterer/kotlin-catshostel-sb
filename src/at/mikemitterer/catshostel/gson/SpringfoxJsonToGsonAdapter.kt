package at.mikemitterer.catshostel.gson


import com.google.gson.JsonElement
import com.google.gson.JsonParser
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import springfox.documentation.spring.web.json.Json
import java.lang.reflect.Type


/**
 * Serialization-Helper for Swagger
 *
 * Mehr:
 *      http://www.programmersought.com/article/7688804471/
 *
 * @since   11.05.20, 18:30
 */
class SpringfoxJsonToGsonAdapter : JsonSerializer<Json> {
    override fun serialize(json: Json, type: Type, context: JsonSerializationContext): JsonElement
            = JsonParser.parseString(json.value())
}