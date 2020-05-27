package at.mikemitterer.catshostel.jwt

import at.mikemitterer.tdd.KeyCloak
import at.mikemitterer.tdd.getForAuthorizedEntity
import com.google.gson.JsonObject
import kotlinx.coroutines.test.runBlockingTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.HttpStatus


/**
 *
 *
 * @since   07.05.20, 09:54
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class KeyCloakTest {
    private val logger = LoggerFactory.getLogger(KeyCloakTest::class.java.simpleName)

    @LocalServerPort
    private val port = 0

    @Autowired
    private lateinit var restTemplate: TestRestTemplate

    /**
     * To succeed run KeyCloak (may be in Docker-Container)
     */
    @Test
    // @Disabled("KeyCloak must be running")
    fun testKeyCloak() = runBlockingTest {
        val keyCloak = KeyCloak(restTemplate)

        val tokens = keyCloak.requestToken("cat1", "12345678")
        val jwt = assertDoesNotThrow {
            keyCloak.verifyWithAuth0(tokens)
        }
        logger.info("OK - Tokens are verified with Auth0!")
        assertThat(jwt.claims["email"]?.asString()).isEqualTo("cat1@miau.at")

        assertDoesNotThrow {
            keyCloak.verifyWithJWTS(tokens)
            logger.info("OK - Tokens are verified with JWTs!")
        }

        val tokensRefreshed = keyCloak.refreshToken(tokens.refreshToken)
        assertDoesNotThrow {
            keyCloak.verifyWithAuth0(tokensRefreshed)
            logger.info("OK - Refreshed tokes are verified!")
        }

        val claims = assertDoesNotThrow {
             keyCloak.verifyWithJWTS(tokensRefreshed)
        }
        assertThat(claims.body["email"]).isEqualTo("cat1@miau.at")
    }

    @Test
    fun testUserMessage() {
        val username = "cat1"

        val keyCloak = KeyCloak(restTemplate)
        val token = keyCloak.requestToken(username, "12345678")

        val message = "Spring Boot beats Ktor!"
        val response = restTemplate.getForAuthorizedEntity(
                "http://localhost:${port}/protected/message?message=${message}",
                token.accessToken,
                String::class.java
        )

        assertThat(response.body).isEqualTo("Psst: (whisper) Spring Boot beats Ktor! -> $username")
    }

    @Test
    fun testAdminMessage() {
        val username = "cat1"

        val keyCloak = KeyCloak(restTemplate)
        val token = keyCloak.requestToken(username, "12345678")

        val message = "Spring Boot beats Ktor!"
        val response = restTemplate.getForAuthorizedEntity(
                "http://localhost:${port}/protected/message4admin?message=${message}",
                token.accessToken,
                String::class.java
        )

        assertThat(response.body).isEqualTo("Psst: (whisper to admin) Spring Boot beats Ktor! -> $username")
    }

    @Test
    fun testAdminMessageForUser() {
        val username = "nicki"

        val keyCloak = KeyCloak(restTemplate)

        // Nicki has only user-status
        val token = keyCloak.requestToken(username, "12345678")

        val message = "Spring Boot beats Ktor!"
        val response = restTemplate.getForAuthorizedEntity(
                "http://localhost:${port}/protected/message4admin?message=${message}",
                token.accessToken,
                JsonObject::class.java
        )

        assertThat(response.statusCode.value()).isEqualTo(HttpStatus.FORBIDDEN.value())
    }

}

