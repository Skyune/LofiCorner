package com.skyune.loficorner

import android.app.Application
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.skyune.loficorner.exoplayer.MusicServiceConnection
import com.skyune.loficorner.ui.theme.Theme
import com.yeocak.parallaximage.GravitySensorDefaulted
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class WeatherApplication: Application() {

    @Inject
    lateinit var musicServiceConnection: MusicServiceConnection

    lateinit var gravitySensorDefaulted: GravitySensorDefaulted

    private val _currentTheme = mutableStateOf(Theme.Jazz)
    val currentTheme: State<Theme> = _currentTheme

    fun changeTheme(theme: Theme) {
        _currentTheme.value = theme
    }


    //should be saved in cache or datastore
    private val isDark = mutableStateOf(false)

    fun toggleLightTheme() {
        isDark.value = !isDark.value
    }

}