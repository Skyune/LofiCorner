package com.skyune.loficorner.viewmodels

import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.*
import com.skyune.loficorner.AppPreferences
import com.skyune.loficorner.data.DataOrException
import com.skyune.loficorner.exoplayer.MusicServiceConnection
import com.skyune.loficorner.exoplayer.library.extension.id
import com.skyune.loficorner.model.CurrentRoom
import com.skyune.loficorner.model.Data
import com.skyune.loficorner.model.Weather
import com.skyune.loficorner.repository.WeatherRepository
import com.skyune.loficorner.utils.playMusicFromId
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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


    var isSelected = mutableStateOf(false)
    var isLoading = mutableStateOf(true)

    val allWords : LiveData<List<Data>> = repository.allWords.asLiveData()
    val allSleepy : LiveData<List<Data>> = repository.allSleepy.asLiveData()
    val allJazzy : LiveData<List<Data>> = repository.allJazzy.asLiveData()

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

    var currentPlaylistSelected = mutableStateOf("")


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

    private val _scrollOffset = MutableStateFlow(0)
    val scrollOffset: StateFlow<Int> = _scrollOffset.asStateFlow()

    fun saveScrollOffset(offset: Int) {
        _scrollOffset.value = offset
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
                val songList = response.body()?.data ?: emptyList()
                viewModelScope.launch {
                    isLoading.value = true
                    delay(200)

                    val isSongIdMatching = songList.any {
                        Log.d("TAG", "it.id: ${it.title}")
                        it.title == musicServiceConnection.currentPlayingSong.value?.description?.title.toString()
                    }
                    val matchingSong = songList.find {
                        it.id == musicServiceConnection.currentPlayingSong.value?.id
                    }
                    if (matchingSong != null) {
                        currentPlaylistSelected.value = item.id
                    }
                    isLoading.value = false
                    isSelected.value = isSongIdMatching
                }



                Log.d("TAG", "item.id: ${ item.title}")
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
        isLoaded: MutableState<Boolean>,
        songType: String

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
                    if (response.isSuccessful) {
                        val firstItem = response.body()!!.data.first() // Get the first element of the list
                        val newItem = Data(
                            artwork = firstItem.artwork,
                            description = firstItem.description,
                            playlist_name = firstItem.playlist_name,
                            duration = firstItem.duration,
                            id = firstItem.id,
                            mood = firstItem.mood,
                            title = firstItem.title,
                            user = firstItem.user,
                            songType = songType// Set the songType field to "jazz"
                        )
                        insert(newItem) // Insert the new item into your data structure
                        Log.d("TAG", "onResponse: ${newItem}")
                    }
                }
            })
            isLoaded.value = true
        }
    }





    val playlistids = listOf("noPJL", "n62mn","ebd1O", "eAlov", "nQR49", "nqbzB", "ezWJp", "lzdql", "XB7R7", "5QaVY", "qE1q2","3AbWv", "AxRP0", "aAw5Q", "Q4wGW", "KK8v2", "RKjdZ","epYaM",
        "LKpEw", "Dv65v", "ebOpP", "ePMJ5", "Ax7ww")
    }



