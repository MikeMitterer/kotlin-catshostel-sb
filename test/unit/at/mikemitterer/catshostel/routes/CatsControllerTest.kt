package at.mikemitterer.catshostel.routes

import at.mikemitterer.catshostel._config.API_V1
import at.mikemitterer.catshostel.events.AddCatResponse
import at.mikemitterer.catshostel.model.Cat
import at.mikemitterer.tdd.MakeURI
import com.google.gson.Gson
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import kotlin.random.Random


/**
 * @since 06.05.20, 14:52
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class CatsControllerTest {
    @LocalServerPort
    private val port = 0

    @Autowired
    private lateinit var server: TestRestTemplate

    @Autowired
    private lateinit var gson: Gson

    companion object {
        const val PATH_GET_CAT = "${API_V1}/cat/id/{id}"
        const val PATH_GET_ALL_CATS = "${API_V1}/cat/"
        const val PATH_ADD_CAT = "${API_V1}/cat/name/{name}/age/{age}"
        const val PATH_DELETE_CAT = "${API_V1}/cat/id/{age}"
        const val PATH_DELETE_ALL_CATS = "${API_V1}/cat/"
    }

    @BeforeEach
    fun setUp() {
        deleteAllCats()
    }

    @Test
    fun testUriBuilder() {
        val uriBuilder = MakeURI.forServer(server)
        val uri = uriBuilder.path("/cat/name/{name}/age/{age}").build(mapOf("name" to "Mike", "age" to 99))

        assertThat(uri.toString()).isEqualTo("http://localhost:${uri.port}/cat/name/Mike/age/99")
    }

    @Test
    fun testPortIsNot0() {
        assertThat(port).isNotEqualTo(0)
    }

    @Test
    fun testRestTemplateNotNull() {
        assertThat(server).isNotNull
    }

    @Test
    fun testGetAllCats() {
        assertThat(allCats).isNotNull
    }

    @Test
    fun testDeleteAllCats() {
        assertThat(deleteAllCats()).isEqualTo(HttpStatus.OK)
        assertThat(allCats.size).isEqualTo(0)
    }

    @Test
    fun testInsertCat() {
        val cat1 = Cat("Mike-${Random.nextLong(Long.MAX_VALUE)}", 99)
        val cat2 = Cat("Mike-${Random.nextLong(Long.MAX_VALUE)}", 99)

        val nrOfCats = allCats.size
        for(index in 1..5) {
            insertCat(cat1)
        }
        assertThat(allCats.size).isEqualTo(nrOfCats + 1)

        insertCat(cat2)
        assertThat(allCats.size).isEqualTo(nrOfCats + 2)
    }

    @Test
    fun testGetCatByID() {
        val cat = insertCat(Cat("Mike-${Random.nextLong(Long.MAX_VALUE)}", 99))

        val uri = MakeURI.forServer(server).path(PATH_GET_CAT).build(
            mapOf("id" to cat.ID)
        )

        val response: ResponseEntity<Cat> = server.exchange(
            uri,
            HttpMethod.GET,
            null,
            object : ParameterizedTypeReference<Cat>() {} )

        assertThat(response).isNotNull
        val catFromServer = response.body!!

        assertThat(cat.ID).isEqualTo(catFromServer.ID)
        assertThat(cat.name).isEqualTo(catFromServer.name)
        assertThat(cat.age).isEqualTo(catFromServer.age)

    }

    // - Helper Functions --------------------------------------------------------------------------

    private val allCats: List<Cat>
        get() {
            val uri = MakeURI.forServer(server).path(PATH_GET_ALL_CATS).build()

            val response: ResponseEntity<List<Cat>> = server.exchange(
                uri,
                HttpMethod.GET,
                null,
                object : ParameterizedTypeReference<List<Cat>>() {} )

            assertThat(response).isNotNull
            return response.body!!
        }

    private fun deleteAllCats(): HttpStatus {
        val uri = MakeURI.forServer(server).path(PATH_DELETE_ALL_CATS).build()
        val response = server.exchange(
            uri,
            HttpMethod.DELETE,
            null,
            ResponseEntity::class.java
        )

        return response.statusCode
    }

    private fun insertCat(cat: Cat): Cat {
        val catMap = mapOf("name" to cat.name, "age" to cat.age)
        val uri = MakeURI.forServer(server).path(PATH_ADD_CAT).build(catMap)

        val response: ResponseEntity<AddCatResponse> = server.exchange(
            uri,
            HttpMethod.POST,
            null,
            AddCatResponse::class.java )

        assertThat(response).isNotNull
        assertThat(response.body).isNotNull

        val addCatResponse = response.body
        assertThat(addCatResponse?.data?.name).isEqualTo(catMap["name"])

        return addCatResponse!!.data
    }
}
