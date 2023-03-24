package com.skyune.loficorner.ui.mainScreen

import android.content.Context
import android.content.Intent
import android.media.session.PlaybackState
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.core.content.ContextCompat.startActivity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LiveData
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberImagePainter
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.skyune.loficorner.R
import com.skyune.loficorner.exoplayer.MusicService
import com.skyune.loficorner.exoplayer.MusicServiceConnection
import com.skyune.loficorner.exoplayer.library.extension.*
import com.skyune.loficorner.model.Data
import com.skyune.loficorner.navigation.WeatherNavigation
import com.skyune.loficorner.ui.BottomNavScreen
import com.skyune.loficorner.ui.theme.Theme
import com.skyune.loficorner.viewmodels.MainViewModel
import com.skyune.loficorner.viewmodels.getTimeString
import com.yeocak.parallaximage.GravitySensorDefaulted


@Composable
fun MainScreen(
    navController: NavHostController,
    onToggleTheme: (Theme) -> Unit,
    onToggleDarkMode: () -> Unit,
    musicServiceConnection: MusicServiceConnection,
    gravitySensorDefaulted: GravitySensorDefaulted,
    allWords: LiveData<List<Data>>,
    mainViewModel: MainViewModel = hiltViewModel()
) {



    val list by allWords.observeAsState(listOf())
    val navController = rememberNavController()
    var showDialog by remember { mutableStateOf(false) }
    val topBarState = remember { derivedStateOf {    (mutableStateOf(true)) }.value}
    val bottomBarState = remember { derivedStateOf {    (mutableStateOf(true)) }}

    val shouldHavePlayBar by remember {
        derivedStateOf {
            musicServiceConnection.playbackState.value?.state == PlaybackState.STATE_PLAYING
                    || musicServiceConnection.playbackState.value?.state == PlaybackState.STATE_PAUSED
                    || musicServiceConnection.playbackState.value?.state == PlaybackState.STATE_SKIPPING_TO_NEXT
                    || musicServiceConnection.playbackState.value?.state == PlaybackState.STATE_BUFFERING
                    || musicServiceConnection.currentPlayingSong.value != null
        }
    }

    mainViewModel.deleteAllTimePassed()

    val isTimerRunning: MutableState<Boolean> = remember{
        derivedStateOf {
            mutableStateOf(false)
        }
    }.value


    //for testing
    var showBottomBar = false

    //TODO needs rework on startup
    //for now ill work on something else.
    val isPlayerReady: MutableState<Boolean> = remember{
        derivedStateOf {
            mutableStateOf(false)
        }
    }.value



//    if(list.size>5 && !isPlayerReady.value) {
//        mainViewModel.PlayPlaylist(
//            list.random(),
//            isPlayerReady,
//            musicServiceConnection
//        )
//        isPlayerReady.value = true
//    }else
//    {
//        if(allWords.value?.isEmpty() == true)
//        {
//            showBottomBar = true
//        }
//    }
        //PlayPlaylist()
    val isLoaded = remember { derivedStateOf {  (mutableStateOf(false)) }}
    val myList: MutableList<Data> = mutableListOf<Data>()
    val songIcon by remember { derivedStateOf { musicServiceConnection.currentPlayingSong.value?.displayIconUri} }
    val title by remember {
        derivedStateOf {
            musicServiceConnection.currentPlayingSong.value?.description?.title.toString()
        }
    }
    val artist by remember {
        derivedStateOf {
            musicServiceConnection.currentPlayingSong.value?.description?.subtitle.toString()
        }
    }

    val timePassedList by mainViewModel.getAllTimePassed().observeAsState(listOf())
    val currentRoom by mainViewModel.getCurrentRoom().observeAsState(null)

    Scaffold(
        topBar = {
            if(topBarState.value) {
                TopAppBar(
                    backgroundColor = Color.Transparent,
                    elevation = 0.dp,
                    modifier = Modifier.wrapContentSize(),
                    title = {},
                    actions = {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                Modifier
                                    .weight(1.12f)
                            ) {
                                IconButton(
                                    onClick = { showDialog = !showDialog }
                                ) {
                                    Icon(
                                        Icons.Filled.Search,
                                        contentDescription = "Search",
                                        tint = MaterialTheme.colors.surface
                                    )
                                }
                            }

                            Box(
                                Modifier
                                    .weight(2f)
                                    .padding(top = 0.dp)
                            ) {

                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                ) {
                                    timePassedList.firstOrNull()?.let {
                                        Text(
                                            text = it.taskName,
                                            fontSize = 19.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colors.surface
                                        )
                                    }
                                    timePassedList.firstOrNull()?.let {
                                        Text(
                                            "${getTimeString(it.time)}",
                                            color = MaterialTheme.colors.onSurface
                                        )
                                    }
                                }


                            }

                            val context = LocalContext.current

                            Box(
                                Modifier
                                    .weight(0.5f)
                            ) {
                                IconButton(
                                    onClick = {
                                        val sharingIntent = Intent(Intent.ACTION_SEND)
                                        sharingIntent.type = "text/plain"
                                        sharingIntent.putExtra(
                                            Intent.EXTRA_TEXT,
                                            "Check out this cool website: https://www.example.com"
                                        )
                                        val chooserIntent =
                                            Intent.createChooser(sharingIntent, "Share via")
                                        startActivity(context, chooserIntent, null)
                                    }
                                ) {
                                    Icon(
                                        Icons.Filled.Share,
                                        contentDescription = "Share",
                                        tint = MaterialTheme.colors.surface
                                    )
                                }
                            }
                        }
                    }

                )
            }
        },
        bottomBar = {
            //remove it later (showbottombar)
            //shouldHavePlayBar
            //TODO: change it back to shouldhaveplaybar
            if(true) {
                BottomBar(
                    navController = navController,
                    bottomBarState,
                    musicServiceConnection,
                    songIcon,
                    title,
                    artist
                )
            }
        }
    ) {

            WeatherNavigation(
                modifier = Modifier.padding(it),
                navController = navController,
                onToggleTheme,
                onToggleDarkMode,
                musicServiceConnection,
                gravitySensorDefaulted,
                bottomBarState.value,
                isLoaded.value,
            myList,
                allWords, isTimerRunning,timePassedList,currentRoom,topBarState,title)

    }


}


