@file:OptIn(ExperimentalFoundationApi::class)

package com.skyune.loficorner.ui.profileScreen

import android.util.Log
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.skyune.loficorner.R
import com.skyune.loficorner.exoplayer.MusicServiceConnection
import com.skyune.loficorner.model.Data
import com.skyune.loficorner.model.Weather
import com.skyune.loficorner.ui.profileScreen.components.RoomImagesRow
import com.skyune.loficorner.ui.theme.Theme
import com.skyune.loficorner.utils.playMusicFromId
import com.skyune.loficorner.viewmodels.ProfileViewModel
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


@Composable
fun ProfileScreen(
    profileViewModel: ProfileViewModel = hiltViewModel(),
    musicServiceConnection: MusicServiceConnection,
    bottomBarState: MutableState<Boolean>,
    isLoaded: MutableState<Boolean>,
    onToggleTheme: (Theme) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        contentAlignment = Alignment.Center
    ) {
        val list by profileViewModel.allWords.observeAsState(listOf())
        ShowData(profileViewModel, musicServiceConnection, bottomBarState, isLoaded,list,onToggleTheme)
    }
}

@Composable
private fun LazyListState.isScrollingUp(): Boolean {
    var previousIndex by remember(this) { mutableStateOf(firstVisibleItemIndex) }
    var previousScrollOffset by remember(this) { mutableStateOf(firstVisibleItemScrollOffset) }
    return remember(this) {
        derivedStateOf {
            if (previousIndex != firstVisibleItemIndex) {
                previousIndex > firstVisibleItemIndex
            } else {
                previousScrollOffset >= firstVisibleItemScrollOffset
            }.also {
                previousIndex = firstVisibleItemIndex
                previousScrollOffset = firstVisibleItemScrollOffset
            }
        }
    }.value
}

@Composable
fun ShowData(
    profileViewModel: ProfileViewModel,
    musicServiceConnection: MusicServiceConnection,
    bottomBarState: MutableState<Boolean>,
    isLoaded: MutableState<Boolean>,
    list: List<Data>,
    onToggleTheme: (Theme) -> Unit
) {
    val listState = rememberLazyListState()
    val isPlayerReady: MutableState<Boolean> = remember{
        derivedStateOf {
            mutableStateOf(false)
        }
    }.value
    bottomBarState.value=listState.isScrollingUp()// If we're scrolling up, show the bottom bar

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(
                brush = Brush.linearGradient(
                    0f to Color(MaterialTheme.colors.background.value),
                    1f to Color(MaterialTheme.colors.onBackground.value),
                    start = Offset(0f, 255f),
                    end = Offset(900f, 1900.5f)
                )
            ),
        contentAlignment = Alignment.Center,
        ) {

        val showAll = remember { mutableStateOf(false) }
        if(profileViewModel.allWords.value?.isEmpty() == true)
        {
            profileViewModel.ShowPlaylistsSongs(isLoaded = isLoaded)
        }
        if(list.size>5)
        {
            val selectedItemId = remember { mutableStateOf(profileViewModel.selectedItemId.value) }

            LazyColumn(modifier = Modifier
                .simpleVerticalScrollbar(listState), contentPadding = PaddingValues(12.dp), state = listState) {
                item {
                    Column(
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text(
                            text = "Your Lofi Corner,",
                            color = Color(MaterialTheme.colors.surface.value),
                            textAlign = TextAlign.Start,
                            lineHeight = 26.sp,
                            style = TextStyle(
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold
                            ),
                            modifier = Modifier
                                .padding(10.dp,4.dp,0.dp,4.dp)
                                .wrapContentSize()
                        )
                    }
                }
                item {
                    Text(
                    text = "What music would you like to listen to?",
                    color = MaterialTheme.colors.onSurface,
                    textAlign = TextAlign.Start,
                    lineHeight = 15.sp,
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    ),
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(10.dp,0.dp,0.dp,6.dp)

                    )}

                item {
                    Column {
                        Row(
                                horizontalArrangement = Arrangement.SpaceEvenly, verticalAlignment = Alignment.CenterVertically) {
                            MusicSelectionButtons(profileViewModel)
                        }

                        Row(horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(0.dp,10.dp,0.dp,0.dp)) {
                            Text(
                                text = "Rooms",
                                color = Color(MaterialTheme.colors.surface.value),
                                textAlign = TextAlign.End,
                                lineHeight = 26.sp,
                                style = TextStyle(

                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold
                                    ),
                                modifier = Modifier.padding(10.dp,0.dp,0.dp,0.dp).height(42.dp)

                            )

                            Text(
                                text = "Show All",
                                color = Color(MaterialTheme.colors.onSurface.value),
                                textAlign = TextAlign.End,
                                lineHeight = 20.sp,
                                style = TextStyle(
                                    fontSize = 20.sp),
                                modifier = Modifier.padding(0.dp,0.dp,10.dp,0.dp)
                                    .weight(0.2f)
                                    .height(42.dp)
                                    .clickable {
                                        showAll.value = !showAll.value
                                    }
                            )
                        }
                        RoomImagesRow(showAll = showAll.value, onToggleTheme)
                    }
                }

                item {  Text(
                    text = "Featured playlists",
                    color = MaterialTheme.colors.surface,
                    textAlign = TextAlign.Center,
                    lineHeight = 26.sp,
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier
                    .padding(10.dp,0.dp,10.dp,5.dp)
                    .wrapContentSize())
                }

                items(list,key = {
                    it.id
                },)  { item ->
                    WeatherItem(
                        item = item,
                        isSelected = item.id == selectedItemId.value,
                        onItemClicked = {
                            selectedItemId.value = item.id
                            profileViewModel.selectItem(item.id)

                            profileViewModel.PlayPlaylist(
                                item,
                                isPlayerReady,
                                musicServiceConnection
                                )
                        })}
            }
        }
        else
        {
            CircularProgressIndicator(modifier = Modifier.fillMaxSize())
        }
 }}

