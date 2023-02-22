package com.skyune.loficorner.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.skyune.loficorner.model.Data
import com.skyune.loficorner.model.Weather
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDatabaseDao {

    @Query("SELECT id,playlist_name,artwork,user FROM data")
    fun getAll(): Flow<List<Data>>

    @Query("SELECT * FROM data WHERE id = :id")
    fun getById(id: String): Data

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(data: Data)


    @Query("SELECT COUNT(*) FROM data")
    fun getCount(): Int

}
