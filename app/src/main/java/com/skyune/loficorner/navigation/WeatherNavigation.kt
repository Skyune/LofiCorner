package com.skyune.loficorner.navigation

import android.util.Log
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LiveData
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.skyune.loficorner.exoplayer.MusicServiceConnection
import com.skyune.loficorner.model.CurrentRoom
import com.skyune.loficorner.model.Data
import com.skyune.loficorner.model.TimePassed
import com.skyune.loficorner.ui.*
import com.skyune.loficorner.ui.homeScreen.HomeScreen
import com.skyune.loficorner.ui.mainScreen.MainScreen
import com.skyune.loficorner.viewmodels.MainViewModel
import com.skyune.loficorner.ui.profileScreen.ProfileScreen
import com.skyune.loficorner.ui.settingsScreen.SettingsScreen
import com.skyune.loficorner.ui.splash.WeatherSplashScreen
import com.skyune.loficorner.ui.theme.Theme
import com.skyune.loficorner.viewmodels.HomeViewModel
import com.skyune.loficorner.viewmodels.ProfileViewModel
import com.skyune.loficorner.viewmodels.SettingsViewModel
import com.yeocak.parallaximage.GravitySensorDefaulted

@Composable
fun WeatherNavigation(
    modifier: Modifier,
    navController: NavHostController,
    onToggleTheme: (Theme) -> Unit,
    onToggleDarkMode: () -> Unit,
    musicServiceConnection: MusicServiceConnection,
    gravitySensorDefaulted: GravitySensorDefaulted,
    bottomBarState: MutableState<Boolean>,
    isLoaded: MutableState<Boolean>,
    myList: MutableList<Data>,
    allWords: LiveData<List<Data>>,
    isTimerRunning: MutableState<Boolean>,
    timePassedList: List<TimePassed>,
    currentRoom: CurrentRoom?,
    topBarState: MutableState<Boolean>,
    title: String
) {

    //not sure gdzie powinna byc deklaracja settingsviewmodel

    val listState = rememberLazyListState()


    NavHost(navController = navController,
        startDestination = BottomNavScreen.Home.route ) {
        composable(WeatherScreens.SplashScreen.name){
            WeatherSplashScreen(navController = navController)
        }

        composable(WeatherScreens.MainScreen.name){
            val mainViewModel = hiltViewModel<MainViewModel>()
            MainScreen(
                navController,
                onToggleTheme,
                onToggleDarkMode,
                musicServiceConnection,
                gravitySensorDefaulted,
                allWords,

            )
        }
        composable(route = BottomNavScreen.Home.route) {
            val homeViewModel : HomeViewModel = hiltViewModel()
            HomeScreen(modifier,musicServiceConnection = musicServiceConnection, homeViewModel, currentRoom)
        }
        composable(route = BottomNavScreen.Profile.route) {
            val profileViewModel : ProfileViewModel = hiltViewModel()
            val list by profileViewModel.allWords.observeAsState(listOf())
            val Sleepylist by profileViewModel.allSleepy.observeAsState(listOf())
            val Jazzylist by profileViewModel.allJazzy.observeAsState(listOf())
            ProfileScreen(
                profileViewModel = profileViewModel,
                musicServiceConnection,
                bottomBarState,
                isLoaded,
                onToggleTheme,
                topBarState,
                title,
                listState,
                list,
                Sleepylist,
                Jazzylist
            )
        }
        composable(route = BottomNavScreen.Settings.route) {
            val settingsViewModel : SettingsViewModel = hiltViewModel()

            SettingsScreen(
                settingsViewModel = settingsViewModel)
        }

    }
}