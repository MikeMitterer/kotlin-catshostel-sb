package at.mikemitterer.catshostel.persistence

import org.apache.ibatis.io.Resources
import org.apache.ibatis.session.SqlSessionFactory
import org.apache.ibatis.session.SqlSessionFactoryBuilder
import org.springframework.context.annotation.Configuration

/**
 * Erstellt eine SessionFactor zur Verwendung mit der Datenbank
 * 
 * Usage:
 *      val sqlSessionFactory = SessionFactory.sqlSessionFactory
 *
 * @since   09.04.20, 09:39
 */
@Configuration
class TableFactory {
    @Suppress("MemberVisibilityCanBePrivate")
    val sqlSessionFactory: SqlSessionFactory

    init {
        val resource = "mybatis-config.xml";
        val inputStream = Resources.getResourceAsStream(resource);

        val reader = Resources.getResourceAsReader(resource)
        val environment = System.getenv("MM_DB_ENV") ?: "development"
        sqlSessionFactory = SqlSessionFactoryBuilder().build(reader /*, "development"*/)
    }

    final inline fun <reified T> get(): T {
        when(T::class) {
            CatDAO::class -> return CatDAOImpl(this.sqlSessionFactory) as T
            else -> throw IllegalArgumentException("Type ${T::class} not supported!")
        }
    }
}