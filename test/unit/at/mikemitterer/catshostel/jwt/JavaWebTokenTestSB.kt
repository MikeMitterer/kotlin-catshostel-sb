package at.mikemitterer.catshostel.jwt

import at.mikemitterer.catshostel.auth.*
import at.mikemitterer.tdd.getForAuthorizedEntity
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.google.gson.Gson
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import org.assertj.core.api.Assertions.assertThat
import org.joda.time.DateTime
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.test.context.TestPropertySource
import java.io.File


/**
 * Because we change the setting to use.keycloak we use [at.mikemitterer.catshostel.config.WebSecurityConfig]
 * so we can "fake" the WebToken
 *
 * @since   07.05.20, 09:54
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = ["use.keycloak=false"])
class JavaWebTokenTestSB {
    private val logger = LoggerFactory.getLogger(JavaWebTokenTestSB::class.java.simpleName)

    private val publicKeyFile = File("./resources/rsakeys/jwt.pub.pem")
    private val privateKeyFile = File("./resources/rsakeys/jwt.pkcs8.pem")

    @LocalServerPort
    private val port = 0

    @Autowired
    private lateinit var gson: Gson

    @Autowired
    private lateinit var restTemplate: TestRestTemplate

    /**
     * Key-Generierung und Basis für Source:
     *      https://gist.github.com/destan/b708d11bd4f403506d6d5bb5fe6a82c5
     */
    @Test
    fun testCreateToken() {
        val privKey = getPrivateKey(privateKeyFile)
        val pubKey = getPublicKey(publicKeyFile)
        
        val algorithm = Algorithm.RSA256(pubKey, privKey)
        assertThat(algorithm).isNotNull

        val now = DateTime.now()
        val userID = "9c292b07-0500-432d-b372-bc258c7615a4"

        // Reserve Claims:
        //      https://auth0.com/docs/tokens/concepts/jwt-claims#reserved-claims
        //      https://www.iana.org/assignments/jwt/jwt.xhtml#claims
        val token = JWT.create()

                // exp
                .withExpiresAt(now.plusMinutes(5).toDate()) // 300 secs

                // iat
                .withIssuedAt(now.toDate())

                // iss
                .withIssuer("mmit")

                // sub
                .withSubject(userID)

                .withClaim("typ", "Bearer")

                .sign(algorithm)

        assertThat(token).isNotEmpty()

        logger.info(token)

        val algorithmForVerification: Algorithm = Algorithm.RSA256(pubKey, null)
        val verifier = JWT.require(algorithmForVerification)
                .withIssuer("mmit")
                .build() //Reusable verifier instance

        val jwt = verifier.verify(token)
        assertThat(jwt).isNotNull
    }

    @Test
    fun testCreateMitJJWT() {
        val privateKey = privateKeyFile.readText().stripPEMMarker()
        val now = DateTime.now()
        val userID = "69c1440f-96aa-42e7-8d24-f26498db8ab7"

        val jwt = createJWT(mutableMapOf(
                Claims.EXPIRATION to now.plusMinutes(5).toDate(),
                Claims.ISSUED_AT to now.toDate(),
                Claims.ISSUER to "mmit",
                Claims.AUDIENCE to "account",
                Claims.SUBJECT to userID,
                "typ" to "Bearer",
                "realm_access" to
                        mapOf("roles" to listOf<String>(
                                "offline_access",
                                "uma_authorization",
                                "vip"
                        ))
                ,
                "resource_access" to mapOf(
                        "vue-test-app" to
                                mapOf("roles" to listOf<String>(
                                        "device"
                                ))
                        ,
                        "account" to
                                mapOf("roles" to listOf<String>(
                                        "manage-account",
                                        "manage-account-links",
                                        "view-profile"
                                ))
                ),
                "scope" to "profile email",
                "email_verified" to true,
                "preferred_username" to "mike",
                "given_name" to "Mike",
                "family_name" to "Mitterer",
                "email" to "office@mikemitterer.at"
        ), privateKey)

        assertThat(jwt as String).isNotEmpty()
        logger.info(jwt)

        val publicKey =  getPublicKey(publicKeyFile) {
            // Hier könnte man den public-Key zum testen ändern (verfälschen)
            // it.replace("a","b")
            it
        }

        val jwtParser = Jwts.parserBuilder()
                .setSigningKey(publicKey)
                .build()

        val claims = jwtParser.parseClaimsJws(jwt)
        assertThat(claims).isNotNull

        logger.info("Header: ${claims.header}")
        logger.info("Body: ${claims.body}")
        logger.info("Signature: ${claims.signature}")

        assertThat(claims.body["email"]).isEqualTo("office@mikemitterer.at")

        @Suppress("UNCHECKED_CAST")
        val realmAccess = claims.body["realm_access"] as Map<String, List<String>?>?
        assertThat(realmAccess
                ?.get("roles")
                ?.get(2))
                .isEqualTo("vip")

        @Suppress("UNCHECKED_CAST")
        assertThat ((claims.body["resource_access"] as Map<String, Map<String, List<String>>>)
                ["account"]
                ?.get("roles")
                ?.get(2))
                .isEqualTo("view-profile")
    }


    /**
     * On the cmdline:
     *      TOKEN=$(curl -X POST -F 'username=test' -F 'password=password' http://0.0.0.0:8080/login)
     *      curl -H "Accept: application/json" -H "Authorization: Bearer ${TOKEN}" http://0.0.0.0:8080/protected/route/jwt
     */
    @Test
    fun testServerRequest() {
        val publicKey =  getPublicKey(publicKeyFile)

        val jwt = createJWTFor("mike")
        val jwtParser = Jwts.parserBuilder()
                .setSigningKey(publicKey)
                .build()

        val claims = jwtParser.parseClaimsJws(jwt)
        val body = claims.body

        assertThat(claims).isNotNull
        assertThat(body["preferred_username"]).isNotNull
        assertThat(body["preferred_username"]).isEqualTo("mike")

        // No need to login - we created the JWT with the same logic as /login
        val message = "Spring Boot beats Ktor!"
        val response = restTemplate.getForAuthorizedEntity(
                "http://localhost:${port}/protected/message?message=${message}",
                jwt,
                String::class.java
        )

        assertThat(response.body).isEqualTo("Psst: (whisper) Spring Boot beats Ktor! -> Mike Mitterer")
    }

    private val tokenToTestExpired = """
        eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICI3Z3U4eEVLbFVKeGFDWFRiZnVMaWxwM25MQVpmNURBQWdtTlplM3d0T284In0.
        eyJleHAiOjE1ODgyNTMwMDMsImlhdCI6MTU4ODI1MjcwMywianRpIjoiOWU0ZjUwODgtZDczOC00NGVhLTkyNTktZDc2ZWY2ZDFlOTM3IiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo5MDAwL2F1dGgvcmVhbG1zL2RlbW8iLCJhdWQiOiJhY2NvdW50Iiwic3ViIjoiNWVmMWNmZDgtYTM0ZS00OGU3LWIzNTgtYTQ5ZTg2ZjM4ODNlIiwidHlwIjoiQmVhcmVyIiwiYXpwIjoidnVlLXRlc3QtYXBwIiwic2Vzc2lvbl9zdGF0ZSI6IjMyOTNmYmMwLTYwMmUtNGFhMS05YTAyLTFiZGU5Y2FhMjU1MyIsImFjciI6IjEiLCJhbGxvd2VkLW9yaWdpbnMiOlsiaHR0cDovL2xvY2FsaG9zdDo4MDgwIiwiaHR0cDovL2xvY2FsaG9zdDo5MDkwIl0sInJlYWxtX2FjY2VzcyI6eyJyb2xlcyI6WyJvZmZsaW5lX2FjY2VzcyIsInVtYV9hdXRob3JpemF0aW9uIiwidmlwIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnsidnVlLXRlc3QtYXBwIjp7InJvbGVzIjpbImRldmljZSJdfSwiYWNjb3VudCI6eyJyb2xlcyI6WyJtYW5hZ2UtYWNjb3VudCIsIm1hbmFnZS1hY2NvdW50LWxpbmtzIiwidmlldy1wcm9maWxlIl19fSwic2NvcGUiOiJwcm9maWxlIGVtYWlsIiwiZW1haWxfdmVyaWZpZWQiOnRydWUsIm5hbWUiOiJNaWtlIE1pdHRlcmVyIiwicHJlZmVycmVkX3VzZXJuYW1lIjoibWlrZSIsImdpdmVuX25hbWUiOiJNaWtlIiwiZmFtaWx5X25hbWUiOiJNaXR0ZXJlciIsImVtYWlsIjoib2ZmaWNlQG1pa2VtaXR0ZXJlci5hdCJ9.
        ekB1pt8eWL1Ez3gY_aCUqW56CapZUUxT1qwGKzKBhsDYsEBAruPdjz-fN_AuYWO-a4j7x6hCqsx4QytK6wrCGyS0tTrvkjcUM_m5BmZqAVLmLT7OFAQs0T0_w5scKxX8gEHyJfOAlhAhXRR4oulcEaIKGknqwTZm2FuOL_npmiGpo5Zvc2TFrL8zG_NRf9UnxYTV27Ss-7lJNH-tua34PkoH7zNyDXNeqzW4PKdRmgaLjGe7fED-j5Lm5udF7FLt5CkqCd64aqAHomLzTfcYcpWL7c9Wi_5DUEMjvRbvo8Hu62dUGZkMaLLKDaOmMoEYGZ2pc7LD6dFU1ISBrcJXjw        
    """.trimIndent().replace("\n","")

    private val tokenToTestExpired2 = """
        eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICI3Z3U4eEVLbFVKeGFDWFRiZnVMaWxwM25MQVpmNURBQWdtTlplM3d0T284In0.
        eyJleHAiOjE1ODgyNTQ3NzAsImlhdCI6MTU4ODI1NDQ3MCwianRpIjoiZDc0NWZjMmUtOTAxMy00Nzc4LWIwYzAtZDlmZjA3ZmI3MTIyIiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo5MDAwL2F1dGgvcmVhbG1zL2RlbW8iLCJhdWQiOiJhY2NvdW50Iiwic3ViIjoiNWVmMWNmZDgtYTM0ZS00OGU3LWIzNTgtYTQ5ZTg2ZjM4ODNlIiwidHlwIjoiQmVhcmVyIiwiYXpwIjoidnVlLXRlc3QtYXBwIiwic2Vzc2lvbl9zdGF0ZSI6IjkwNmVmODFiLTMyNTQtNGZjMC05YmI1LTZlZGYyZjFiYzFiYiIsImFjciI6IjEiLCJhbGxvd2VkLW9yaWdpbnMiOlsiaHR0cDovL2xvY2FsaG9zdDo4MDgwIiwiaHR0cDovL2xvY2FsaG9zdDo5MDkwIl0sInJlYWxtX2FjY2VzcyI6eyJyb2xlcyI6WyJvZmZsaW5lX2FjY2VzcyIsInVtYV9hdXRob3JpemF0aW9uIiwidmlwIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnsidnVlLXRlc3QtYXBwIjp7InJvbGVzIjpbImRldmljZSJdfSwiYWNjb3VudCI6eyJyb2xlcyI6WyJtYW5hZ2UtYWNjb3VudCIsIm1hbmFnZS1hY2NvdW50LWxpbmtzIiwidmlldy1wcm9maWxlIl19fSwic2NvcGUiOiJwcm9maWxlIGVtYWlsIiwiZW1haWxfdmVyaWZpZWQiOnRydWUsIm5hbWUiOiJNaWtlIE1pdHRlcmVyIiwicHJlZmVycmVkX3VzZXJuYW1lIjoibWlrZSIsImdpdmVuX25hbWUiOiJNaWtlIiwiZmFtaWx5X25hbWUiOiJNaXR0ZXJlciIsImVtYWlsIjoib2ZmaWNlQG1pa2VtaXR0ZXJlci5hdCJ9.
        JoeHL2z78QtnoGUATnk9s3sIAlCLE2fRA-S2nxFEoQ67X8mvItTN8ik9OwhM68-FMrMu2IZQajH4edX4jUCtL-JPYhzhcueNAFBgqZ9JWEcYGlry7O76jntw6woMC8i1q-UrzM0gQzUmsVyUIDKD2jy9eDgO1xkhdvWKsiUwwAL9bUCGYwIYEbsomuizsY1C0wNSxNNbejtxEjBnKkpbklQHEP-v0bUWKpPgoFDzPfnhhH46ISa_gBZElm_TFXAsxU_87NebDT2Qik0HxPOueIZiseAzA6G2T1-ymGwP_HgxMaf9RZfrDbdtyyY1IVW4X6GYzrgGUU59znPUk6dRiQ        
    """.trimIndent().replace("\n","")
}

