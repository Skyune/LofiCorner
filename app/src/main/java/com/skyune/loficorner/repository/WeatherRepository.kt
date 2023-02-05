package com.skyune.loficorner.repository

import android.util.Log
import com.skyune.loficorner.data.DataOrException
import com.skyune.loficorner.model.Weather
import com.skyune.loficorner.network.WeatherApi
import com.skyune.loficorner.data.NoteDatabaseDao
import com.skyune.loficorner.model.Data
import kotlinx.coroutines.flow.Flow
import retrofit2.Call
import javax.inject.Inject

class WeatherRepository @Inject constructor(private val api: WeatherApi, private val noteDatabaseDao: NoteDatabaseDao) {

    suspend fun getWeather()
    :DataOrException<Weather, Boolean, Exception>  {
        val response = try {
            api.getWeather()

        }catch (e: Exception){
            Log.d("REX", "getWeather: $e")
            return DataOrException(e = e)
        }
        Log.d("INSIDE", "getWeather: $response")
        return  DataOrException(data = response)

    }

    val allWords: Flow<List<Data>> = noteDatabaseDao.getAll()


    suspend fun count(): Int {
        return noteDatabaseDao.getCount()
    }

    suspend fun insert(id: Data) {
        noteDatabaseDao.insert(id)
    }

    fun getMovieById(id: String): Call<Weather> {
        return api.getMovieById(id)
    }

     suspend fun getPlaylist(id: String): List<Data> {
        return api.getPlaylist(id)
    }


     fun getPlaylistData(id: String): Call<Weather> {
        return api.getPlaylistData(id)
    }


}