package com.skyune.loficorner.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skyune.loficorner.data.DataOrException
import com.skyune.loficorner.model.CurrentSong
import com.skyune.loficorner.model.TimePassed
import com.skyune.loficorner.model.Weather
import com.skyune.loficorner.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import retrofit2.Call
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: WeatherRepository)
    : ViewModel(){
        suspend fun getWeatherData()
        : DataOrException<Weather, Boolean, Exception> {
            return repository.getWeather()

        }

    private val _currentSong = MutableStateFlow<List<CurrentSong>>(emptyList())
    val currentSongList = _currentSong.asStateFlow()

     fun insert(timePassed: TimePassed) =  viewModelScope.launch {
         repository.insertTime(timePassed)
    }

    suspend fun getLatestTimePassed(): TimePassed? = repository.getLatestTimePassed()

    private val currentTaskName = MutableLiveData<String>()

    suspend fun insertTimePassed(timePassed: TimePassed) {
        val taskName = currentTaskName.value ?: return
        val timePassedWithTaskName = timePassed.copy(taskName = taskName)
        repository.insertTimePassed(timePassedWithTaskName)
    }

    fun update(timePassed: TimePassed) = viewModelScope.launch {
        repository.update(timePassed)
    }
}