@Composable
fun MusicSelectionButtons(profileViewModel: ProfileViewModel) {
    val selectedButtonIndex = remember { mutableStateOf(profileViewModel.selectedButtonIndexId.value) }

    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
        BouncingMusicSelectionButton(R.drawable.iconoir_coffee_cup__4_, "Chill", selectedButtonIndex.value == 0) { profileViewModel.selectButtonIndex(0)
            selectedButtonIndex.value = 0
        }
        BouncingMusicSelectionButton(
            R.drawable.fluent_sleep_20_filled__1_,
            "Sleepy",
            selectedButtonIndex.value == 1
        ) { profileViewModel.selectButtonIndex(1)
            selectedButtonIndex.value = 1
        }
        BouncingMusicSelectionButton(
            R.drawable.fluent_emoji_high_contrast_saxophone__1_,
            "Jazzy",
            selectedButtonIndex.value == 2
        ) { profileViewModel.selectButtonIndex(2)
            selectedButtonIndex.value = 2
        }
    }
}

@Composable
fun BouncingMusicSelectionButton(
    ImageId: Int,
    Title: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {

    val animationSpec = spring<Float>(
        dampingRatio = Spring.DampingRatioLowBouncy,
        stiffness = Spring.StiffnessLow
    )


    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.1f else 1f,
        animationSpec = animationSpec
    )

    Box(
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(0.dp,0.dp,0.dp,10.dp)
            .size(size = 130.dp)
            .scale(scale)
    ) {
        MusicSelectionButton(ImageId,Title, isSelected, onClick)
    }
}
@Composable
private fun MusicSelectionButton(
    ImageId: Int,
    Title: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(size = 130.dp)
            .padding(6.dp)
    ) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .clip(shape = RoundedCornerShape(15.dp))
                .clickable(onClick = onClick)
                .background(
                    if (!isSelected) {
                        Brush.linearGradient(
                            0f to Color(MaterialTheme.colors.primary.value),
                            1f to Color(MaterialTheme.colors.primaryVariant.value),
                            start = Offset(0f, 0f),
                            end = Offset(20f, 500f)
                        )
                    } else {
                        Brush.linearGradient(
                            0f to MaterialTheme.colors.onPrimary,
                            1f to MaterialTheme.colors.onSecondary,
                            start = Offset(0f, 0f),
                            end = Offset(100f, 450f)
                        )
                    }
                )
                .border(
                    BorderStroke(
                        1.dp, brush = Brush.linearGradient(
                            0f to MaterialTheme.colors.secondary,
                            1f to MaterialTheme.colors.secondaryVariant,
                            start = Offset(0f, 0f),
                            end = Offset(20f, 500f)
                        )
                    )
                ), contentAlignment = Alignment.Center
        ) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Card(
                    modifier = Modifier
                        .fillMaxSize(1f)
                        .aspectRatio(0.9f),
                    shape = CircleShape,
                    backgroundColor = Color.Transparent,
                    elevation = 0.dp
                ) {}
            }
            Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                Image(
                    rememberAsyncImagePainter(
                        ImageRequest.Builder(LocalContext.current)
                            .diskCachePolicy(CachePolicy.DISABLED)
                            .data(data = ImageId)
                            .build()
                    ),
                    modifier = Modifier.size(60.dp),
                    contentScale = ContentScale.FillBounds,
                    contentDescription = null
                )
                Text(text = Title, Modifier.padding(4.dp), color = MaterialTheme.colors.error)
            }
        }
        ClippedShadow(
            elevation = 10.dp,
            shape = RoundedCornerShape(15.dp),
            modifier = Modifier
                .matchParentSize()
        )
    }
}

