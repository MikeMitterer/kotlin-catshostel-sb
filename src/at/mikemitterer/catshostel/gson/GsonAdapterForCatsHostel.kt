package at.mikemitterer.catshostel.gson

import com.google.gson.Gson
import com.google.gson.GsonBuilder




/**
 * Hier können auch diverse, spezielle Adapter registriert werden
 */
object GsonAdapterForCatsHostel {
    val gson: Gson by lazy {
        val gsonBuilder = with(GsonBuilder()) {
            // excludeFieldsWithoutExposeAnnotation()
            enableComplexMapKeySerialization()

            setPrettyPrinting()

            // registerTypeAdapter(it.key, it.value)
        }

        gsonBuilder.create()
    }
}
