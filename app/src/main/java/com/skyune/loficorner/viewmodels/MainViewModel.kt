package com.skyune.loficorner.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skyune.loficorner.AppPreferences
import com.skyune.loficorner.data.DataOrException
import com.skyune.loficorner.model.CurrentRoom
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
class MainViewModel @Inject constructor(private val repository: WeatherRepository, private val appPreferences: AppPreferences)
    : ViewModel(){
        suspend fun getWeatherData()
        : DataOrException<Weather, Boolean, Exception> {
            return repository.getWeather()

        }

    //val currentPomodoroDuration: MutableState<Int> = appPreferences.TimeLeft.let { mutableStateOf(it) }


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

    fun getCurrentRoom(): LiveData<CurrentRoom> {
        return repository.getCurrentRoom()
    }

    fun update(timePassed: TimePassed) = viewModelScope.launch {
        repository.update(timePassed)
    }

    fun getAllTimePassed(): LiveData<List<TimePassed>> {
        return repository.getAllTimePassed()
    }
}