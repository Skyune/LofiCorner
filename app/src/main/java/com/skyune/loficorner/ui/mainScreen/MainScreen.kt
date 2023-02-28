package com.skyune.loficorner.ui.mainScreen

import android.annotation.SuppressLint
import android.content.Context
import android.media.session.PlaybackState
import android.net.Uri
import android.os.CountDownTimer
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.zIndex
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
import com.skyune.loficorner.exoplayer.isPlaying
import com.skyune.loficorner.exoplayer.library.extension.*
import com.skyune.loficorner.model.Data
import com.skyune.loficorner.navigation.WeatherNavigation
import com.skyune.loficorner.ui.BottomNavScreen
import com.skyune.loficorner.ui.theme.Theme
import com.skyune.loficorner.utils.playMusic
import com.skyune.loficorner.utils.playMusicFromId
import com.skyune.loficorner.viewmodels.MainViewModel
import com.skyune.loficorner.viewmodels.ProfileViewModel
import com.yeocak.parallaximage.GravitySensorDefaulted
import kotlinx.coroutines.*


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
    if (showDialog) {
        Popup(onDismiss = { showDialog = false })
    }

    //for testing
    var showBottomBar = false

    //TODO needs rework on startup
    //for now ill work on something else.
    val isPlayerReady: MutableState<Boolean> = remember{
        derivedStateOf {
            mutableStateOf(false)
        }
    }.value

    if(list.size>5 && !isPlayerReady.value) {
        playMusicFromId(
            musicServiceConnection,
            list,
            list[0].id,
            isPlayerReady.value
        )
        isPlayerReady.value = true
    }else
    {
        if(allWords.value?.isEmpty() == true)
        {
            showBottomBar = true
        }
    }
        //PlayPlaylist()
    val isLoaded = remember { derivedStateOf {  (mutableStateOf(false)) }}
    var myList: MutableList<Data> = mutableListOf<Data>()
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


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "My App Title") },
                actions = {
                    IconButton(onClick = { showDialog = !showDialog  }) {
                        Icon(Icons.Filled.Search, contentDescription = "Search")
                    }
                    IconButton(onClick = { /* Handle action */ }) {
                        Icon(Icons.Filled.Settings, contentDescription = "Settings")
                    }
                }
            )
        },
        bottomBar = {
            //remove it later (showbottombar)
            if(shouldHavePlayBar || showBottomBar) {
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
                navController = navController,
                onToggleTheme,
                onToggleDarkMode,
                musicServiceConnection,
                gravitySensorDefaulted,
                bottomBarState.value,
                isLoaded.value,
            myList,
                allWords)

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


enum class PomodoroSession {
    WORK,
    SHORT_BREAK,
    LONG_BREAK
}

@Composable
fun Popup(onDismiss: () -> Unit) {
    var isTimerRunning by remember { mutableStateOf(false) }
    var timeLeft by remember { mutableStateOf(0L) }
    var currentSession by remember { mutableStateOf(PomodoroSession.WORK) }
    var timePaused by remember { mutableStateOf(0L) }

    var pomodorosCompleted = 0
    val WORK_DURATION_MINUTES = 10
    val SHORT_BREAK_DURATION_MINUTES = 5
    val LONG_BREAK_DURATION_MINUTES = 15
    val POMODORO_SESSIONS_BEFORE_LONG_BREAK = 2


    // should be 60_000L
    val ONE_MINUTE_MILLIS = 1000L

    var countDownTimer: CountDownTimer? = null

    fun startTimer() {
        val durationInMillis = when (currentSession) {
            PomodoroSession.WORK -> WORK_DURATION_MINUTES * ONE_MINUTE_MILLIS
            PomodoroSession.SHORT_BREAK -> SHORT_BREAK_DURATION_MINUTES * ONE_MINUTE_MILLIS
            PomodoroSession.LONG_BREAK -> LONG_BREAK_DURATION_MINUTES * ONE_MINUTE_MILLIS
        } - timePaused // subtract paused time from duration

        countDownTimer = object : CountDownTimer(durationInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeft = millisUntilFinished
            }

            override fun onFinish() {
                when (currentSession) {
                    PomodoroSession.WORK -> {
                        currentSession = PomodoroSession.SHORT_BREAK
                        pomodorosCompleted++
                        if (pomodorosCompleted >= POMODORO_SESSIONS_BEFORE_LONG_BREAK) {
                            currentSession = PomodoroSession.LONG_BREAK
                            pomodorosCompleted = 0
                        }
                        timeLeft = 0L
                        startTimer()
                    }
                    PomodoroSession.SHORT_BREAK -> {
                        timeLeft = 0L
                        startTimer()
                    }
                    PomodoroSession.LONG_BREAK -> {
                        currentSession = PomodoroSession.WORK
                        timeLeft = 0L
                        startTimer()
                    }
                }
                // TODO: show a notification or play a sound to indicate the end of the timer
            }
        }.start()
        isTimerRunning = true

    }

    fun pauseTimer() {
        countDownTimer?.cancel()
        isTimerRunning = false
        timePaused += (when (currentSession) {
            PomodoroSession.WORK -> WORK_DURATION_MINUTES * ONE_MINUTE_MILLIS
            PomodoroSession.SHORT_BREAK -> SHORT_BREAK_DURATION_MINUTES * ONE_MINUTE_MILLIS
            PomodoroSession.LONG_BREAK -> LONG_BREAK_DURATION_MINUTES * ONE_MINUTE_MILLIS
        } - timeLeft)
    }

    fun resetTimer() {
        countDownTimer?.cancel()
        isTimerRunning = false
        timeLeft = 0L
        timePaused = 0L
        currentSession = PomodoroSession.WORK
    }


    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable(onClick = onDismiss),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .background(Color.White)
                    .padding(16.dp)
                    .size(width = 280.dp, height = 400.dp),
                contentAlignment = Alignment.Center
            ) {
                Column {
                    Text(
                        text = "Pomodoro Timer",
                        fontSize = 24.sp,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    when (currentSession) {
                        PomodoroSession.WORK -> {
                            Text(
                                text = "Work Session",
                                fontSize = 18.sp,

                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                        }
                        PomodoroSession.SHORT_BREAK -> {
                            Text(
                                text = "Short Break",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                        }
                        PomodoroSession.LONG_BREAK -> {
                            Text(
                                text = "Long Break",
                                fontSize = 18.sp,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                        }
                    }

                    Text(
                        text = getTimeString(timeLeft),
                        fontSize = 48.sp,

                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(
                            onClick = { if (isTimerRunning) pauseTimer() else startTimer() },
                            modifier = Modifier.width(120.dp)
                        ) {
                            Text(if (isTimerRunning) "Pause" else "Start")
                        }
                        Button(
                            onClick = { resetTimer() },
                            modifier = Modifier.width(120.dp)
                        ) {
                            Text("Reset")
                        }
                    }
                }
            }
        }
    }
}
@SuppressLint("DefaultLocale")
fun getTimeString(timeMillis: Long): String {
    val seconds = timeMillis / 1000 % 60
    val minutes = timeMillis / (1000 * 60) % 60
    return String.format("%02d:%02d", minutes, seconds)
}
/*
@Composable
fun Popup(onDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        var timeLeft by remember { mutableStateOf(0L) }
        var timerJob: Job? = null

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.6f))
                .clickable(onClick = onDismiss),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .background(Color.White)
                    .padding(16.dp)
                    .size(width = 280.dp, height = 400.dp),
                contentAlignment = Alignment.Center
            ) {
                Column {
                    Text(
                        text = "Popup Title",
                        fontSize = 20.sp,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    Button(
                        onClick = {
                            timerJob?.cancel()
                            timerJob = CoroutineScope(Dispatchers.Main).launch {
                                timeLeft = 25 * 60 // 25 minutes
                                while (timeLeft > 0) {
                                    delay(1000)
                                    timeLeft -= 1
                                }
                                // Timer finished, do something here
                            }
                        }
                    ) {
                        Text(text = "Start Pomodoro Timer")
                    }
                    Text(
                        text = "Time left: ${timeLeft / 60}:${timeLeft % 60}",
                        fontSize = 16.sp,
                        modifier = Modifier.padding(vertical = 16.dp)
                    )
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items((0..20).toList()) { index ->
                            Text(text = "Item $index", fontSize = 16.sp)
                        }
                    }
                }
            }
        }
    }
}

 */

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
                modifier = Modifier
                ,
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
                    modifier = Modifier.size(50.dp)
                    ,
                    contentScale = ContentScale.FillBounds,
                    contentDescription = null
                )
                Column {
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
                        .weight(0.2f)
                    ,
                    onClick = {
                        if (isPlaying) musicServiceConnection.transportControls?.pause()
                        else musicServiceConnection.transportControls?.play()
                    }
                ) {
                    Icon(

                        painter = painterResource(id = if (isPlaying) R.drawable.exo_icon_pause else R.drawable.exo_icon_play),
                        tint = Color.White,
                        modifier = Modifier
                        ,
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

