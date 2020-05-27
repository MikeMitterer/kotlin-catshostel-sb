package at.mikemitterer.catshostel.routes

import at.mikemitterer.catshostel.services.UserContextService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * JWT protected message
 *
 * @since   07.05.20, 18:23
 */
@Suppress("DuplicatedCode")
@RestController
@RequestMapping("/protected")
class ProtectedController(private val userContextService: UserContextService) {
    var logger: Logger = LoggerFactory.getLogger(ProtectedController::class.java)

    @GetMapping("/message")
    fun saySomething(message: String): String {
        val userContext = userContextService.userContext
        return "Psst: (whisper) $message -> ${userContext.name}"
    }

    @GetMapping("/message4admin")
    fun saySomethingToAdmin(message: String): String {
        val userContext = userContextService.userContext
        return "Psst: (whisper to admin) $message -> ${userContext.name}"
    }
}
