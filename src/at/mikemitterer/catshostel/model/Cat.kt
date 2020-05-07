package at.mikemitterer.catshostel.model

import com.google.gson.Gson

class Cat {
    companion object {
        fun fromJson(json: String): Cat {
            val gson = Gson()
            return gson.fromJson(json, Cat::class.java)
        }
    }
    /**
     * Wird bei insert automatisch von iBatis gesetzt
     */
    var ID = 0L

    var name: String? = null

    var age: Int = 0

    /** Für iBatis  */
    constructor() {}

    constructor(name: String, age: Int) {
        this.name = name
        this.age = age
    }
}