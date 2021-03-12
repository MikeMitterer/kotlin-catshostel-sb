package at.mikemitterer.catshostel.event

interface Event<D> {
     val event: String
     val data: D

     fun toJson(): String
}
