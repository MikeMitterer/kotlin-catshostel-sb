package at.mikemitterer.catshostel.routes

import com.google.gson.Gson
import com.google.gson.internal.LinkedTreeMap
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.web.server.LocalServerPort





/**
 * @since 06.05.20, 14:52
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class BasicControllerTest {
    @LocalServerPort
    private val port = 0

    @Autowired
    private lateinit var restTemplate: TestRestTemplate

    @Autowired
    private lateinit var gson: Gson

    @Test
    fun testPortIsNot0() {
        assertThat(port).isNotEqualTo(0)
    }

    @Test
    internal fun testRestTemplateNotNull() {
        assertThat(restTemplate).isNotNull
    }

    @Test
    internal fun testGetGreeting() {
        val greeting = restTemplate
                .getForObject("http://localhost:${port}/greeting", Greeting::class.java)

        assertThat(greeting.content).isEqualTo("Hello, World")
    }

    @Test
    internal fun testGetServus() {
        val greeting = restTemplate
                .getForObject("http://localhost:${port}/servus?name=Mike", Greeting::class.java)

        assertThat(greeting.content).isEqualTo("Servus, Mike!")
    }

    @Test
    internal fun testException() {
        val greeting = restTemplate
                .getForEntity("http://localhost:${port}/exception", LinkedTreeMap::class.java)

        assertThat(greeting.statusCode.value()).isEqualTo(500)
        assertThat(greeting.body?.get("message")).isEqualTo("Test-REST Exception")
    }
}