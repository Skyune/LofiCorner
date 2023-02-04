package com.skyune.loficorner

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.skyune.loficorner.exoplayer.MusicServiceConnection
import com.skyune.loficorner.ui.mainScreen.MainScreen
import com.skyune.loficorner.ui.theme.AppTheme
import com.skyune.loficorner.ui.theme.Theme
import com.yeocak.parallaximage.GravitySensorDefaulted
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var gravitySensorDefaulted: GravitySensorDefaulted




    @Inject
    lateinit var application: WeatherApplication

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        gravitySensorDefaulted = GravitySensorDefaulted(this)

        val musicServiceConnection = (application as WeatherApplication).musicServiceConnection


        setContent {
            AppTheme(theme = application.currentTheme.value) {
                WeatherApp(onToggleTheme = { application.changeTheme(Theme.Light) },
                    onToggleDarkMode = { application.changeTheme(Theme.Dark) },
                    musicServiceConnection = musicServiceConnection,
                    gravitySensorDefaulted = gravitySensorDefaulted,
                )
            }
        }
    }
}


//this is probably a bad way to implement the theming, going to improve on this later.
@Composable
fun WeatherApp(

    onToggleTheme: () -> Unit,
    onToggleDarkMode: () -> Unit,
    musicServiceConnection: MusicServiceConnection,
    gravitySensorDefaulted: GravitySensorDefaulted
) {


               Column(verticalArrangement = Arrangement.Center,
                     horizontalAlignment = Alignment.CenterHorizontally) {
                   val navController = rememberNavController()

                   MainScreen(navController = navController, onToggleTheme = onToggleTheme, onToggleDarkMode = onToggleDarkMode, musicServiceConnection = musicServiceConnection,gravitySensorDefaulted = gravitySensorDefaulted)
               }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    AppTheme(theme = Theme.Dark) {

    }
}

