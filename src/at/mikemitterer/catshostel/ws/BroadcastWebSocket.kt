package at.mikemitterer.catshostel.ws

import org.springframework.stereotype.Component
import org.springframework.web.socket.handler.TextWebSocketHandler

/**
 * Sends Broadcast-Message to all WS-Clients
 *
 * @since   13.05.20, 12:36
 */
@Component
abstract class BroadcastWebSocket: TextWebSocketHandler() {
    abstract fun broadcast(message: String)
    abstract fun broadcast(sender: String, message: String)
}