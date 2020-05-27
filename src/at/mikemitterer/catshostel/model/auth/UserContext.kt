package at.mikemitterer.catshostel.model.auth

import org.springframework.security.core.GrantedAuthority
import java.security.Principal

/**
 * UserContext will be generated for KeyCloak AND SpringWebSecurity
 *
 * For SpringWebSecurity the UserContext is generated in [at.mikemitterer.catshostel.filter.JWTAuthFilter]
 * KeyCloak has it's own filter...
 *
 * [at.mikemitterer.catshostel.services.UserContextService] generates the
 * right UserContext
 *
 * @since   27.05.20, 11:49
 */
class UserContext(
        private val name: String,
        val id: String,
        val roles: List<GrantedAuthority>) : Principal {
    
    override fun getName(): String = this.name
}