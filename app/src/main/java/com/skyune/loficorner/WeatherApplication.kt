package com.skyune.loficorner

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import com.skyune.loficorner.exoplayer.MusicServiceConnection
import com.skyune.loficorner.model.Data
import com.skyune.loficorner.ui.theme.Theme
import com.yeocak.parallaximage.GravitySensorDefaulted
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class WeatherApplication: Application() {

    @Inject
    lateinit var musicServiceConnection: MusicServiceConnection

    lateinit var gravitySensorDefaulted: GravitySensorDefaulted



    //should be saved in cache or datastore
    private val isDark = mutableStateOf(false)
    public val currentTheme = mutableStateOf(Theme.Light)

    fun toggleLightTheme() {
        isDark.value = !isDark.value
    }

    fun changeTheme(currentTheme: Theme) {
        this.currentTheme.value = currentTheme
    }



    //multiple themes


//    fun toggleLightTheme() {
//        currentRoom = roomTitle
//    }
}