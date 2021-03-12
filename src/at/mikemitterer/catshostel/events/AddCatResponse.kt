package at.mikemitterer.catshostel.events

import at.mikemitterer.catshostel.event.AbstractEvent
import at.mikemitterer.catshostel.event.Event
import at.mikemitterer.catshostel.model.Cat

class AddCatResponse(override val data: Cat): Event<Cat>, AbstractEvent() {
    override val event = EVENT_NAME

    companion object {
        val EVENT_NAME: String = AddCatResponse::class.java.canonicalName

        fun parse(json: String): AddCatResponse = parse(json, AddCatResponse::class.java)
    }
}

