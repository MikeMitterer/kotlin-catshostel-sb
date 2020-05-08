package at.mikemitterer.catshostel.persistence

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest


/**
 * @since 06.05.20, 14:52
 */
@SpringBootTest
// @ExtendWith(SpringExtension::class)
internal class TableFactoryTest {

    @Autowired
    private lateinit var tableFactor: TableFactory


}