package com.skyune.loficorner.viewmodels

import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.*
import com.skyune.loficorner.AppPreferences
import com.skyune.loficorner.data.DataOrException
import com.skyune.loficorner.exoplayer.MusicServiceConnection
import com.skyune.loficorner.model.CurrentRoom
import com.skyune.loficorner.model.Data
import com.skyune.loficorner.model.Weather
import com.skyune.loficorner.repository.WeatherRepository
import com.skyune.loficorner.utils.playMusicFromId
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@OptIn(InternalCoroutinesApi::class)
@HiltViewModel
class ProfileViewModel @Inject constructor(private val repository: WeatherRepository,
                                           private val preferences: AppPreferences)
    : ViewModel() {
    suspend fun getWeatherData()
            : DataOrException<Weather, Boolean, Exception> {
        return repository.getWeather()

    }
    suspend fun getSongData()
            : DataOrException<Weather, Boolean, Exception> {
        return repository.getWeather()

    }

    suspend fun getPlaylist(id: String) : List<Data>? {
        return repository.getPlaylist(id)

    }



    fun getPlaylistData(id: String) : Call<Weather>? {
        return repository.getPlaylistData(id)

    }

    fun getMovieById(id: String) : Call<Weather>? {
        return repository?.getMovieById(id)

    }



    val allWords : LiveData<List<Data>> = repository.allWords.asLiveData()
    var playlist: MutableList<Data> = mutableListOf<Data>()


    private val _noteList = MutableStateFlow<List<Weather>>(emptyList())

    var noteList = _noteList.asStateFlow()

    fun playPlaylistSongById(
        item: String,
        isPlayerReady: MutableState<Boolean>,
        musicServiceConnection: MusicServiceConnection,
    ) {

        Log.d("play", "playPlaylistSongById: $item")
         viewModelScope.launch {
             val data = repository.getPlaylist(item)


             Log.d("data", "playPlaylistSongById: $data")
             if (isPlayerReady.value) {
                 isPlayerReady.value = false
             }
             if (data != null) {
                 playMusicFromId(
                     musicServiceConnection,
                     data,
                     item,
                     isPlayerReady.value
                 )
             }
             isPlayerReady.value = true
         }


            }


    fun insert(id: Data) = viewModelScope.launch {
        repository.insert(id)
    }

    private val _selectedItemId = mutableStateOf("")

    val selectedItemId: State<String> = preferences.selectedItemId?.let { mutableStateOf(it) }
        ?: _selectedItemId

    fun selectItem(itemId: String) {
        preferences.selectedItemId = itemId // save to SharedPreferences
        _selectedItemId.value = itemId
    }

    var _selectedButtonIndex = mutableStateOf(0)
    val selectedButtonIndexId: State<Int> = preferences.selectedButtonIndexId.let { mutableStateOf(it) }



    fun insertRoom(room: CurrentRoom) =  viewModelScope.launch {
        repository.insertRoom(room)
    }


    fun selectButtonIndex(itemId: Int) {
        preferences.selectedButtonIndexId = itemId // save to SharedPreferences
        _selectedButtonIndex.value = itemId
    }


    var _selectedRoomIndex = mutableStateOf(0)
    val selectedRoomIndexId: State<Int> = preferences.selectedRoomId.let { mutableStateOf(it) }
    fun selectRoomIndex(itemId: Int) {
        preferences.selectedRoomId = itemId // save to SharedPreferences
        _selectedRoomIndex.value = itemId
    }

    fun PlayPlaylist(
        item: Data,
        isPlayerReady: MutableState<Boolean>,
        musicServiceConnection: MusicServiceConnection,
    ) {
        val response: Call<Weather>? =
            getMovieById(item.id)
        response?.enqueue(object : Callback<Weather> {
            override fun onFailure(call: Call<Weather>, t: Throwable) {
                Log.d("onFailure", t.message.toString())
            }

            override fun onResponse(
                call: Call<Weather>,
                response: Response<Weather>
            ) {
                if (isPlayerReady.value) {
                    isPlayerReady.value = false
                }
                playMusicFromId(
                    musicServiceConnection,
                    response.body()!!.data,
                    item.id,
                    isPlayerReady.value
                )
                isPlayerReady.value = true
            }
        })
    }

    fun ShowPlaylistsSongs(
        isLoaded: MutableState<Boolean>

        ) {
                isLoaded.value = false
        for (i in playlistids.indices) {
                    val response: Call<Weather>? =
                        getPlaylistData(playlistids[i])
            response?.enqueue(object : Callback<Weather> {
                override fun onFailure(call: Call<Weather>, t: Throwable) {
                    Log.d("onFailure", t.message.toString())
                    isLoaded.value = false
                }

                override fun onResponse(call: Call<Weather>, response: Response<Weather>) {
                    Log.d("onResponse", response.body().toString())
                    if (response.isSuccessful) {
                        playlist.add(response.body()!!.data[0])
                        Log.d("TAG", "onResponse: ${playlist}")
                        insert(response.body()!!.data[0])

                    }
                }
            })
                    isLoaded.value = true
                }
    }

    val playlistids = listOf("noPJL", "n62mn","ebd1O", "eAlov", "nQR49", "nqbzB", "ezWJp", "lzdql", "XB7R7", "5QaVY", "qE1q2","3AbWv", "AxRP0", "aAw5Q", "Q4wGW", "KK8v2", "RKjdZ","epYaM",
        "LKpEw", "Dv65v", "ebOpP", "ePMJ5", "Ax7ww")
    }



