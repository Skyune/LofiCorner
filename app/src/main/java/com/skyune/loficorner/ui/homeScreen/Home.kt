package com.skyune.loficorner.ui.homeScreen

import android.app.Activity
import android.content.Context
import android.gesture.GestureLibraries.fromRawResource
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.media.session.PlaybackState
import android.net.Uri
import android.os.Build.VERSION.SDK_INT

import android.support.v4.media.session.MediaControllerCompat
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.annotation.DrawableRes
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
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.room.Room
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import coil.size.Size
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ui.StyledPlayerView
import com.rld.justlisten.android.ui.bottombars.sheets.BottomSheetScreen
import com.skyune.loficorner.R
import com.skyune.loficorner.exoplayer.MusicServiceConnection
import com.skyune.loficorner.model.Data
import com.skyune.loficorner.utils.playMusic
import com.skyune.loficorner.utils.playMusicFromId

import com.skyune.loficorner.widgets.RoundIconButton
import com.yeocak.parallaximage.*
import com.yeocak.parallaximage.ParallaxImage
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
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
                        //ParallaxImage(image = R.drawable.witch, sensor = GravitySensorDefaulted(context = LocalContext.current))

                        val context = LocalContext.current
                        val scope = rememberCoroutineScope()

                        var data by remember { mutableStateOf<SensorData?>(null) }

                        DisposableEffect(Unit) {
                            val dataManager = SensorDataManager(context)
                            dataManager.init()

                            scope.launch {
                                dataManager.data
                                    .receiveAsFlow()
                                    .onEach { data = it }
                                    .collect()
                            }

                            onDispose {
                                dataManager.cancel()
                            }
                        }

                        if (data != null) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .offset(y = 40.dp)
                            ) {
                                // Blurred image as glow shadow for the card
                                // Will move in opposite direction of Image Card to reveal itself when inclined
                                Image(
                                    painter = painterResource(id = R.drawable.witchopaque),
                                    modifier = Modifier
                                        .offset(
                                            x = (-data!!.roll * 0.14).dp,
                                            y = (data!!.pitch * 0.14).dp
                                        )
                                        .wrapContentSize()
                                        .aspectRatio(0.9f)
                                        .scale(1.08f)

                                        .blur(
                                            radius = 10.dp,
                                            edgeTreatment = BlurredEdgeTreatment.Unbounded

                                        ),
                                contentDescription = null,
                                contentScale = ContentScale.FillHeight,
                                )

                                // White edge shadow (should move slightly slower than the card)
//                                Box(
//                                    modifier = Modifier
//                                        .offset(
//                                            x = (data!!.roll * 0.35).dp,
//                                            y = (-data!!.pitch * 0.35).dp
//                                        )
//                                        .width(300.dp)
//                                        .height(400.dp)
//                                        .background(
//                                            color = Color.White.copy(alpha = 0.0f),
//                                            shape = RoundedCornerShape(16.dp)
//                                        ),
//                                )

                                Image(
                                    painter = painterResource(id = R.drawable.stars),
                                    modifier = Modifier
                                        .offset(
                                            x = (data!!.roll * 0.25).dp,
                                            y = (-data!!.pitch * 0.25).dp
                                        )
                                        .wrapContentSize().aspectRatio(0.9f).scale(0.9f),

                                    contentDescription = null,
                                    contentScale = ContentScale.FillHeight,
                                    alignment = BiasAlignment(
                                        horizontalBias = -(data!!.roll * 0.001).toFloat(),
                                        verticalBias = 0f,
                                    )
                                )


//                                Box (modifier = Modifier.offset(75.dp, -50.dp)) {
//                                    VideoView(
//                                        videoResourceId = R.raw.bluestars, modifier = Modifier
//                                            .offset(
//                                                x = (data!!.roll * 0.25).dp,
//                                                y = (-data!!.pitch * 0.25).dp
//                                            )
//                                            .wrapContentSize().aspectRatio(0.9f).scale(0.4f)
//                                    )
//                                }

                                // Image Card (with slight parallax for the image itself)
                                Image(
                                    painter = painterResource(id = R.drawable.witchopaque),
                                    modifier = Modifier
                                        .offset(
                                            x = (data!!.roll * 0.1).dp,
                                            y = (-data!!.pitch * 0.1).dp
                                        )
                                        .wrapContentSize()
                                        .aspectRatio(0.9f)
                                        .scale(1.08f),

                                    contentDescription = null,
                                    contentScale = ContentScale.FillHeight,
                                    alignment = BiasAlignment(
                                        horizontalBias = -(data!!.roll * 0.001).toFloat(),
                                        verticalBias = 0f,
                                    )
                                )
                                
//                                VideoView(videoResourceId = R.raw.cat, modifier = Modifier.offset(
//                                            x = (data!!.roll * 0.3).dp,
//                                            y = (-data!!.pitch * 0.3).dp
//                                        ))
                            }
                        }
                        class SensorDataManager (context: Context): SensorEventListener {

                            private val sensorManager by lazy {
                                context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
                            }

                            fun init() {
                                Log.d("SensorDataManager", "init")
                                val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY)
                                val magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)

                                sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI)
                                sensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_UI)
                            }

                            private var gravity: FloatArray? = null
                            private var geomagnetic: FloatArray? = null

                            val data1: Channel<SensorData> = Channel(Channel.UNLIMITED)


                            override fun onSensorChanged(event: SensorEvent?) {
                                if (event?.sensor?.type == Sensor.TYPE_GRAVITY)
                                    gravity = event.values

                                if (event?.sensor?.type == Sensor.TYPE_MAGNETIC_FIELD)
                                    geomagnetic = event.values

                                if (gravity != null && geomagnetic != null) {
                                    var r = FloatArray(9)
                                    var i = FloatArray(9)

                                    if (SensorManager.getRotationMatrix(r, i, gravity, geomagnetic)) {
                                        var orientation = FloatArray(3)
                                        SensorManager.getOrientation(r, orientation)

                                        data1.trySend (
                                            SensorData(
                                                roll = orientation[2].toDouble(),
                                                pitch = orientation[1].toDouble()
                                            )
                                        )
                                    }
                                }
                            }

                            fun cancel() {
                                Log.d("SensorDataManager", "cancel")
                                sensorManager.unregisterListener(this)
                            }

                            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
                        }

                        data class SensorData(
                            val roll: Double,
                            val pitch: Double
                        )

                       // VideoView(R.raw.cat)
                        //room()
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

