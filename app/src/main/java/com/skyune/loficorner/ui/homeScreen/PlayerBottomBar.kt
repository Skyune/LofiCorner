package com.skyune.loficorner.ui.homeScreen

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.rld.justlisten.android.ui.extensions.noRippleClickable
import com.skyune.loficorner.model.SongIconList
import com.rld.justlisten.datalayer.models.UserModel
import com.skyune.loficorner.R
import com.skyune.loficorner.exoplayer.MusicService.Companion.curSongDuration
import com.skyune.loficorner.exoplayer.MusicServiceConnection
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
@Composable
fun PlayerBottomBar(
    isExtended: Boolean,
    songIcon: String,
    title: String,
    artist: String,
    currentFraction: Float,
    musicServiceConnection: MusicServiceConnection,
    onSkipNextPressed: () -> Unit,
    onMoreClicked: () -> Unit,
    onBackgroundClicked: () -> Unit,
    painterLoaded: (Painter) -> Unit
) {
    val color = MaterialTheme.colors.background.toArgb()
    var gradientColor by remember {
        mutableStateOf(color)
    }

    BoxWithConstraints(
        modifier = if (isExtended) Modifier
            .noRippleClickable { onBackgroundClicked() } else Modifier.noRippleClickable { onBackgroundClicked() }
    ) {
        val constraints = this@BoxWithConstraints
        Column(Modifier.fillMaxSize(), ) {


            //PlayBarTopSection(currentFraction, onCollapsedClicked, onMoreClicked)

//            PlayBarSwipeActions(
//                songIcon, currentFraction, constraints,
//                title, musicServiceConnection, onSkipNextPressed, painterLoaded, onFavoritePressed,
//                newDominantColor = { color ->
//                    gradientColor = color
//                    newDominantColor(color)
//                },
//                playBarMinimizedClicked = playBarMinimizedClicked
//            )
            LinearProgressIndicator(
                progress = musicServiceConnection.songDuration.value / curSongDuration.toFloat(),
                Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .graphicsLayer {
                     //   alpha = if (currentFraction > 0.001) 0f else 1f
                    }
            )
            PlayBarActionsMaximized(
                bottomPadding = {
                    if (isExtended) {
                        0.dp
                    } else {
                        16.dp
                    }
                }(),
                currentFraction,
                musicServiceConnection,
                title,
                artist,
                onSkipNextPressed,
                constraints.maxWidth.value
            )
        }
    }
}


@Composable
fun IsLoading(isLoading: Boolean, modifier: Modifier) {
    if (isLoading) {
        Box(
            modifier = modifier.size(48.dp),
            contentAlignment = Alignment.Center,
        ) {
            CircularProgressIndicator()
            Icon(
                painter = painterResource(id = R.drawable.exo_icon_pause),
                modifier = modifier.size(35.dp),
                contentDescription = null
            )
        }
    }
}
