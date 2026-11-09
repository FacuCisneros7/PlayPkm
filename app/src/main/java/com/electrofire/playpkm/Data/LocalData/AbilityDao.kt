package com.electrofire.playpkm.Data.LocalData

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface AbilityDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(abilities: List<AbilityEntity>)

    @Query("DELETE FROM abilities")
    suspend fun deleteAll()

    @Query("SELECT * FROM abilities WHERE LOWER(nombre) LIKE LOWER(:query) || '%' ORDER BY nombre ASC")
    suspend fun searchAbility(query: String): List<AbilityEntity>

    @Query("SELECT COUNT(*) FROM abilities")
    suspend fun count(): Int

}
