package at.mikemitterer.catshostel.ws

/**
 * Sample:
 *      https://ktor.io/samples/app/chat.html
 *
 * @since   16.04.20, 09:14
 */

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import java.nio.channels.ClosedChannelException
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.atomic.AtomicInteger

/**
 * A chat session is identified by a unique nonce ID. This nonce comes from a secure random source.
 */
data class ChatSession(val id: String)

/**
 * Class in charge of the logic of the chat server.
 * It contains handlers to events and commands to send messages to specific users in the server.
 */

class ChatServer : BroadcastWebSocket() {
    companion object {
        private val logger: Logger = LoggerFactory.getLogger(ChatServer::class.java)
    }

    /**
     * Atomic counter used to get unique user-names based on the maxiumum users the server had.
     */
    val usersCounter = AtomicInteger()

    /**
     * A concurrent map associating session IDs to user names.
     */
    val memberNames = ConcurrentHashMap<String, String>()

    /**
     * Associates a session-id to a set of websockets.
     * Since a browser is able to open several tabs and windows with the same cookies and thus the same session.
     * There might be several opened sockets for the same client.
     */
    val members = ConcurrentHashMap<String, MutableList<WebSocketSession>>()

    /**
     * A list of the lastest messages sent to the server, so new members can have a bit context of what
     * other people was talking about before joining.
     */
    val lastMessages = LinkedList<String>()

    @Throws(Exception::class)
    override fun afterConnectionEstablished(session: WebSocketSession) {
        super.afterConnectionEstablished(session)
        memberJoin(session.id, session)
    }

    @Throws(Exception::class)
    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
        super.afterConnectionClosed(session, status)
         memberLeft(session.id, session )
    }

    @Throws(Exception::class)
    override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        super.handleTextMessage(session, message)
        receivedMessage(session.id, message.payload)
    }

    /**
     * Sends a [message] to all the members in the server, including all the connections per member.
     */
    override fun broadcast(message: String) {
        members.values.forEach { socket ->
            socket.send(TextMessage(message))
        }
    }

    /**
     * Sends a [message] coming from a [sender] to all the members in the server, including all the connections per member.
     */
    override fun broadcast(sender: String, message: String) {
        val name = memberNames[sender] ?: sender
        broadcast("[$name] $message")
    }

    /**
     * We received a message. Let's process it.
     */
    private fun receivedMessage(id: String, command: String) {
        // We are going to handle commands (text starting with '/') and normal messages
        when {
            // The command `who` responds the user about all the member names connected to the user.
            command.startsWith("/who") -> who(id)
            // The command `user` allows the user to set its name.
            command.startsWith("/user") -> {
                // We strip the command part to get the rest of the parameters.
                // In this case the only parameter is the user's newName.
                val newName = command.removePrefix("/user").trim()
                // We verify that it is a valid name (in terms of length) to prevent abusing
                when {
                    newName.isEmpty() -> sendTo(id, "server::help", "/user [newName]")
                    newName.length > 50 -> sendTo(
                            id,
                            "server::help",
                            "new name is too long: 50 characters limit"
                    )
                    else -> memberRenamed(id, newName)
                }
            }
            // The command 'help' allows users to get a list of available commands.
            command.startsWith("/help") -> help(id)
            // If no commands matched at this point, we notify about it.
            command.startsWith("/") -> sendTo(
                    id,
                    "server::help",
                    "Unknown command ${command.takeWhile { !it.isWhitespace() }}"
            )

            // Handle a normal message.
            else -> message(id, command)
        }
    }

    /**
     * Handles that a member identified with a session id and a socket joined.
     */
    private fun memberJoin(sessionID: String, socket: WebSocketSession) {
        // Checks if this user is already registered in the server and gives him/her a temporal name if required.
        val name = memberNames.computeIfAbsent(sessionID) { "user${usersCounter.incrementAndGet()}" }

        // Associates this socket to the member id.
        // Since iteration is likely to happen more frequently than adding new items,
        // we use a `CopyOnWriteArrayList`.
        // We could also control how many sockets we would allow per client here before appending it.
        // But since this is a sample we are not doing it.
        val list = members.computeIfAbsent(sessionID) { CopyOnWriteArrayList<WebSocketSession>() }
        list.add(socket)

        // Only when joining the first socket for a member notifies the rest of the users.
        if (list.size == 1) {
            broadcast("server", "Member joined: $name.")
        }

        // Sends the user the latest messages from this server to let the member have a bit context.
        val messages = synchronized(lastMessages) { lastMessages.toList() }
        for (message in messages) {
            socket.sendMessage(TextMessage(message))
        }
    }

    /**
     * Handles a [member] idenitified by its session id renaming [to] a specific name.
     */
    private fun memberRenamed(member: String, to: String) {
        // Re-sets the member name.
        val oldName = memberNames.put(member, to) ?: member
        // Notifies everyone in the server about this change.
        broadcast("server", "Member renamed from $oldName to $to")
    }

    /**
     * Handles that a [member] with a specific [socket] left the server.
     */
    private fun memberLeft(member: String, socket: WebSocketSession) {
        // Removes the socket connection for this member
        val connections = members[member]
        connections?.remove(socket)

        // If no more sockets are connected for this member, let's remove it from the server
        // and notify the rest of the users about this event.
        if (connections != null && connections.isEmpty()) {
            val name = memberNames.remove(member) ?: member
            broadcast("server", "Member left: $name.")
        }
    }

    /**
     * Handles the 'who' command by sending the member a list of all all members names in the server.
     */
    private fun who(sender: String) {
        members[sender]?.send(TextMessage(memberNames.values.joinToString(prefix = "[server::who] ")))
    }

    /**
     * Handles the 'help' command by sending the member a list of available commands.
     */
    private fun help(sender: String) {
        members[sender]?.send(TextMessage("[server::help] Possible commands are: /user, /help and /who"))
    }

    /**
     * Handles sending to a [recipient] from a [sender] a [message].
     *
     * Both [recipient] and [sender] are identified by its session-id.
     */
    fun sendTo(recipient: String, sender: String, message: String) {
        members[recipient]?.send(TextMessage("[$sender] $message"))
    }

    /**
     * Handles a [message] sent from a [sender] by notifying the rest of the users.
     */
    fun message(sender: String, message: String) {
        // Pre-format the message to be send, to prevent doing it for all the users or connected sockets.
        val name = memberNames[sender] ?: sender
        val formatted = "[$name] $message"

        // Sends this pre-formatted message to all the members in the server.
        broadcast(formatted)

        // Appends the message to the list of [lastMessages] and caps that collection to 100 items to prevent
        // growing too much.
        synchronized(lastMessages) {
            lastMessages.add(formatted)
            if (lastMessages.size > 100) {
                lastMessages.removeFirst()
            }
        }
    }

    /**
     * Sends a [message] to a list of [this] [WebSocketSession].
     */
    private fun List<WebSocketSession>.send(message: TextMessage) {
        forEach {
            try {
                it.sendMessage(message)
            } catch (t: Throwable) {
                try {
                    it.close(CloseStatus(CloseStatus.PROTOCOL_ERROR.code, t.message))
                } catch (ignore: ClosedChannelException) {
                    // at some point it will get closed
                }
            }
        }
    }
}

