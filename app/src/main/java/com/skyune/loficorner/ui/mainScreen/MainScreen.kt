package com.skyune.loficorner.ui.mainScreen

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
import androidx.compose.runtime.*
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.skyune.loficorner.R
import com.skyune.loficorner.exoplayer.MusicService
import com.skyune.loficorner.exoplayer.MusicServiceConnection
import com.skyune.loficorner.exoplayer.library.extension.*
import com.skyune.loficorner.model.Data
import com.skyune.loficorner.navigation.WeatherNavigation
import com.skyune.loficorner.ui.BottomNavScreen
import com.yeocak.parallaximage.GravitySensorDefaulted


@Composable
fun MainScreen(
    navController: NavHostController,
    onToggleTheme: () -> Unit,
    onToggleDarkMode: () -> Unit,
    musicServiceConnection: MusicServiceConnection,
    gravitySensorDefaulted: GravitySensorDefaulted
) {

    val navController = rememberNavController()
    val bottomBarState = remember { derivedStateOf {    (mutableStateOf(true)) }}
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

    //Scaffold from Accompanist, initialized in build.gradle. (for hide bottom bar support)
    Scaffold(
        bottomBar = { BottomBar(navController = navController, bottomBarState,musicServiceConnection, songIcon,title,artist)  }
    ) {

            WeatherNavigation(navController = navController,
                onToggleTheme,
                onToggleDarkMode,
                musicServiceConnection,
                gravitySensorDefaulted,
                bottomBarState.value,
                isLoaded.value,
            myList)

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
                            durationMillis = 150,
                            easing = LinearEasing
                        )
                    ),
                    exit = slideOutVertically(
                        targetOffsetY = { it }, animationSpec = tween(
                            durationMillis = 150,
                            easing = LinearEasing
                        )
                    ),
                    content = {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Bottom,

                            modifier = Modifier
                                .wrapContentHeight()
                                .fillMaxWidth()
                                .zIndex(1f)
                                .offset(0.dp, 16.dp)
                                .clip(shape = RoundedCornerShape(15.dp, 15.dp, 0.dp, 0.dp))
                                .background(
                                    brush = Brush.linearGradient(
                                        0f to Color(0xffFEC5F3),
                                        1f to Color(0xffBB70C8),
                                        start = Offset(0f, 0f),
                                        end = Offset(0f, 202f)
                                    )
                                )) {

                            Column(Modifier.padding(0.dp,0.dp,0.dp,16.dp)) {
                               Row(modifier = Modifier, horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                                   Image(
                                       rememberAsyncImagePainter(
                                           ImageRequest.Builder(LocalContext.current)
                                               .diskCachePolicy(CachePolicy.DISABLED)
                                               .data(data = songIcon)
                                               .build()
                                       ),
                                       modifier = Modifier.size(50.dp),
                                       contentScale = ContentScale.FillBounds,
                                       contentDescription = null
                                   )
                                   Column {
                                       Text(text = title)
                                       Text(artist)
                                   }
                                   if (musicServiceConnection.playbackState.value?.state != PlaybackState.STATE_PLAYING &&
                                       musicServiceConnection.playbackState.value?.state != PlaybackState.STATE_BUFFERING
                                   ) {
                                       OutlinedButton(
                                           shape = CircleShape,
                                           border = BorderStroke(1.dp, Color.Transparent),
                                           contentPadding = PaddingValues(0.dp),
                                           colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent),
                                           modifier = Modifier
                                               .size(40.dp)
                                               .weight(0.2f),
                                           onClick = { musicServiceConnection.transportControls.play() }) {
                                           Icon(
                                               painter = painterResource(id = R.drawable.exo_icon_play),
                                               tint = Color.White,
                                               contentDescription = null
                                           )
                                       }
                                   } else {
                                       OutlinedButton(
                                           shape = CircleShape,
                                           border = BorderStroke(1.dp, MaterialTheme.colors.onSurface),
                                           contentPadding = PaddingValues(0.dp),
                                           colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colors.onSurface),
                                           modifier = Modifier
                                               .size(40.dp)
                                               .weight(0.2f),
                                           onClick = { musicServiceConnection.transportControls.pause() }) {
                                           Icon(
                                               painter = painterResource(id = R.drawable.exo_icon_pause),
                                               contentDescription = null
                                           )
                                       }
                                   }
                               }


                                LinearProgressIndicator(
                                    progress = musicServiceConnection.songDuration.value / MusicService.curSongDuration.toFloat(),
                                    Modifier
                                        .fillMaxWidth()
                                        .height(1.dp)
                                        .graphicsLayer {
                                            //   alpha = if (currentFraction > 0.001) 0f else 1f
                                        }
                                )
                            }
                        }
                    })
            }
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom,
            modifier = Modifier
                .zIndex(20f)
        ) {


            //why tf value.value
            //TODO fix later
            AnimatedVisibility(
                visible = bottomBarState.value.value,
                enter = slideInVertically(
                    initialOffsetY = { it }, animationSpec = tween(
                        durationMillis = 150,
                        easing = LinearEasing
                    )
                ),
                exit = slideOutVertically(
                    targetOffsetY = { it }, animationSpec = tween(
                        durationMillis = 150,
                        easing = LinearEasing
                    )
                ),
                content = {
                    BottomNavigation(
                        modifier = Modifier
                            .zIndex(21f)
                            .clip(shape = RoundedCornerShape(15.dp, 15.dp, 0.dp, 0.dp)),
                        backgroundColor = MaterialTheme.colors.secondary, elevation = 10.dp,
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
                })
        }
    }

}


//TODO needs better implementation?
//stutter on animation when scrolling up and down on lazycolumn screen.
@Composable
fun RowScope.AddItem(
    screen: BottomNavScreen,
    currentDestination: NavDestination?,
    navController: NavHostController,
    bottomBarState: MutableState<Boolean>,
) {
    BottomNavigationItem(
        label = {
            Text(text = screen.title)
        },
        icon = {
            Icon(
                imageVector = screen.icon,
                contentDescription = "Navigation Icon",
                tint = MaterialTheme.colors.primaryVariant
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