@Composable
private fun PlayPlaylist(
    profileViewModel: ProfileViewModel,
    item: Data,
    isPlayerReady: MutableState<Boolean>,
    musicServiceConnection: MusicServiceConnection
) {
    val response: Call<Weather> =
        profileViewModel.getMovieById(item.id)
    response.enqueue(object : Callback<Weather> {
        override fun onFailure(call: Call<Weather>, t: Throwable) {
            Log.d("onFailure", t.message.toString())
        }

        override fun onResponse(
            call: Call<Weather>,
            response: Response<Weather>
        ) {
            if (isPlayerReady.value) {
                isPlayerReady.value = false
            }
            playMusicFromId(
                musicServiceConnection,
                response.body()!!.data,
                item.id,
                isPlayerReady.value
            )
            isPlayerReady.value = true
        }
    })
}

fun Modifier.simpleVerticalScrollbar(
    state: LazyListState,
    width: Dp = 4.dp
): Modifier = composed {
    val targetAlpha = if (state.isScrollInProgress) 1f else 0f
    val duration = if (state.isScrollInProgress) 300 else 800

    val alpha by animateFloatAsState(
        targetValue = targetAlpha,
        animationSpec = tween(durationMillis = duration)
    )


    drawWithContent {
        drawContent()

        val firstVisibleElementIndex = state.layoutInfo.visibleItemsInfo.firstOrNull()?.index
        val needDrawScrollbar = state.isScrollInProgress || alpha > 0.0f

        // Draw scrollbar if scrolling or if the animation is still running and lazy column has content
        if (needDrawScrollbar && firstVisibleElementIndex != null) {
            val elementHeight = this.size.height / state.layoutInfo.totalItemsCount
            val scrollbarOffsetY = firstVisibleElementIndex * elementHeight
            val scrollbarHeight = state.layoutInfo.visibleItemsInfo.size * elementHeight

            drawRect(
                color = Color.Red,
                topLeft = Offset(this.size.width - width.toPx(), scrollbarOffsetY),
                size = Size(width.toPx(), scrollbarHeight),
                alpha = alpha
            )
        }
    }
}


@Composable
fun ClippedShadow(elevation: Dp, shape: Shape, modifier: Modifier = Modifier) {
    Layout(
        modifier
            .drawWithCache {
                // Naive cache setup similar to foundation's Background.
                val path = Path()
                var lastSize: Size? = null

                fun updatePathIfNeeded() {
                    if (size != lastSize) {
                        path.reset()
                        path.addOutline(
                            shape.createOutline(size, layoutDirection, this)
                        )
                        lastSize = size
                    }
                }

                onDrawWithContent {
                    updatePathIfNeeded()
                    clipPath(path, ClipOp.Difference) {
                        this@onDrawWithContent.drawContent()
                    }
                }
            }
            .shadow(elevation, shape)
    ) { _, constraints ->
        layout(constraints.minWidth, constraints.minHeight) {}
    }
}

