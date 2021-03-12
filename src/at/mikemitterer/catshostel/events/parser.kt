package at.mikemitterer.catshostel.events

import at.mikemitterer.catshostel.gson.GsonAdapterForCatsHostel

fun <T> parse(json: String, classOfT: Class<T>): T = GsonAdapterForCatsHostel.gson.fromJson(json, classOfT)
