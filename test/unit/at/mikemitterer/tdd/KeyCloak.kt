package at.mikemitterer.tdd

import at.mikemitterer.catshostel.config.GsonConfig
import com.auth0.jwk.UrlJwkProvider
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.DecodedJWT
import com.auth0.jwt.interfaces.JWTVerifier
import com.google.gson.JsonObject
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jws
import io.jsonwebtoken.Jwts
import org.slf4j.LoggerFactory
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import java.net.URL
import java.security.interfaces.RSAPublicKey

/**
 * Helper zum Generieren der Tokens
 *
 * @since   26.05.20, 18:18
 */
class KeyCloak(private val restTemplate: TestRestTemplate) {
    private val logger = LoggerFactory.getLogger(KeyCloak::class.java.simpleName)

    class Tokens(val accessToken: String, val refreshToken: String)

    fun requestToken(username: String, password: String): Tokens {
        val parameters: MultiValueMap<String, String> = LinkedMultiValueMap<String, String>().apply {
            add("username", username)
            add("password", password)
            add("client_id", KeyCloakConfig.Server.clientID)
            add("grant_type", "password")
        }

        val response = restTemplate
                .postFormEntity(KeyCloakConfig.Server.Urls.tokenUrl, parameters, JsonObject::class.java)

        val obj = checkNotNull(response.body)
        return toToken(obj)
    }

    fun refreshToken(refreshToken: String): Tokens {
        val response = restTemplate
                .postFormEntity(KeyCloakConfig.Server.Urls.tokenUrl, LinkedMultiValueMap<String, String>().apply {
                    add("client_id",  KeyCloakConfig.Server.clientID)
                    add("grant_type", "refresh_token")
                    add("refresh_token", refreshToken)
                }, JsonObject::class.java)

        val obj = checkNotNull(response.body)
        return toToken(obj)
    }

    fun verifyWithAuth0(tokens: Tokens): DecodedJWT {
        val jwt = checkNotNull(JWT.decode(tokens.accessToken))

        //val jwkProvider = UrlJwkProvider("https://YOUR_TENANT.auth0.com/")
        val jwkProvider = UrlJwkProvider( URL(KeyCloakConfig.Server.Urls.certsUrl)).get(jwt.keyId);

        val pubKey = jwkProvider.publicKey
        logger.info(pubKey.toString())

        val algorithm: Algorithm = Algorithm.RSA256(jwkProvider.publicKey as RSAPublicKey, null)

        val verifier: JWTVerifier = JWT.require(algorithm)
                .withIssuer(KeyCloakConfig.Server.Urls.issuer)
                .acceptExpiresAt(0)
                .build()

        verifier.verify(tokens.accessToken)
        return jwt
    }

    fun verifyWithJWTS(tokens: Tokens, log: Boolean = true): Jws<Claims> {
        val jwt = JWT.decode(tokens.accessToken)

        val jwkProvider = UrlJwkProvider( URL(KeyCloakConfig.Server.Urls.certsUrl)).get(jwt.keyId)

        val jwtParser = Jwts.parserBuilder()
                .setSigningKey(jwkProvider.publicKey as RSAPublicKey)
                .build()

        val claims = checkNotNull(jwtParser.parseClaimsJws(tokens.accessToken))

        if(log) {
            val gson = GsonConfig().gson()

            logger.info("Header: ${claims.header}")
            logger.info("Body: ${gson.toJson(claims.body)}")
            logger.info("Signature: ${claims.signature}")
        }
        return claims
    }

    private fun toToken(obj: JsonObject): Tokens {
        val gson = GsonConfig().gson()
        val json = gson.toJson(obj)
        logger.info(json)

        return Tokens(
                obj.get("access_token").asString,
                obj.get("refresh_token").asString
        )
    }
}