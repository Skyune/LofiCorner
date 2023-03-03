package com.skyune.loficorner.repository

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.skyune.loficorner.data.DataOrException
import com.skyune.loficorner.model.Weather
import com.skyune.loficorner.network.WeatherApi
import com.skyune.loficorner.data.NoteDatabaseDao
import com.skyune.loficorner.model.CurrentRoom
import com.skyune.loficorner.model.Data
import com.skyune.loficorner.model.TimePassed
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import retrofit2.Call
import javax.inject.Inject

class WeatherRepository @Inject constructor(private val api: WeatherApi?, private val noteDatabaseDao: NoteDatabaseDao) {

    suspend fun getWeather()
    :DataOrException<Weather, Boolean, Exception>  {
        val response = try {
            api?.getWeather()

        }catch (e: Exception){
            Log.d("REX", "getWeather: $e")
            return DataOrException(e = e)
        }
        Log.d("INSIDE", "getWeather: $response")
        return  DataOrException(data = response)

    }

    val allWords: Flow<List<Data>> = noteDatabaseDao.getAll()




    suspend fun insertTimePassed(timePassed: TimePassed) {
        withContext(Dispatchers.IO) {
            noteDatabaseDao.insert(timePassed)
        }
    }

    suspend fun count(): Int {
        return noteDatabaseDao.getCount()
    }

    suspend fun insert(id: Data) {
        noteDatabaseDao.insert(id)
    }

    fun getMovieById(id: String): Call<Weather>? {
        return api?.getMovieById(id)
    }

     suspend fun getPlaylist(id: String): List<Data>? {
        return api?.getPlaylist(id)
    }


     fun getPlaylistData(id: String): Call<Weather>? {
        return api?.getPlaylistData(id)
    }

    suspend fun insertTime(timePassed: TimePassed) {
        noteDatabaseDao.insert(timePassed)
    }

    suspend fun insertRoom(room: CurrentRoom) {
        noteDatabaseDao.insertRoom(room)
    }

    fun getCurrentRoom(): LiveData<CurrentRoom> {
        return noteDatabaseDao.getCurrentRoom()
    }

    fun getAllTimePassed(): LiveData<List<TimePassed>> {
        return noteDatabaseDao.getAllTimePassed()
    }

    suspend fun getLatestTimePassed(): TimePassed? {
        return withContext(Dispatchers.IO) {
            noteDatabaseDao.getLatestTimePassed()
        }
    }
    suspend fun update(timePassed: TimePassed) {
        noteDatabaseDao.updateTimePassed(timePassed)
    }

}