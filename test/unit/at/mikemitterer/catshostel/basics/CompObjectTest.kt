package at.mikemitterer.catshostel.basics

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class COTest {
    val myID: Int = ++iCounter

    companion object {
        var iCounter: Int = 0
    }
}

/**
 *
 *
 * @since   16.05.20, 10:01
 */
class CompObjectTest {

    @Test
    fun testCOTEst() {
        val cos = (1..5).map { COTest() }

        assertThat(COTest.iCounter).isEqualTo(5)
        assertThat(cos[1].myID).isEqualTo(2)
    }
}
