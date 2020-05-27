package at.mikemitterer.catshostel.services

import at.mikemitterer.catshostel.model.auth.UserAuthenticationToken
import at.mikemitterer.catshostel.model.auth.UserContext
import org.keycloak.KeycloakPrincipal
import org.keycloak.KeycloakSecurityContext
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component

/**
 * Provides a custom UserContext-object
 *
 * @since   27.05.20, 16:06
 */
@Component
class UserContextService {
    @Suppress("UNCHECKED_CAST")
    val userContext: UserContext
        get() {
        val context = when(val auth = SecurityContextHolder.getContext().authentication) {
            is KeycloakAuthenticationToken -> {
                // Generated in https://bit.ly/3gszvQf
                // bzw. in https://bit.ly/3gvzHxY (KeycloakWebSecurityConfigurerAdapter)
                val principal = auth.principal as KeycloakPrincipal<KeycloakSecurityContext>

                UserContext(
                        principal.name,
                        principal.keycloakSecurityContext.token.subject,
                        auth.authorities.toList()
                )
            }
            is UserAuthenticationToken -> {
                // Generated in JWTAuthFilter
                auth.principal
            }
            else -> throw IllegalArgumentException(
                    "SecurityContext is neither a KeycloakAuthenticationToken nor a UserAuthenticationToken")
        }
        return context
    }
}