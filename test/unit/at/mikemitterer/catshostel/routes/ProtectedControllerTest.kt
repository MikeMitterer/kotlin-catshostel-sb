package at.mikemitterer.catshostel.routes

import at.mikemitterer.catshostel.model.Credentials
import at.mikemitterer.tdd.TestUtils.regexJWT
import at.mikemitterer.tdd.getForAuthorizedEntity
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.util.LinkedMultiValueMap
import java.net.URLEncoder
import java.nio.charset.StandardCharsets


/**
 * @since 06.05.20, 14:52
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class ProtectedControllerTest {
    @LocalServerPort
    private val port = 0

    @Autowired
    private lateinit var restTemplate: TestRestTemplate

    @Test
    internal fun testGetProtectedMessageShouldFail() {
        val message = "Should not be shown!"
        val response = restTemplate
                .getForEntity("http://localhost:${port}/protected/message?message=${message}",
                        Object::class.java)

        // Forbidden!
        assertThat(response.statusCodeValue).isEqualTo(403)
    }

    @Test
    internal fun testGetProtectedMessage() {
        val jwt = login()

        val message = "Spring Boot beats Ktor?"
        val response = restTemplate
                .exchange("http://localhost:${port}/protected/message?message=${message}",
                        HttpMethod.GET,
                        HttpEntity<Any>(LinkedMultiValueMap<String, String>().apply {
                            add("Authorization", "Bearer $jwt")
                        }),
                        String::class.java
                )

        assertThat(response.body).isEqualTo("Psst: (wisper) Spring Boot beats Ktor? -> Mike")
    }
    
    @Test
    internal fun testGetProtectedMessageExtFunction() {
        val jwt = login()

        val message = "Spring Boot beats Ktor?"
        val response = restTemplate.getForAuthorizedEntity(
                "http://localhost:${port}/protected/message?message=${message}",
                jwt,
                String::class.java
        )

        assertThat(response.body).isEqualTo("Psst: (wisper) Spring Boot beats Ktor? -> Mike")
    }

    /**
     * Generate JWT
     */
    private fun login(username: String = "mike", password: String = "12345678"): String {
        val response = restTemplate
                .postForEntity("http://localhost:${port}/login",
                        Credentials(username, password), String::class.java)

        // Returns a JWT
        val jwt = response.body!!
        assertThat(jwt.contains(regexJWT)).isTrue()

        return jwt;
    }

}

private fun String.urlEncoded(): String {
    return URLEncoder.encode(this, StandardCharsets.UTF_8.name())
}