package com.skyune.loficorner.ui.homeScreen

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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rld.justlisten.android.ui.extensions.ModifiedSlider
import com.rld.justlisten.android.ui.utils.offsetX
import com.rld.justlisten.android.ui.utils.widthSize
import com.skyune.loficorner.R
import com.skyune.loficorner.exoplayer.MusicService
import com.skyune.loficorner.exoplayer.MusicServiceConnection
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
@Composable
public fun PlayBarActionsMaximized(
    modifier: Modifier = Modifier,
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
                    musicServiceConnection.transportControls?.seekTo(musicServiceConnection.songDuration.value)
                    musicServiceConnection.sliderClicked.value = false
                    musicServiceConnection.updateSong()
                }
                is PressInteraction.Cancel -> {}
                is DragInteraction.Start -> {
                    musicServiceConnection.sliderClicked.value = true
                }
                is DragInteraction.Stop -> {
                    musicServiceConnection.transportControls?.seekTo(musicServiceConnection.songDuration.value)
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
                .fillMaxSize()
                .background(Color.Transparent)
                .padding(bottom = 40.dp)
                //.padding(bottom = bottomPadding + 5.dp, top = 5.dp),
                    ,
            verticalArrangement = Arrangement.Bottom
        ) {
            MarqueeText(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                text = title,
                fontWeight = FontWeight.Bold,
                fontSize = 19.sp,
                color = MaterialTheme.colors.surface,
                style = MaterialTheme.typography.body1,

                textAlign = TextAlign.Center, gradientEdgeColor = MaterialTheme.colors.primary)
            MarqueeText(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                text = artist,
                color = MaterialTheme.colors.onSurface,
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

            val customSliderColors = SliderDefaults.colors(
                thumbColor = MaterialTheme.colors.surface,
                activeTrackColor = MaterialTheme.colors.surface,
                inactiveTrackColor = MaterialTheme.colors.secondary

            )

            ModifiedSlider(
                interactionSource = interactionSource,
                modifier = Modifier
                    .offset(x = offsetX(currentFraction, maxWidth).dp)
                    .width(widthSize(currentFraction, maxWidth).dp),
                colors = customSliderColors,
                value = sliderValue, onValueChange = {
                    sliderValueRaw = it
                }, onValueChangeFinished = {musicServiceConnection.songDuration.value =
                    (sliderValue * MusicService.curSongDuration).toLong()}


            )


            Row(
                Modifier.wrapContentHeight()
            ) {
                if (musicServiceConnection.shuffleMode == SHUFFLE_MODE_NONE) {
                    Icon(
                        modifier = Modifier
                            .size(40.dp)
                            .alpha(0.6f)
                            .weight(0.2f)
                            .clickable {
                                musicServiceConnection.transportControls?.setShuffleMode(
                                    SHUFFLE_MODE_ALL
                                )
                            },
                        //off
                        painter = painterResource(id = R.drawable.exo_icon_shuffle_on),
                        contentDescription = null,
                        tint = MaterialTheme.colors.surface
                    )
                } else {
                    Icon(
                        modifier = Modifier
                            .size(40.dp)
                            .weight(0.2f)
                            .clickable {
                                musicServiceConnection.transportControls?.setShuffleMode(
                                    SHUFFLE_MODE_NONE
                                )
                            },
                        painter = painterResource(id = R.drawable.exo_icon_shuffle_on),
                        contentDescription = null,
                        tint = MaterialTheme.colors.onSurface

                    )
                }
                Icon(
                    modifier = Modifier
                        .size(40.dp)
                        .weight(0.2f)
                        .clickable {
                            musicServiceConnection.transportControls?.skipToPrevious()
                        },
                    painter = painterResource(id = R.drawable.exo_ic_skip_previous),
                    contentDescription = null,
                    tint = MaterialTheme.colors.surface
                )
                if (musicServiceConnection.playbackState.value?.state != PlaybackState.STATE_PLAYING &&
                    musicServiceConnection.playbackState.value?.state != PlaybackState.STATE_BUFFERING
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .scale(1.2f)
                            .weight(0.105f)
                    ) {
                        // Circle wrapper with brush background
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    brush = Brush.linearGradient(
                                        colors = listOf(
                                            MaterialTheme.colors.onPrimary,
                                            MaterialTheme.colors.onSecondary
                                        ),
                                        start = Offset(0f, 0f),
                                        end = Offset(40f, 140f)
                                    ),
                                    shape = CircleShape
                                )
                        ) {
                            OutlinedButton(
                                shape = CircleShape,
                                border = BorderStroke(0.dp, MaterialTheme.colors.onSurface),
                                contentPadding = PaddingValues(0.dp),
                                colors = ButtonDefaults.buttonColors(Color.Transparent),
                                modifier = Modifier
                                    .size(40.dp)
                                    .scale(1f),

                                onClick = { musicServiceConnection.transportControls?.play() }) {
                                Icon(
                                    painter = painterResource(id = R.drawable.exo_icon_play),
                                    contentDescription = null,
                                    tint = Color.White
                                )
                            }
                        }
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .scale(1.2f)
                            .weight(0.105f)
                    ) {
                        // Circle wrapper with brush background
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    brush = Brush.linearGradient(
                                        colors = listOf(
                                            MaterialTheme.colors.onPrimary,
                                            MaterialTheme.colors.onSecondary
                                        ),
                                        start = Offset(0f, 0f),
                                        end = Offset(40f, 140f)
                                    ),
                                    shape = CircleShape
                                )
                        ) {
                            OutlinedButton(
                                shape = CircleShape,
                                border = BorderStroke(0.dp, MaterialTheme.colors.onSurface),
                                contentPadding = PaddingValues(0.dp),
                                colors = ButtonDefaults.buttonColors(Color.Transparent),
                                modifier = Modifier
                                    .size(40.dp)
                                    .scale(1f),
                                onClick = { musicServiceConnection.transportControls?.pause() }) {
                                Icon(
                                    painter = painterResource(id = R.drawable.exo_icon_pause),
                                    contentDescription = null,
                                    tint = Color.White
                                )
                            }
                        }
                    }
                }

                Icon(
                    painter = painterResource(id = R.drawable.exo_ic_skip_next),
                    modifier = Modifier
                        .size(40.dp)
                        .clickable(onClick = onSkipNextPressed)
                        .weight(0.2f),
                    contentDescription = null,
                    tint = MaterialTheme.colors.surface
                )
                when (musicServiceConnection.repeatMode) {
                    //off
                    REPEAT_MODE_NONE -> Icon(
                        modifier = Modifier
                            .size(40.dp)
                            .alpha(0.6f)
                            .weight(0.2f)
                            .clickable {
                                musicServiceConnection.transportControls?.setRepeatMode(
                                    REPEAT_MODE_ONE
                                )
                            },
                        painter = painterResource(id = R.drawable.exo_icon_repeat_all),
                        contentDescription = null,
                        tint = MaterialTheme.colors.surface
                    )

                    //off
                    REPEAT_MODE_ONE -> Icon(
                        modifier = Modifier
                            .size(40.dp)
                            .weight(0.2f)
                            .clickable {
                                musicServiceConnection.transportControls?.setRepeatMode(
                                    REPEAT_MODE_ALL
                                )
                            },
                        painter = painterResource(id = R.drawable.exo_icon_repeat_one),
                        contentDescription = null,
                        tint = MaterialTheme.colors.onSurface

                    )
                    REPEAT_MODE_ALL -> Icon(
                        modifier = Modifier
                            .size(40.dp)
                            .weight(0.2f)
                            .clickable {
                                musicServiceConnection.transportControls?.setRepeatMode(
                                    REPEAT_MODE_NONE
                                )
                            },
                        painter = painterResource(id = R.drawable.exo_icon_repeat_all),
                        contentDescription = null,
                        tint = MaterialTheme.colors.onSurface

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