@Composable
fun BottomBar(
    navController: NavHostController,
    bottomBarState: State<MutableState<Boolean>>,
    musicServiceConnection: MusicServiceConnection,
    songIcon: Uri?,
    title: String,
    artist: String,
) {
    val screens = listOf(
        BottomNavScreen.Home,
        BottomNavScreen.Profile,
        BottomNavScreen.Settings,
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val context = LocalContext.current
    Column(
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
        ) {
            Row() {
                AnimatedVisibility(
                    visible = bottomBarState.value.value && currentDestination?.route != "home" && musicServiceConnection.isConnected.value,
                    enter = slideInVertically(
                        initialOffsetY = { it }, animationSpec = tween(
                            durationMillis = 200,
                            easing = LinearEasing
                        )
                    ),
                    exit = slideOutVertically(
                        targetOffsetY = { it }, animationSpec = tween(
                            durationMillis = 200,
                            easing = LinearEasing
                        )
                    )
                ) {
                    SongColumn(songIcon, title, artist, musicServiceConnection, context)
                }
            }
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom,
            modifier = Modifier
                .zIndex(20f)
        ) {
            AnimatedVisibility(
                visible = bottomBarState.value.value,
                enter = slideInVertically(
                    initialOffsetY = { it }, animationSpec = tween(
                        durationMillis = 200,
                        easing = LinearEasing
                    )
                ),
                exit = slideOutVertically(
                    targetOffsetY = { it }, animationSpec = tween(
                        durationMillis = 200,
                        easing = LinearEasing
                    )
                )
            ) {
                Box(
                    modifier = Modifier
                        .zIndex(22f)
                        .clip(shape = RoundedCornerShape(20.dp, 20.dp, 0.dp, 0.dp))
                        .background(
                            Brush.linearGradient(
                                0f to MaterialTheme.colors.primary,
                                1f to MaterialTheme.colors.primaryVariant,
                                start = Offset(0f, -20f),
                                end = Offset(0f, 100f)
                            )
                        )
                ) {
                    BottomNavigation(
                        modifier = Modifier
                            .clip(shape = RoundedCornerShape(15.dp, 15.dp, 0.dp, 0.dp)),
                        backgroundColor = Color.Transparent,
                        elevation = 0.dp
                    ) {
                        screens.forEach { screen ->
                            AddItem(
                                screen = screen,
                                currentDestination = currentDestination,
                                navController = navController,
                                bottomBarState = bottomBarState.value,
                            )
                        }
                    }
                }
            }
        }
    }
}


