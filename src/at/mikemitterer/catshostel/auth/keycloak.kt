package at.mikemitterer.catshostel.auth

import org.keycloak.KeycloakPrincipal
import org.keycloak.KeycloakSecurityContext
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder


/**
 * Helper to retrieve the JWT from KeyCloak
 *
 * @since   27.05.20, 09:09
 */
fun getJWTToken(): String? {
    val authentication = SecurityContextHolder.getContext()
            .authentication as KeycloakAuthenticationToken

    @Suppress("UNCHECKED_CAST")
    val keycloakPrincipal = authentication
            .principal as KeycloakPrincipal<KeycloakSecurityContext>

    return keycloakPrincipal.keycloakSecurityContext.tokenString
}

fun KeycloakAuthenticationToken.asPrincipal(): KeycloakPrincipal<KeycloakSecurityContext> {
    @Suppress("UNCHECKED_CAST")
    return this.principal as KeycloakPrincipal<KeycloakSecurityContext>
}