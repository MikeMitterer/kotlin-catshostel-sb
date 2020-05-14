@file:Suppress("SpringJavaInjectionPointsAutowiringInspection")

package at.mikemitterer.catshostel.routes

import at.mikemitterer.catshostel.ws.BroadcastWebSocket
import com.google.gson.Gson
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.joda.time.DateTime
import org.joda.time.Seconds
import org.joda.time.format.DateTimeFormat
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
class BasicController(
        private val wsServer: BroadcastWebSocket,
        private val gson: Gson) {

    val logger: Logger = LoggerFactory.getLogger(BasicController::class.java)

    val counter = AtomicLong()

    @GetMapping("/greeting")
    fun greeting(@RequestParam(value = "name", defaultValue = "World") name: String): Greeting {
        logger.info("Greetings!")
        return Greeting(counter.incrementAndGet(), "Hello, $name")
    }

    @GetMapping("/servus")
    fun servus(@RequestParam(value = "name", defaultValue = "World") name: String) =
            Greeting(counter.incrementAndGet(), "Servus, $name!")

    @GetMapping("/ping")
    fun pingWebSocket(@RequestParam(value = "name", defaultValue = "World") name: String) {
        val greetings = Greeting(counter.incrementAndGet(), "Hello, $name")
        wsServer.broadcast("server", gson.toJson(greetings))
        return
    }

    @GetMapping("/wait")
    fun waitForSeconds(@RequestParam(value = "seconds", defaultValue = "1") seconds: Long): String = runBlocking {
        val now = DateTime.now()

        val first = async {
            waitAndGive(seconds, 1)
        }
        val second = async {
            waitAndGive(seconds, 2)
        }

        val result1 = first.await()
        val result2 = second.await()

        val then = DateTime.now()

        val formatter = DateTimeFormat.forPattern("HH:mm:ss")
        """
            Diff between: ${formatter.print(now)} and ${formatter.print(then)} is: 
            ${Seconds.secondsBetween(now, then).seconds}sec(s)<br>
            Result1: $result1 / Result2: $result2 
        """.trimIndent()
    }

    @GetMapping("/exception")
    fun exception(): Greeting {
        throw IllegalArgumentException("Test-REST Exception")
    }
}

data class Greeting(val id: Long, val content: String)

suspend fun waitAndGive(wait: Long, giveMe: Int): Int {
    delay(wait * 1000)
    return giveMe
}
