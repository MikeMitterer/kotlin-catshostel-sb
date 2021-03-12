package at.mikemitterer.catshostel.events

import at.mikemitterer.catshostel.model.Cat
import org.assertj.core.api.Assertions.assertThat
import org.intellij.lang.annotations.Language
import org.junit.jupiter.api.Test

/**
 * Json-Tests
 */
class AddCatResponseTest {

    @Test
    fun testResponseToJson() {
        val cat = Cat("Mike", 99)
        val response = AddCatResponse(cat)
        val json = response.toJson()

        val responseFromJson = AddCatResponse.parse(json)

        assertThat(responseFromJson.event).isEqualTo(AddCatResponse.EVENT_NAME)
        
        assertThat(responseFromJson.data.ID).isEqualTo(cat.ID)
        assertThat(responseFromJson.data.name).isEqualTo(cat.name)
        assertThat(responseFromJson.data.age).isEqualTo(cat.age)
    }

    @Test
    internal fun testFromJson() {
        val  response = AddCatResponse.parse(jsonToTest)

        assertThat(response.event).isEqualTo(AddCatResponse.EVENT_NAME)

        assertThat(response.data.ID).isEqualTo(0)
        assertThat(response.data.name).isEqualTo("Mike")
        assertThat(response.data.age).isEqualTo(99)
    }

    @Language("json")
    private val jsonToTest = """
        {
          "event": "at.mikemitterer.catshostel.events.AddCatResponse",
          "data": {
            "ID": 0,
            "name": "Mike",
            "age": 99
          }
      }
    """.trimIndent()
}
