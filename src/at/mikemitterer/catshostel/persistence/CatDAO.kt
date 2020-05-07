package at.mikemitterer.catshostel.persistence

import at.mikemitterer.catshostel.model.Cat
import at.mikemitterer.catshostel.persistence.mapper.CatsMapper
import org.apache.ibatis.session.SqlSessionFactory

/**
 * Verwendet das selbe Interface wie CatsMapper - kann aber später einfach geändert werden
 */
interface CatDAO {
    val numberOfCats: Long
    val all: List<Cat>
    suspend fun catByID(id: Number): Cat
    suspend fun insert(cat: Cat)
    suspend fun update(cat: Cat)
    suspend fun delete(id: Number)
    suspend fun deleteAll()
}

class CatDAOImpl(
        private val sessionFactory: SqlSessionFactory) : CatDAO {
    
    override suspend fun insert(cat: Cat) {
        sessionFactory.openSession().use {
            it.getMapper(CatsMapper::class.java).insert(cat)
            it.commit()
        }
    }

    override val all: List<Cat>
        get() {
            sessionFactory.openSession().use { session ->
                return session.getMapper(CatsMapper::class.java).selectAll()
            }
    }

    override suspend fun catByID(id: Number): Cat {
        sessionFactory.openSession().use { session ->
            val cat = session.getMapper(CatsMapper::class.java).catByID(id)
            if(null == cat) {
                throw IllegalArgumentException("Could not find cat with ${id}!")
            } else {
                return cat
            }
        }
    }

    override val numberOfCats: Long
        get() {
        sessionFactory.openSession().use { session ->
            return session.getMapper(CatsMapper::class.java).numberOfCats
        }
    }

    override suspend fun update(cat: Cat) {
        sessionFactory.openSession().use { session ->
            session.getMapper(CatsMapper::class.java).update(cat)
            session.commit()
        }
    }

    override suspend fun delete(id: Number) {
        sessionFactory.openSession().use { session ->
            session.getMapper(CatsMapper::class.java).delete(id)
            session.commit()
        }
    }

    override suspend fun deleteAll() {
        sessionFactory.openSession().use {
            it.getMapper(CatsMapper::class.java).deleteAll()
            it.commit()
        }
    }
}