@Composable
private fun SongColumn(
    songIcon: Uri?,
    title: String,
    artist: String,
    musicServiceConnection: MusicServiceConnection,
    context: Context
) {
    val imageRequest = remember(songIcon) {
        ImageRequest.Builder(context)
            .diskCachePolicy(CachePolicy.DISABLED)
            .data(data = songIcon)
            .build()
    }

    val isPlaying = musicServiceConnection.playbackState.value?.state == PlaybackState.STATE_PLAYING ||
            musicServiceConnection.playbackState.value?.state == PlaybackState.STATE_BUFFERING


    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom,
        modifier = Modifier
            .height(70.dp)
            .wrapContentHeight()
            .fillMaxWidth()
            .zIndex(1f)
            .offset(0.dp, 16.dp)
            .clip(shape = RoundedCornerShape(10.dp, 10.dp, 0.dp, 0.dp))
            .background(
                brush = Brush.linearGradient(
                    0f to Color(MaterialTheme.colors.onPrimary.value),
                    1f to Color(MaterialTheme.colors.onSecondary.value),
                    start = Offset(0f, 0f),
                    end = Offset(0f, 200f)
                )
            )
    ) {


        Column(Modifier.padding(0.dp, 0.dp, 0.dp, 16.dp)) {
            Row(
                modifier = Modifier,
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val painter = rememberImagePainter(
                    imageRequest,

                    builder = {
                        crossfade(true)
                            .data(songIcon)
                    }
                )
                Image(
                    painter = painter,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.2f),

                    contentDescription = null,

                )
                Column(modifier = Modifier
                    .weight(1f)
                    .padding(start = 12.dp)) {
                    Text(text = title,color = MaterialTheme.colors.onSurface)
                    Text(artist,color = MaterialTheme.colors.onSurface)
                }
                OutlinedButton(
                    shape = CircleShape,
                    border = BorderStroke(1.dp, Color.Transparent),
                    contentPadding = PaddingValues(0.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent),
                    modifier = Modifier
                        .size(40.dp)
                        .weight(0.4f)
                    ,
                    onClick = {
                        if (isPlaying) musicServiceConnection.transportControls?.pause()
                        else musicServiceConnection.transportControls?.play()
                    }
                ) {
                    Icon(
                        painter = painterResource(id = if (isPlaying) R.drawable.exo_icon_pause else R.drawable.exo_icon_play),
                        tint = Color.White,
                        modifier = Modifier,
                        contentDescription = null
                    )
                }
            }



                LinearProgressIndicator(
                    progress = musicServiceConnection.songDuration.value / MusicService.curSongDuration.toFloat(),
                    Modifier
                        .align(Alignment.CenterHorizontally)
                        .fillMaxWidth(0.9f)
                        .height(3f.dp)
                        .graphicsLayer {
                        }
                )
            }

    }

}




@Composable
fun RowScope.AddItem(
    screen: BottomNavScreen,
    currentDestination: NavDestination?,
    navController: NavHostController,
    bottomBarState: MutableState<Boolean>,
) {
    BottomNavigationItem(
        label = {
            Text(text = screen.title, color = MaterialTheme.colors.onSurface)
        },
        icon = {
            Icon(
                imageVector = screen.icon,
                contentDescription = "Navigation Icon",
                tint = MaterialTheme.colors.surface
            )
        },
        selected = currentDestination?.hierarchy?.any {
            it.route == screen.route
        } == true,
        unselectedContentColor = LocalContentColor.current.copy(alpha = ContentAlpha.disabled),
        onClick = {
            bottomBarState.value = true
            navController.navigate(screen.route) {
                popUpTo(navController.graph.findStartDestination().id)
                launchSingleTop = true
            }
        }
    )
}

