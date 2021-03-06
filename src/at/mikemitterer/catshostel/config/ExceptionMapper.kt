package at.mikemitterer.catshostel.config

import org.apache.commons.lang3.exception.ExceptionUtils
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus

/**
 * Universelles Exception-Mapping
 *
 * Mit @ControllerAdvice wird dieser Teil bei
 * jedem Controller eingefügt
 *
 * @since   06.05.20, 16:05
 */
@ControllerAdvice
class ExceptionMapper {

    @ResponseBody // Wandelt die Message in einen JSON-String um
    @ExceptionHandler
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    fun handleException(cause: Throwable): Map<String, Any>  {
        val message = mapOf<String, Any>(
                "message" to (cause.message ?: "no message!"),
                "stacktrace" to ExceptionUtils.getRootCauseStackTrace(cause).map {
                    it.replace("\t", " - ")
                }
        )
        return message
    }
}