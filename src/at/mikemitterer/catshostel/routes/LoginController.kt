package at.mikemitterer.catshostel.routes

import at.mikemitterer.catshostel.auth.createJWTFor
import at.mikemitterer.catshostel.model.Credentials
import com.google.gson.Gson
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest

/**
 * Simples User-Login
 *
 * @since   07.05.20, 18:23
 */
@RestController
class LoginController {
    var logger: Logger = LoggerFactory.getLogger(BasicController::class.java)

    @Autowired
    private lateinit var gson: Gson

    @PostMapping("/login", consumes = [ MediaType.APPLICATION_FORM_URLENCODED_VALUE] )
    fun loginViaForm(credentials: Credentials): String {
        return createJWTFor(credentials.username)
    }

    @PostMapping("/login")
    fun loginViaObject(@RequestBody credentials: Credentials): String {
        return createJWTFor(credentials.username)
    }

    /**
     * Propagates the logout to the Keycloak infrastructure
     * @param request
     * @return
     * @throws Exception
     */
    @PostMapping("/logout")
    @Throws(Exception::class)
    fun logout(request: HttpServletRequest): String? {
        request.logout()
        return "redirect:/admin"
    }
}
