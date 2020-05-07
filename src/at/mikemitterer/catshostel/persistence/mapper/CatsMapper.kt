package at.mikemitterer.catshostel.persistence.mapper

import at.mikemitterer.catshostel.model.Cat
import org.apache.ibatis.annotations.*

interface CatsMapper {
    @get:Select("SELECT count(*) FROM cats")
    @get:Options(useCache = true)
    val numberOfCats: Long

    @Options(useCache = true)
    @Select("SELECT * FROM cats ORDER BY name")
    fun selectAll(): List<Cat>

    @Options(useCache = true)
    @Select("SELECT * FROM cats WHERE id = #{id}")
    fun catByID(id: Number): Cat?

    // Set keyProperty as the Java variable name and keyColumn as the column name in the database.
    @Options(flushCache = Options.FlushCachePolicy.TRUE, useGeneratedKeys = true, keyProperty = "cat.ID", keyColumn = "id")
    @Insert("INSERT INTO cats ( name, age ) VALUES (#{cat.name}, #{cat.age})")
    fun insert(@Param("cat") cat: Cat)

    @Options(flushCache = Options.FlushCachePolicy.TRUE)
    @Update("UPDATE cats SET name=#{cat.name},age=#{cat.age} WHERE id = #{cat.ID}")
    fun update(@Param("cat") cat: Cat)

    @Options(flushCache = Options.FlushCachePolicy.TRUE)
    @Delete("DELETE FROM cats WHERE id = #{id}")
    fun delete(id: Number)

    @Options(flushCache = Options.FlushCachePolicy.TRUE)
    @Delete("DELETE FROM cats")
    fun deleteAll()
}