package com.skyune.loficorner.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.skyune.loficorner.model.CurrentRoom
import com.skyune.loficorner.model.Data
import com.skyune.loficorner.model.TimePassed
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDatabaseDao {

    @Query("SELECT id,playlist_name,artwork,user FROM data")
    fun getAll(): Flow<List<Data>>

    @Query("SELECT id,playlist_name,artwork,user FROM data WHERE songType='Sleepy'")
    fun getAllSleepy(): Flow<List<Data>>

    @Query("SELECT id,playlist_name,artwork,user FROM data WHERE songType='Jazzy'")
    fun getAllJazzy(): Flow<List<Data>>

    @Query("SELECT * FROM data WHERE id = :id")
    fun getById(id: String): Data

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(data: Data)


    @Query("SELECT COUNT(*) FROM data")
    fun getCount(): Int

    @Query("SELECT * FROM time_passed ORDER BY id DESC LIMIT 1")
    suspend fun getLatestTimePassed(): TimePassed?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(timePassed: TimePassed)


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRoom(room: CurrentRoom)

    @Query("SELECT * FROM current_room")
    fun getCurrentRoom(): LiveData<CurrentRoom>

    @Query("SELECT * FROM time_passed ORDER BY id ASC")
    fun getAllTimePassed(): LiveData<List<TimePassed>>

    @Query("SELECT SUM(time) FROM time_passed")
    fun getTotalTime(): LiveData<Long>

    @Query("SELECT * FROM time_passed WHERE id = :id")
    suspend fun getTimePassedById(id: Int): TimePassed?

    @Update
    suspend fun updateTimePassed(timePassed: TimePassed)

    @Query("DELETE FROM time_passed")
    suspend fun deleteAllTimePassed()
}
