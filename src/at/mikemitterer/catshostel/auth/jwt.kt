package at.mikemitterer.catshostel.auth

import io.jsonwebtoken.Claims
import io.jsonwebtoken.JwtBuilder
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.joda.time.DateTime
import java.io.File
import java.net.URL
import java.security.KeyFactory
import java.security.PrivateKey
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import java.util.*

/**
 * Reads the Public-Key-File and converts its content to RSAPublicKey
 *
 * Usage:
 *     val publicKeyFile = File("/rsakeys/jwt.pub.pem")
 *     val pubKey = getPublicKey(publicKeyFile)
 *
 * @since   04.05.20, 17:49
 */
fun getPublicKey(file: File, modifier: (key: String) -> String = { it }): RSAPublicKey {
    val publicKey = modifier(file.readText().stripPEMMarker())
    return toRSAKey(publicKey)
}

/**
 * Reads the Public-Key-Resource and converts its content to RSAPublicKey
 *
 * Usage:
 *     val resourceUrl = javaClass.getResource("/rsakeys/jwt.pub.pem")
 *     val pubKey = getPublicKey(resourceUrl)
 *
 * @since   05.05.20, 17:49
 */
fun getPublicKey(url: URL, modifier: (key: String) -> String = { it }): RSAPublicKey {
    val publicKey = modifier(url.readText().stripPEMMarker())
    return toRSAKey(publicKey)
}

fun toRSAKey(publicKey: String): RSAPublicKey {
    val keyFactory: KeyFactory = KeyFactory.getInstance("RSA")

    val keySpecX509 = X509EncodedKeySpec(Base64.getDecoder().decode(publicKey))

    return keyFactory.generatePublic(keySpecX509) as RSAPublicKey
}

fun getPrivateKey(file: File): RSAPrivateKey {
    val privateKey = file.readText().stripPEMMarker()

    val keyFactory: KeyFactory = KeyFactory.getInstance("RSA")

    val keySpecPKCS8 = PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKey))

    return keyFactory.generatePrivate(keySpecPKCS8) as RSAPrivateKey
}

fun createJWT(claims: Map<String, Any>, privateKey: String): String {
    //The JWT signature algorithm we will be using to sign the token
    val signatureAlgorithm = SignatureAlgorithm.RS256

    val apiKeySecretBytes: ByteArray = Base64.getDecoder().decode(privateKey.stripPEMMarker())

    val keyFactory = KeyFactory.getInstance("RSA")
    val keySpec = PKCS8EncodedKeySpec(apiKeySecretBytes)
    val signingKey: PrivateKey = keyFactory.generatePrivate(keySpec)

    val builder: JwtBuilder = Jwts.builder()
            .setClaims(claims)
            .signWith(signingKey, signatureAlgorithm)

    return builder.compact()
}


/**
 * Creates a JWT for specific user (mimics KeyCloak-Structure)
 */
fun createJWTFor(username: String): String {
    val privateKey = "/rsakeys/jwt.pkcs8.pem".asResource().stripPEMMarker()
    val now = DateTime.now()

    val jwt = createJWT(mutableMapOf(
            Claims.EXPIRATION to now.plusMinutes(5).toDate(),
            Claims.ISSUED_AT to now.toDate(),
            Claims.ISSUER to "mmit",
            Claims.AUDIENCE to "account",
            Claims.SUBJECT to "Subject",
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
            "preferred_username" to username,
            "given_name" to username.capitalize(),
            "family_name" to "Mitterer",
            "email" to "${username}@mikemitterer.at"
    ), privateKey)

    return jwt
}