class SensorDataManager (context: Context): SensorEventListener {

    private val sensorManager by lazy {
        context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }

    fun init() {
        Log.d("SensorDataManager", "init")
        val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY)
        val magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)

        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI)
        sensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_UI)
    }

    private var gravity: FloatArray? = null
    private var geomagnetic: FloatArray? = null

    val data: Channel<SensorData> = Channel(Channel.UNLIMITED)

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_GRAVITY)
            gravity = event.values

        if (event?.sensor?.type == Sensor.TYPE_MAGNETIC_FIELD)
            geomagnetic = event.values

        if (gravity != null && geomagnetic != null) {
            var r = FloatArray(9)
            var i = FloatArray(9)

            if (SensorManager.getRotationMatrix(r, i, gravity, geomagnetic)) {
                var orientation = FloatArray(3)
                SensorManager.getOrientation(r, orientation)

                data.trySend(
                    SensorData(
                        roll = orientation[2] * 180 / Math.PI,
                        pitch = orientation[1] * 180 / Math.PI
                    )
                )
            }
        }
    }

    fun cancel() {
        Log.d("SensorDataManager", "cancel")
        sensorManager.unregisterListener(this)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
}

data class SensorData(
    val roll: Double,
    val pitch: Double
)


@Composable
fun VideoView(videoResourceId: Int, modifier: Modifier) {
    val context = LocalContext.current

    val rawPath = "android.resource://${context.packageName}/$videoResourceId"
    val videoUri = Uri.parse(rawPath)

    val exoPlayer = ExoPlayer.Builder(context)
        .build()
        .also { exoPlayer ->
            val mediaItem = MediaItem.fromUri(videoUri)
            exoPlayer.setMediaItem(mediaItem)
            exoPlayer.repeatMode = Player.REPEAT_MODE_ONE
            exoPlayer.prepare()
            exoPlayer.play()
        }

    DisposableEffect(
        AndroidView(factory = {
            StyledPlayerView(context).apply {
                player = exoPlayer
                useController = false
            }

        },   modifier = modifier

        )
    ) {
        onDispose { exoPlayer.release() }
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