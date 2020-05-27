package at.mikemitterer.catshostel.filter

import at.mikemitterer.catshostel.auth.asResource
import at.mikemitterer.catshostel.auth.stripPEMMarker
import at.mikemitterer.catshostel.auth.toRSAKey
import at.mikemitterer.catshostel.model.auth.UserAuthenticationToken
import at.mikemitterer.catshostel.model.auth.UserContext
import io.jsonwebtoken.*
import io.jsonwebtoken.io.IOException
import org.springframework.http.HttpHeaders
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter
import java.security.interfaces.RSAPublicKey
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


/**
 * Interprets the JWT and wraps everything into UsernamePasswordAuthenticationToken
 *
 * @since   08.05.20, 11:06
 */
class JWTAuthFilter : OncePerRequestFilter() {
    companion object {
        private const val BAERER_PREFIX = "Bearer "
    }

    @Throws(ServletException::class, IOException::class)
    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {
        try {
            if (hasJWTToken(request, response)) {
                val claims = validateToken(request)
                claims?.body?.get("realm_access")?.let {
                    setUpSpringAuthentication(claims?.body)
                } ?: SecurityContextHolder.clearContext()

                // if (claims?.body?.get("realm_access") as Map<*, *>? != null) {
                // } else {
                //     SecurityContextHolder.clearContext()
                // }
            }
            chain.doFilter(request, response)
        } catch (e: ExpiredJwtException) {
            response.status = HttpServletResponse.SC_FORBIDDEN
            response.sendError(HttpServletResponse.SC_FORBIDDEN, e.message)
            return
        } catch (e: UnsupportedJwtException) {
            response.status = HttpServletResponse.SC_FORBIDDEN
            response.sendError(HttpServletResponse.SC_FORBIDDEN, e.message)
            return
        } catch (e: MalformedJwtException) {
            response.status = HttpServletResponse.SC_FORBIDDEN
            response.sendError(HttpServletResponse.SC_FORBIDDEN, e.message)
            return
        }
    }

    private fun validateToken(request: HttpServletRequest): Jws<Claims>? {
        val jwtToken: String = request.getHeader(HttpHeaders.AUTHORIZATION).replace(BAERER_PREFIX, "")

        val jwtParser = Jwts.parserBuilder()
                .setSigningKey(publicKey)
                .build()

        return jwtParser.parseClaimsJws(jwtToken)
    }

    private val publicKey: RSAPublicKey by lazy {
        val publicKey = "/rsakeys/jwt.pub.pem".asResource().stripPEMMarker()
        toRSAKey(publicKey)
    }

    /**
     * Authentication method in Spring flow
     *
     * @param claims
     */
    private fun setUpSpringAuthentication(body: Claims) {
        val realm = checkNotNull(body.get("realm_access") as Map<String, List<String>>)
        val roles = checkNotNull(realm.get("roles"))
        val subject = checkNotNull(body.get(Claims.SUBJECT) as String)
        val firstName = checkNotNull(body.get("given_name") as String)
        val familyName = checkNotNull(body.get("family_name") as String)
        val email = checkNotNull(body.get("email") as String)

        val authorities = roles.map { SimpleGrantedAuthority(it) }
        val auth = UserAuthenticationToken(UserContext(
                "$firstName $familyName", subject, authorities
        ), authorities)

        SecurityContextHolder.getContext().authentication = auth
    }

    private fun hasJWTToken(request: HttpServletRequest, res: HttpServletResponse): Boolean {
        val authenticationHeader: String? = request.getHeader(HttpHeaders.AUTHORIZATION)
        return !(authenticationHeader == null || !authenticationHeader.startsWith(BAERER_PREFIX))
    }
}