package at.mikemitterer.catshostel.routes

import at.mikemitterer.catshostel.model.Credentials
import at.mikemitterer.tdd.TestUtils.regexJWT
import at.mikemitterer.tdd.postFormEntity
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import java.util.*


/**
 * @since 06.05.20, 14:52
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class LoginControllerTest {
    @LocalServerPort
    private val port = 0

    @Autowired
    private lateinit var restTemplate: TestRestTemplate


    /**
     * On the cmdline:
     *       curl -X POST -F 'username=test' -F 'password=password' http://0.0.0.0:8080/login
     */
    @Test
    internal fun testLoginViaForm() {
        val parameters: MultiValueMap<String, String> = LinkedMultiValueMap()
        parameters.add("username", "mike")
        parameters.add("password", "12345678")

        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_FORM_URLENCODED
        headers.accept = Collections.singletonList(MediaType.APPLICATION_JSON)

        val formEntity = HttpEntity(parameters, headers)

        val response = restTemplate
                .postForEntity("http://localhost:${port}/login", formEntity, String::class.java)

        // Returns a JWT
        val jwt = response.body!!
        assertThat(jwt.contains(regexJWT)).isTrue()
    }

    @Test
    internal fun testViaExtensionFunction() {
        val parameters: MultiValueMap<String, String> = LinkedMultiValueMap()
        parameters.add("username", "mike")
        parameters.add("password", "12345678")

        val response = restTemplate
                .postFormEntity("http://localhost:${port}/login", parameters, String::class.java)

        // Returns a JWT
        val jwt = response.body!!
        assertThat(jwt.contains(regexJWT)).isTrue()
    }

    @Test
    internal fun testLoginViaJsonObject() {
        val response = restTemplate
                .postForEntity("http://localhost:${port}/login",
                        Credentials("mike", "12345678"), String::class.java)

        // Returns a JWT
        val jwt = response.body!!
        assertThat(jwt.contains(regexJWT)).isTrue()
    }
}