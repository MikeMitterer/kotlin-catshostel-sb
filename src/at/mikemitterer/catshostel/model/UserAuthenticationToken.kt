package at.mikemitterer.catshostel.model

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.GrantedAuthority

/**
 *
 *
 * @since   08.05.20, 16:58
 */
class UserAuthenticationToken(user: User, authorities: Collection<GrantedAuthority>)
    : UsernamePasswordAuthenticationToken(user, null, authorities) {

    override fun getPrincipal(): User {
        return super.getPrincipal() as User
    }
}