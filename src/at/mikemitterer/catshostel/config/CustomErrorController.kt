package at.mikemitterer.catshostel.config

import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController
import org.springframework.boot.web.servlet.error.ErrorAttributes
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


/**
 *
 *
 * @since   08.05.20, 12:45
 */
@RestController
@RequestMapping(CustomErrorController.PATH)
class CustomErrorController(errorAttributes: ErrorAttributes) : AbstractErrorController(errorAttributes) {
    companion object {
        const val PATH = "/error"
    }

    @RequestMapping
    fun error(request: HttpServletRequest, response: HttpServletResponse): ResponseEntity<Map<String?, Any?>> {
        // Appropriate HTTP response code (e.g. 404 or 500) is automatically set by Spring.
        // Here we just define response body.
        val body = getErrorAttributes(
                request, true, true, false
        )

        val status: HttpStatus = getStatus(request)
        return ResponseEntity(body, status)
    }

    override fun getErrorPath(): String {
        return PATH
    }
}