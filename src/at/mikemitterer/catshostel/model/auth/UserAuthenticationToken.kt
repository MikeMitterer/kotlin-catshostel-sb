package at.mikemitterer.catshostel.model.auth

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.GrantedAuthority

/**
 *
 *
 * @since   08.05.20, 16:58
 */
class UserAuthenticationToken(user: UserContext, authorities: Collection<GrantedAuthority>)
    : UsernamePasswordAuthenticationToken(user, null, authorities) {

    override fun getPrincipal(): UserContext {
        return super.getPrincipal() as UserContext
    }
}