package at.mikemitterer.catshostel.config

import at.mikemitterer.catshostel.ws.BroadcastWebSocket
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.web.socket.config.annotation.EnableWebSocket
import org.springframework.web.socket.config.annotation.WebSocketConfigurer
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry


/**
 * WebSocket Configuration
 *
 * @since   11.05.20, 14:43
 */
@Configuration
@EnableWebSocket
class WebSocketConfig : WebSocketConfigurer {
    @Autowired
    private lateinit var wsServer: BroadcastWebSocket

    override fun registerWebSocketHandlers(webSocketHandlerRegistry: WebSocketHandlerRegistry) {
        webSocketHandlerRegistry.addHandler(wsServer, "/ws").setAllowedOrigins("*");
    }
}