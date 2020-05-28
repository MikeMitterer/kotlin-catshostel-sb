package at.mikemitterer.tdd

/**
 * Wird zum Generieren der Tokens in [ at.mikemitterer.tdd.KeyCloak ] benötigt
 *
 * Eigentlich sollten die Daten mit den Daten in application.properties übereinstimmen
 *
 * @since   26.05.20, 18:07
 */
object KeyCloakConfig {
    object Server {
        private const val realm = "CatsHostel"
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