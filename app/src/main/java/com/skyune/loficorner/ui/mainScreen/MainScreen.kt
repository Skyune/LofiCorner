package com.skyune.loficorner.ui.mainScreen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.skyune.loficorner.exoplayer.MusicServiceConnection
import com.skyune.loficorner.model.Data
import com.skyune.loficorner.navigation.WeatherNavigation
import com.skyune.loficorner.ui.BottomNavScreen
import com.skyune.loficorner.viewmodels.MainViewModel
import com.yeocak.parallaximage.GravitySensorDefaulted



@Composable
fun MainScreen(
    navController: NavController,
    mainViewModel: MainViewModel = hiltViewModel(),
    onToggleTheme: () -> Unit,
    onToggleDarkMode: () -> Unit,
    musicServiceConnection: MusicServiceConnection,
    gravitySensorDefaulted: GravitySensorDefaulted
) {

    val navController = rememberNavController()

    val bottomBarState = rememberSaveable { (mutableStateOf(true)) }

    val isLoaded = rememberSaveable { (mutableStateOf(false)) }

    var myList: MutableList<Data> = mutableListOf<Data>()

    //Scaffold from Accompanist, initialized in build.gradle. (for hide bottom bar support)
    Scaffold(
        bottomBar = { BottomBar(navController = navController, bottomBarState)  }
    ) {

            WeatherNavigation(navController = navController,
                onToggleTheme,
                onToggleDarkMode,
                musicServiceConnection,
                gravitySensorDefaulted,
                bottomBarState,
                isLoaded,
            myList)

    }


}

@Composable
fun BottomBar(navController: NavHostController, bottomBarState: MutableState<Boolean>) {
    val screens = listOf(
        BottomNavScreen.Home,
        BottomNavScreen.Profile,
        BottomNavScreen.Settings,
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Bottom) {


//        Card(
//            modifier = Modifier
//                .clip(shape = RoundedCornerShape(15.dp, 15.dp, 0.dp, 0.dp))
//                .height(50.dp)
//                .fillMaxWidth(), backgroundColor = MaterialTheme.colors.background
//        )
//        {}
        AnimatedVisibility(
            visible = bottomBarState.value,
            enter = slideInVertically(initialOffsetY = { it }, animationSpec = tween(
                durationMillis = 300,
                easing = LinearEasing
            )),
            exit = slideOutVertically(targetOffsetY = { it }, animationSpec = tween(
                durationMillis = 300,
                easing = LinearEasing
            )),
            content = {
        BottomNavigation(
            modifier = Modifier
                .zIndex(2f)
                .clip(shape = RoundedCornerShape(15.dp, 15.dp, 0.dp, 0.dp)),
            backgroundColor = MaterialTheme.colors.secondary, elevation = 10.dp,
        ) {
            screens.forEach { screen ->
                AddItem(
                    screen = screen,
                    currentDestination = currentDestination,
                    navController = navController,
                    bottomBarState = bottomBarState,
                )
            }
        }})
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

