package at.mikemitterer.catshostel.routes

import at.mikemitterer.catshostel.auth.AuthenticationFacade
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * JWT protected message
 *
 * @since   07.05.20, 18:23
 */
@RestController
@RequestMapping("/protected")
class ProtectedController {
    var logger: Logger = LoggerFactory.getLogger(ProtectedController::class.java)

    @Autowired
    private lateinit var auth: AuthenticationFacade

    @GetMapping("/message")
    fun saySomething(message: String): String {
        return "Psst: (wisper) $message -> ${auth.token.principal.firstName}"
    }

}
