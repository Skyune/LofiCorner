package com.skyune.loficorner.viewmodels

import androidx.lifecycle.LiveData
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
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(InternalCoroutinesApi::class)
@HiltViewModel
class SettingsViewModel @Inject constructor(private val repository: WeatherRepository)
    : ViewModel() {
    suspend fun getWeatherData()
            : DataOrException<Weather, Boolean, Exception> {
        return repository.getWeather()

    }
    suspend fun getSongData()
            : DataOrException<Weather, Boolean, Exception> {
        return repository.getWeather()

    }

    fun getAllTimePassed(): LiveData<List<TimePassed>> {
        return repository.getAllTimePassed()
    }

    private val _noteList = MutableStateFlow<List<CurrentSong>>(emptyList())
    var noteList = _noteList.asStateFlow()

    init {
//        viewModelScope.launch(Dispatchers.IO) {
//            repository.getAllNotes().distinctUntilChanged()
//                .collect() {
//                    _noteList.value = it
//                }
//        }
        //noteList.addAll(NotesDataSource().loadNotes())
    }




    //fun addNote(song: CurrentSong) = viewModelScope.launch { repository.addNote(song) }
    //fun getNotes() = viewModelScope.launch {  repository.getLatestNote() }

}



