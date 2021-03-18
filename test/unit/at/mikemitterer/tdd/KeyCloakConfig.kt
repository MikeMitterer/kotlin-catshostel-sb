package at.mikemitterer.tdd

/**
 * Wird zum Generieren der Tokens in [ at.mikemitterer.tdd.KeyCloak ] benötigt
 *
 * Eigentlich sollten die Daten mit den Daten in application.properties übereinstimmen
 *
 * Für die Tests wird folgendes vorausgesetzt:
 *      Roles: user + admin
 *
 *      User:
 *          cat1, cat1@catshostel.at, PW: 12345678, user + admin
 *          nicki, nicki@catshostel.at, PW: 12345678, user
 */
object KeyCloakConfig {
    object Server {
        // Realm und
        private const val realm = "CatsHostel"
        private const val authHost = "http://jenkins.int.mikemitterer.at:9000" // KeyCloak-Server

        // ... Clients muss in KeyCloak angelegt sein!
        const val clientID = "catshostel-app"

        object Urls {
            const val issuer = "${authHost}/auth/realms/${realm}"

            const val tokenUrl = "${issuer}/protocol/openid-connect/token"
            const val certsUrl = "${issuer}/protocol/openid-connect/certs"

            // const val configUrl = "${issuer}/.well-known/openid-configuration"
        }
    }
}
