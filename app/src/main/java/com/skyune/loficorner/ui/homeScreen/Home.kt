package com.skyune.loficorner.ui.homeScreen

import android.app.Activity
import android.media.session.PlaybackState
import android.os.Build.VERSION.SDK_INT
import android.support.v4.media.session.MediaControllerCompat
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.*
import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.room.Room
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import coil.size.Size
import com.rld.justlisten.android.ui.bottombars.sheets.BottomSheetScreen
import com.skyune.loficorner.R
import com.skyune.loficorner.exoplayer.MusicServiceConnection
import com.skyune.loficorner.model.Data
import com.skyune.loficorner.utils.playMusic
import com.skyune.loficorner.utils.playMusicFromId

import com.skyune.loficorner.widgets.RoundIconButton
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterialApi::class,
    ExperimentalAnimationGraphicsApi::class, InternalCoroutinesApi::class
)

@Composable
fun HomeScreen(musicServiceConnection: MusicServiceConnection) {
            Column(modifier = Modifier
                .background(
                    brush = Brush.linearGradient(
                        0f to MaterialTheme.colors.background,
                        1f to MaterialTheme.colors.onBackground,
                        start = Offset(250f, 300f),
                        end = Offset(900f, 1900.5f)
                    )
                )
                .fillMaxSize()
                .padding(30.dp, 0.dp, 30.dp, 4.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally) {
                     val shouldHavePlayBar by remember {
                        derivedStateOf {
                            musicServiceConnection.playbackState.value?.state == PlaybackState.STATE_PLAYING
                                    || musicServiceConnection.playbackState.value?.state == PlaybackState.STATE_PAUSED
                                    || musicServiceConnection.playbackState.value?.state == PlaybackState.STATE_SKIPPING_TO_NEXT
                                    || musicServiceConnection.playbackState.value?.state == PlaybackState.STATE_BUFFERING
                                    || musicServiceConnection.currentPlayingSong.value != null
                        }
                    }

                    if (shouldHavePlayBar) {
                        room()
                        val title by remember { derivedStateOf {
                            musicServiceConnection.currentPlayingSong.value?.description?.title.toString()  } }
                        val artist by remember { derivedStateOf {
                            musicServiceConnection.currentPlayingSong.value?.description?.subtitle.toString() } }

                        BoxWithConstraints() {
                            val constraints = this@BoxWithConstraints
                            PlayBarActionsMaximized(
                                bottomPadding = 0.dp,
                                currentFraction = 1f,
                                musicServiceConnection = musicServiceConnection,
                                title = title,
                                artist = artist,
                                onSkipNextPressed = { musicServiceConnection.transportControls?.skipToNext() },
                                maxWidth = constraints.maxWidth.value
                            )
                        }
                    }
            }
    }


@Composable
fun GifImage(
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val imageLoader = ImageLoader.Builder(context)
        .components {
            if (SDK_INT >= 28) {
                add(ImageDecoderDecoder.Factory())
            } else {
                add(GifDecoder.Factory())
            }
        }
        .build()
    Image(
        painter = rememberAsyncImagePainter(
            ImageRequest.Builder(context).data(data = R.drawable.night).apply(block = {
                size(Size.ORIGINAL)
            }).build(), imageLoader = imageLoader
        ),
        contentDescription = null,
        modifier = modifier
    )
}

@Composable
fun MarqueeText(
    text: String,
    modifier: Modifier = Modifier,
    textModifier: Modifier = Modifier,
    gradientEdgeColor: Color = Color.White,
    color: Color = Color.Unspecified,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontStyle: FontStyle? = null,
    fontWeight: FontWeight? = null,
    fontFamily: FontFamily? = null,
    letterSpacing: TextUnit = TextUnit.Unspecified,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign? = null,
    lineHeight: TextUnit = TextUnit.Unspecified,
    overflow: TextOverflow = TextOverflow.Clip,
    softWrap: Boolean = true,
    onTextLayout: (TextLayoutResult) -> Unit = {},
    style: TextStyle = LocalTextStyle.current,
) {
    val createText = @Composable { localModifier: Modifier ->
        Text(
            text,
            textAlign = textAlign,
            modifier = localModifier,
            color = color,
            fontSize = fontSize,
            fontStyle = fontStyle,
            fontWeight = fontWeight,
            fontFamily = fontFamily,
            letterSpacing = letterSpacing,
            textDecoration = textDecoration,
            lineHeight = lineHeight,
            overflow = overflow,
            softWrap = softWrap,
            maxLines = 1,
            onTextLayout = onTextLayout,
            style = style,
        )
    }
    var offset by remember { mutableStateOf(0) }
    val textLayoutInfoState = remember { mutableStateOf<TextLayoutInfo?>(null) }
    LaunchedEffect(textLayoutInfoState.value) {
        val textLayoutInfo = textLayoutInfoState.value ?: return@LaunchedEffect
        if (textLayoutInfo.textWidth <= textLayoutInfo.containerWidth) return@LaunchedEffect
        val duration = 7500 * textLayoutInfo.textWidth / textLayoutInfo.containerWidth
        val delay = 1000L
        do {
            val animation = TargetBasedAnimation(
                animationSpec = infiniteRepeatable(
                    animation = tween(
                        durationMillis = duration,
                        delayMillis = 1000,
                        easing = LinearEasing,
                    ),
                    repeatMode = RepeatMode.Restart
                ),
                typeConverter = Int.VectorConverter,
                initialValue = 0,
                targetValue = -textLayoutInfo.textWidth
            )
            val startTime = withFrameNanos { it }
            do {
                val playTime = withFrameNanos { it } - startTime
                offset = (animation.getValueFromNanos(playTime))
            } while (!animation.isFinishedFromNanos(playTime))
            delay(delay)
        } while (true)
    }

    SubcomposeLayout(
        modifier = modifier.clipToBounds()
    ) { constraints ->
        val infiniteWidthConstraints = constraints.copy(maxWidth = Int.MAX_VALUE)
        var mainText = subcompose(MarqueeLayers.MainText) {
            createText(textModifier)
        }.first().measure(infiniteWidthConstraints)

        var gradient: Placeable? = null

        var secondPlaceableWithOffset: Pair<Placeable, Int>? = null
        if (mainText.width <= constraints.maxWidth) {
            mainText = subcompose(MarqueeLayers.SecondaryText) {
                createText(textModifier.fillMaxWidth())
            }.first().measure(constraints)
            textLayoutInfoState.value = null
        } else {
            val spacing = constraints.maxWidth * 2 / 3
            textLayoutInfoState.value = TextLayoutInfo(
                textWidth = mainText.width + spacing,
                containerWidth = constraints.maxWidth
            )
            val secondTextOffset = mainText.width + offset + spacing
            val secondTextSpace = constraints.maxWidth - secondTextOffset
            if (secondTextSpace > 0) {
                secondPlaceableWithOffset = subcompose(MarqueeLayers.SecondaryText) {
                    createText(textModifier)
                }.first().measure(infiniteWidthConstraints) to secondTextOffset
            }
            gradient = subcompose(MarqueeLayers.EdgesGradient) {
                Row {
                    GradientEdge(gradientEdgeColor, Color.Transparent)
                    Spacer(Modifier.weight(1f))
                    GradientEdge(Color.Transparent, gradientEdgeColor)
                }
            }.first().measure(constraints.copy(maxHeight = mainText.height))
        }

        layout(
            width = constraints.maxWidth,
            height = mainText.height
        ) {
            mainText.place(offset, 0)
            secondPlaceableWithOffset?.let {
                it.first.place(it.second, 0)
            }
            gradient?.place(0, 0)
        }
    }
}

@Composable
private fun GradientEdge(
    startColor: Color, endColor: Color,
) {
    Box(
        modifier = Modifier
            .width(10.dp)
            .fillMaxHeight()
            .background(
                brush = Brush.horizontalGradient(
                    0f to startColor, 1f to endColor,
                )
            )
    )
}

private enum class MarqueeLayers { MainText, SecondaryText, EdgesGradient }
private data class TextLayoutInfo(val textWidth: Int, val containerWidth: Int)

@Preview
@Composable
fun HomeScreenPreview() {
    //HomeScreen(musicServiceConnection)
}