@Composable
fun WeatherItem(item: Data, onItemClicked: () -> Unit, isSelected: Boolean) {

Box(modifier = Modifier
    .wrapContentWidth()
    .wrapContentHeight()) {
    ClippedShadow(
        elevation = 10.dp,
        shape = RoundedCornerShape(15.dp),
        modifier = Modifier
            .matchParentSize()
            .padding(4.dp)
    )
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .clickable { onItemClicked() }
            .height(height = 100.dp)
            .clip(shape = RoundedCornerShape(15.dp))
            .background(
                if (!isSelected) {
                    Brush.linearGradient(
                        0f to Color(MaterialTheme.colors.primary.value),
                        1f to Color(MaterialTheme.colors.primaryVariant.value),
                        start = Offset(0f, 0f),
                        end = Offset(20f, 500f)
                    )
                } else {
                    Brush.linearGradient(
                        0f to Color(0xFFA920CF),
                        1f to Color(MaterialTheme.colors.primaryVariant.value),
                        start = Offset(0f, 0f),
                        end = Offset(20f, 500f)
                    )
                }
            )
            .border(
                BorderStroke(
                    1.dp, brush = Brush.linearGradient(
                        0f to Color(MaterialTheme.colors.secondary.value),
                        1f to Color(MaterialTheme.colors.secondaryVariant.value),
                        start = Offset(0f, 0f),
                        end = Offset(20f, 500f)
                    )
                )
            )
        , contentAlignment = Alignment.CenterStart) {
        Row() {
            Image(
                    rememberAsyncImagePainter(
                        ImageRequest.Builder(LocalContext.current)
                            .diskCachePolicy(CachePolicy.DISABLED)
                            .data(data = item.artwork?.small)
                            .build()
                    ),
                    modifier = Modifier
                        .size(100.dp)
                        .clip(shape = RoundedCornerShape(15.dp)),
                    contentScale = ContentScale.FillBounds,
                    contentDescription = null
                )
          Column(verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.Start) {
              item.playlist_name?.let {
                  Text(
                      text = it,
                      color = MaterialTheme.colors.surface,
                      lineHeight = 15.sp,
                      style = TextStyle(
                          fontSize = 13.sp,
                          fontWeight = FontWeight.Bold
                      ),
                      modifier = Modifier
                          .width(width = 209.dp)
                          .padding(10.dp)
                  )
              }
              item.user?.name?.let {
                  Text(
                      text = it,
                      color = MaterialTheme.colors.surface,
                      textAlign = TextAlign.Center,
                      lineHeight = 15.sp,
                      style = TextStyle(
                          fontSize = 9.sp
                      ),
                      modifier = Modifier
                          .width(width = 55.dp)
                          .height(height = 15.dp)
                  )
              }

              Box(modifier = Modifier, contentAlignment = Alignment.BottomStart){

                  ClippedShadow(
                      elevation = 12.dp,
                      shape = RoundedCornerShape(15.dp),
                      modifier = Modifier
                          .matchParentSize()
                          .padding(4.dp)
                  )
              Box(
                  modifier = Modifier
                      .width(width = 63.dp)
                      .height(height = 22.dp)
                      .clip(shape = RoundedCornerShape(15.dp))
                      .background(
                          brush = Brush.linearGradient(
                              0f to Color(MaterialTheme.colors.onPrimary.value),
                              1f to Color(MaterialTheme.colors.onSecondary.value),
                              start = Offset(0f, 0f),
                              end = Offset(0f, 80f)
                          )
                      ), contentAlignment = Alignment.Center
              ) {
                  item.user?.track_count?.let {
                      Text(
                          text = it.toString(),
                          color = Color(MaterialTheme.colors.onSurface.value),
                          textAlign = TextAlign.Center,
                          lineHeight = 15.sp,
                          style = TextStyle(
                              fontSize = 9.sp
                          ),
                          modifier = Modifier
                              .width(width = 55.dp)
                              .height(height = 15.dp)
                      )
                  }
              }
          }
}}}}}

@Composable
fun ProfileScreenPreview() {
}