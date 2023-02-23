package com.skyune.loficorner

import android.app.Application
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.skyune.loficorner.exoplayer.MusicServiceConnection
import com.skyune.loficorner.ui.theme.Theme
import com.yeocak.parallaximage.GravitySensorDefaulted
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class WeatherApplication : Application() {

    @Inject
    lateinit var musicServiceConnection: MusicServiceConnection

    @Inject lateinit var preferences: AppPreferences


    public val _currentTheme = mutableStateOf(Theme.Jazz)
    val currentTheme: State<Theme> by lazy {
        preferences.selectedTheme.let{ mutableStateOf(it) }
    }


    fun changeTheme(theme: Theme) {
        Log.d("TAG", "changeTheme: ${currentTheme.value}")
        preferences.selectedTheme = theme // save to SharedPreferences
        currentTheme.value
        _currentTheme.value = theme
    }


    //should be saved in cache or datastore
    private val isDark = mutableStateOf(false)

    fun toggleLightTheme() {
        isDark.value = !isDark.value
    }

}