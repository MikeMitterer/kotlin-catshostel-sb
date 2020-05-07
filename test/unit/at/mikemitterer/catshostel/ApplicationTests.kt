package at.mikemitterer.catshostel

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class ApplicationTests {

	@Test
	fun contextLoads() {
        assertThat(13).isEqualTo(13)
        assertThat(true).isEqualTo(true)
        assertThat("Mike").isEqualTo("Mike")
	}
}
