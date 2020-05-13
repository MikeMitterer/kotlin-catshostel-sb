package at.mikemitterer.catshostel.ws

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import java.io.IOException
import java.util.concurrent.CopyOnWriteArrayList
import java.util.function.Consumer


/**
 *
 *
 * @since   11.05.20, 14:41
 */
class SimpleTextWSHandler : BroadcastWebSocket() {
    companion object {
        private val logger: Logger = LoggerFactory.getLogger(SimpleTextWSHandler::class.java)
    }

    private val sessions: MutableList<WebSocketSession> = CopyOnWriteArrayList<WebSocketSession>()


    @Throws(Exception::class)
    override fun afterConnectionEstablished(session: WebSocketSession) {
        sessions.add(session)
        super.afterConnectionEstablished(session)
    }

    @Throws(Exception::class)
    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
        sessions.remove(session)
        super.afterConnectionClosed(session, status)
    }

    @Throws(Exception::class)
    override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        super.handleTextMessage(session, message)
        sessions.forEach(Consumer<WebSocketSession> { webSocketSession: WebSocketSession ->
            try {
                webSocketSession.sendMessage(message)
            } catch (e: IOException) {
                logger.error("Error occurred.", e)
            }
        })
    }

    /**
     * Sends a [message] coming from a [sender] to all the members in the server, including all the connections per member.
     */
    override fun broadcast(sender: String, message: String) {
        broadcast("[$sender] $message")
    }

    override fun broadcast(message: String) {
        sessions.forEach(Consumer<WebSocketSession> { webSocketSession: WebSocketSession ->
            try {
                webSocketSession.sendMessage(TextMessage(message))
            } catch (e: IOException) {
                logger.error("Error occurred.", e)
            }
        })
    }
}