package at.mikemitterer.catshostel.auth

import at.mikemitterer.catshostel.model.auth.UserAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component

/**
 *
 *
 * @since   08.05.20, 16:53
 */
@Component
class AuthenticationFacade {
    val token: UserAuthenticationToken
        get() = SecurityContextHolder.getContext().authentication as UserAuthenticationToken
}