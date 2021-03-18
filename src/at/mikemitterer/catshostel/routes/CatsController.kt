package at.mikemitterer.catshostel.routes

import at.mikemitterer.catshostel._config.API_V1
import at.mikemitterer.catshostel.events.AddCatResponse
import at.mikemitterer.catshostel.model.Cat
import at.mikemitterer.catshostel.persistence.CatDAO
import at.mikemitterer.catshostel.persistence.TableFactory
import kotlinx.coroutines.runBlocking
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

/**
 * CRUD-Sample für die Bewohner des Hostels
 */
@RestController
@RequestMapping("$API_V1/cat")
class CatsController(
    private val tableFactory: TableFactory
    ) {

    @GetMapping("/id/{id}")
    fun getCatByID(@PathVariable(value = "id")  id: Number) = runBlocking {
        val dao = tableFactory.get<CatDAO>()
        dao.catByID(id)
    }

    @GetMapping("/")
    fun getAllCats(): List<Cat> {
        val dao = tableFactory.get<CatDAO>()
        return dao.all
    }


    @PostMapping("/name/{name}/age/{age}")
    @ResponseStatus(HttpStatus.CREATED )
    fun insertName(@PathVariable(value = "name")  name: String, @PathVariable(value = "age")  age: Int): AddCatResponse = runBlocking {
        val cat = Cat(name, age)
        val dao = tableFactory.get<CatDAO>()

        dao.upsert(cat)

        AddCatResponse(cat)
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun deleteCat(@PathVariable(name = "id") id: String) = runBlocking {
        val dao = tableFactory.get<CatDAO>()
        dao.delete(id.toInt())
    }

    @DeleteMapping("/")
    @ResponseStatus(HttpStatus.OK)
    fun deleteAllCats() = runBlocking {
        val dao = tableFactory.get<CatDAO>()
        dao.deleteAll()
    }

}
