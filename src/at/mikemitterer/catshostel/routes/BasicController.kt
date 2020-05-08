package at.mikemitterer.catshostel.routes

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.concurrent.atomic.AtomicLong

/**
 *
 *
 * @since   05.05.20, 18:23
 */
@RestController
class BasicController {
    var logger: Logger = LoggerFactory.getLogger(BasicController::class.java)

    val counter = AtomicLong()

    @GetMapping("/greeting")
    fun greeting(@RequestParam(value = "name", defaultValue = "World") name: String): Greeting {
        logger.info("Greetings!")
        return Greeting(counter.incrementAndGet(), "Hello, $name")
    }

    @GetMapping("/servus")
    fun servus(@RequestParam(value = "name", defaultValue = "World") name: String) =
            Greeting(counter.incrementAndGet(), "Servus, $name!")

    @GetMapping("/exception")
    fun exception(): Greeting {
        throw IllegalArgumentException("Test-REST Exception")
    }
}

data class Greeting(val id: Long, val content: String)