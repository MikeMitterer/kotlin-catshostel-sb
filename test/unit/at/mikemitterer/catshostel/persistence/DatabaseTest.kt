package at.mikemitterer.catshostel.persistence

import at.mikemitterer.catshostel.model.Cat
import at.mikemitterer.tdd.TestUtils.predictName
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest


@SpringBootTest
@ExperimentalCoroutinesApi
class DatabaseTest {
    @Autowired
    private lateinit var tableFactor: TableFactory

    @Test
    fun testInjection() {
        assertThat(tableFactor).isNotNull
    }
    
    @Test
    fun testInsert() = runBlockingTest {
        val dao = tableFactor.get<CatDAO>()
        val number = dao.numberOfCats

        addNewCat(dao)

        val newNumber = dao.numberOfCats
        assertThat(newNumber).isEqualTo(number + 1)
    }

    @Test
    fun testSelectAll()  = runBlockingTest {
        val dao = tableFactor.get<CatDAO>()

        val cat = Cat(predictName("Pepples"), 99)
        dao.insert(cat)

        assertThat(dao.all.count()).isGreaterThan(0)
    }

    @Test
    fun testDelete()  = runBlockingTest {
        val dao = tableFactor.get<CatDAO>()

        val cat = addNewCat(dao)
        val numberBeforeDelete = dao.numberOfCats

        dao.delete(cat.ID)
        assertThat(dao.numberOfCats < numberBeforeDelete).isTrue()
    }

    @Test
    fun testUpdate()  = runBlockingTest {
        val dao = tableFactor.get<CatDAO>()
        val cat = addNewCat(dao)

        val oldAge = cat.age

        cat.age = cat.age + 1
        dao.update(cat)

        val updatedCat = dao.catByID(cat.ID)
        assertThat(updatedCat.age).isEqualTo(oldAge + 1)
    }
}

private suspend fun addNewCat(dao: CatDAO): Cat {
    val name = predictName("Pepples")
    val cat = Cat(name, 99)
    dao.insert(cat)
    return cat
}