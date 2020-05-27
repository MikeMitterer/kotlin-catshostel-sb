package at.mikemitterer.tdd

/**
 *
 *
 * @since   26.05.20, 18:07
 */
object TestConfig {
    object KeyCloakServer {
        private const val realm = "MobiAdTest"
        private const val authHost = "http://jenkins.int.mikemitterer.at:9000" // KeyCloak-Server

        const val clientID = "catshostel-app"

        object Urls {
            const val issuer = "${authHost}/auth/realms/${realm}"

            const val tokenUrl = "${issuer}/protocol/openid-connect/token"
            const val certsUrl = "${issuer}/protocol/openid-connect/certs"

            // const val configUrl = "${issuer}/.well-known/openid-configuration"
        }
    }
}