package com.skyune.loficorner.ui.homeScreen

import android.graphics.drawable.AnimationDrawable
import android.media.session.PlaybackState
import android.support.v4.media.session.PlaybackStateCompat.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.rld.justlisten.android.ui.extensions.ModifiedSlider
import com.rld.justlisten.android.ui.utils.offsetX
import com.rld.justlisten.android.ui.utils.widthSize
import com.skyune.loficorner.R
import com.skyune.loficorner.exoplayer.MusicService
import com.skyune.loficorner.exoplayer.MusicServiceConnection
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
@Composable
fun PlayBarActionsMaximized(
    bottomPadding: Dp,
    currentFraction: Float,
    musicServiceConnection: MusicServiceConnection,
    title: String,
    artist: String,
    onSkipNextPressed: () -> Unit,
    maxWidth: Float,
) {
    val interactionSource = remember { MutableInteractionSource() }


    LaunchedEffect(interactionSource) {
        interactionSource.interactions.collect { interaction ->
            when (interaction) {
                is PressInteraction.Press -> {
                    musicServiceConnection.sliderClicked.value = true
                }
                is PressInteraction.Release -> {
                    musicServiceConnection.transportControls.seekTo(musicServiceConnection.songDuration.value)
                    musicServiceConnection.sliderClicked.value = false
                    musicServiceConnection.updateSong()
                }
                is PressInteraction.Cancel -> {}
                is DragInteraction.Start -> {
                    musicServiceConnection.sliderClicked.value = true
                }
                is DragInteraction.Stop -> {
                    musicServiceConnection.transportControls.seekTo(musicServiceConnection.songDuration.value)
                    musicServiceConnection.sliderClicked.value = false
                    musicServiceConnection.updateSong()
                }
                is DragInteraction.Cancel -> {}
            }
        }
    }


    if (currentFraction == 1f) {
        var sliderPosition by remember { mutableStateOf(0f) }
        sliderPosition =
            musicServiceConnection.songDuration.value / MusicService.curSongDuration.toFloat()




        Column(
            Modifier
                .fillMaxSize().background(Color.Transparent)
                .padding(bottom = bottomPadding + 5.dp,top = 5.dp),
            verticalArrangement = Arrangement.Bottom
        ) {
            MarqueeText(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                text = title,
                style = MaterialTheme.typography.body1,
                textAlign = TextAlign.Center, gradientEdgeColor = MaterialTheme.colors.primary)
            MarqueeText(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                text = artist,
                textAlign = TextAlign.Center, gradientEdgeColor = MaterialTheme.colors.primary)


            var sliderValueRaw by remember { mutableStateOf(sliderPosition) }

            val isPressed by interactionSource.collectIsPressedAsState()
            val isDragged by interactionSource.collectIsDraggedAsState()
            val isInteracting = isPressed || isDragged

            val sliderValue by derivedStateOf {
                if (isInteracting) {
                    sliderValueRaw
                } else {
                    musicServiceConnection.songDuration.value / MusicService.curSongDuration.toFloat()

                }
            }

            ModifiedSlider(
                interactionSource = interactionSource,
                modifier = Modifier
                    .offset(x = offsetX(currentFraction, maxWidth).dp)
                    .width(widthSize(currentFraction, maxWidth).dp),
                value = sliderValue, onValueChange = {
                    sliderValueRaw = it
                }, onValueChangeFinished = {musicServiceConnection.songDuration.value =
                    (sliderValue * MusicService.curSongDuration).toLong()}
            )
            Row(
                Modifier.height(200.dp)
            ) {
                if (musicServiceConnection.shuffleMode == SHUFFLE_MODE_NONE) {
                    Icon(
                        modifier = Modifier
                            .size(40.dp)
                            .weight(0.2f)
                            .clickable {
                                musicServiceConnection.transportControls.setShuffleMode(
                                    SHUFFLE_MODE_ALL
                                )
                            },
                        painter = painterResource(id = R.drawable.exo_icon_shuffle_off),
                        contentDescription = null,
                    )
                } else {
                    Icon(
                        modifier = Modifier
                            .size(40.dp)
                            .weight(0.2f)
                            .clickable {
                                musicServiceConnection.transportControls.setShuffleMode(
                                    SHUFFLE_MODE_NONE
                                )
                            },
                        painter = painterResource(id = R.drawable.exo_icon_shuffle_on),
                        contentDescription = null,
                    )
                }
                Icon(
                    modifier = Modifier
                        .size(40.dp)
                        .weight(0.2f)
                        .clickable {
                            musicServiceConnection.transportControls.skipToPrevious()
                        },
                    painter = painterResource(id = R.drawable.exo_ic_skip_previous),
                    contentDescription = null,
                )
                if (musicServiceConnection.playbackState.value?.state != PlaybackState.STATE_PLAYING &&
                    musicServiceConnection.playbackState.value?.state != PlaybackState.STATE_BUFFERING
                ) {
                    OutlinedButton(
                        shape = CircleShape,
                        border = BorderStroke(1.dp, MaterialTheme.colors.onSurface),
                        contentPadding = PaddingValues(0.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colors.onSurface),
                        modifier = Modifier
                            .size(40.dp)
                            .weight(0.2f),
                        onClick = { musicServiceConnection.transportControls.play() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.exo_icon_play),
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

                Icon(
                    painter = painterResource(id = R.drawable.exo_ic_skip_next),
                    modifier = Modifier
                        .size(40.dp)
                        .clickable(onClick = onSkipNextPressed)
                        .weight(0.2f),
                    contentDescription = null,
                )
                when (musicServiceConnection.repeatMode) {
                    REPEAT_MODE_NONE -> Icon(
                        modifier = Modifier
                            .size(40.dp)
                            .weight(0.2f)
                            .clickable {
                                musicServiceConnection.transportControls.setRepeatMode(
                                    REPEAT_MODE_ONE
                                )
                            },
                        painter = painterResource(id = R.drawable.exo_icon_repeat_off),
                        contentDescription = null,
                    )
                    REPEAT_MODE_ONE -> Icon(
                        modifier = Modifier
                            .size(40.dp)
                            .weight(0.2f)
                            .clickable {
                                musicServiceConnection.transportControls.setRepeatMode(
                                    REPEAT_MODE_ALL
                                )
                            },
                        painter = painterResource(id = R.drawable.exo_icon_repeat_one),
                        contentDescription = null,
                    )
                    REPEAT_MODE_ALL -> Icon(
                        modifier = Modifier
                            .size(40.dp)
                            .weight(0.2f)
                            .clickable {
                                musicServiceConnection.transportControls.setRepeatMode(
                                    REPEAT_MODE_NONE
                                )
                            },
                        painter = painterResource(id = R.drawable.exo_icon_repeat_all),
                        contentDescription = null,
                    )
                }
            }
        }
    }
}


@Composable
fun Slider(
    initialValue: Float,
    interactionSource: MutableInteractionSource,
    currentFraction: Float,
    maxWidth: Float,
    musicServiceConnection: MusicServiceConnection,
    valueSetter: (Float) -> Unit,
) {
    val value by remember { mutableStateOf(initialValue) }
    ModifiedSlider(
        interactionSource = interactionSource,
        modifier = Modifier
            .offset(x = offsetX(currentFraction, maxWidth).dp)
            .width(widthSize(currentFraction, maxWidth).dp),
        value = value, onValueChange = {
            musicServiceConnection.songDuration.value =
                (it * MusicService.curSongDuration).toLong()
        }
    )
}
