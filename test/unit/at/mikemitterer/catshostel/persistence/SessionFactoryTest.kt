package at.mikemitterer.catshostel.persistence

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension


/**
 * @since 06.05.20, 14:52
 */
@SpringBootTest
@ExtendWith(SpringExtension::class)
internal class SessionFactoryTest {

    @Autowired
    private lateinit var sessionFactor: SessionFactory

    @Test
    fun testSFInjection() {
        assertThat(sessionFactor).isNotNull
    }
}