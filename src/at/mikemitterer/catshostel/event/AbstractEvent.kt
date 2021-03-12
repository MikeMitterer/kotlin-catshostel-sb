package at.mikemitterer.catshostel.event

import at.mikemitterer.catshostel.gson.GsonAdapterForCatsHostel

abstract class AbstractEvent {
    fun toJson(): String = GsonAdapterForCatsHostel.gson.toJson(this)
}
