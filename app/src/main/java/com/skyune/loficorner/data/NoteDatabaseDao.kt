package com.skyune.loficorner.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.skyune.loficorner.model.Data
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDatabaseDao {

    @Query("SELECT * FROM data")
    fun getAll(): Flow<List<Data>>

    @Query("SELECT * FROM data WHERE id = :id")
    fun getById(id: String): Data

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(data: Data)


}
