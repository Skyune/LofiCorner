@file:OptIn(ExperimentalFoundationApi::class)

package com.skyune.loficorner.ui.profileScreen

import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.input.key.Key.Companion.F
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.skyune.loficorner.exoplayer.MusicServiceConnection
import com.skyune.loficorner.model.Data
import com.skyune.loficorner.model.Weather
import com.skyune.loficorner.ui.homeScreen.WeatherItem
import com.skyune.loficorner.ui.profileScreen.components.RoomImagesRow
import com.skyune.loficorner.utils.playMusicFromId
import com.skyune.loficorner.viewmodels.ProfileViewModel
import retrofit2.Call
import retrofit2.Response
import kotlin.time.Duration.Companion.hours


@Composable
fun ProfileScreen(
    profileViewModel: ProfileViewModel = hiltViewModel(),
    musicServiceConnection: MusicServiceConnection,
    bottomBarState: MutableState<Boolean>,
    isLoaded: MutableState<Boolean>
) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        contentAlignment = Alignment.Center
    ) {
        val list by profileViewModel.allWords.observeAsState(listOf())

        ShowData(profileViewModel, musicServiceConnection, bottomBarState, isLoaded,list)
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
    list: List<Data>
) {
    val listState = rememberLazyListState()
    //val scrollContext = rememberScrollContext(listState = listState)
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
            .background(brush = Brush.linearGradient(
                0f to Color(0xfff8eef9),
                1f to Color(0xffd4b2c7),
                start = Offset(0f, 255f),
                end = Offset(400f, 1900.5f))),
        contentAlignment = Alignment.Center,

        ) {



        if(profileViewModel.allWords.value?.isEmpty() == true)
        {
            Log.d("TAG", "ShowData: list is empty")
            profileViewModel.ShowPlaylistsSongs(isLoaded = isLoaded)

        }
        if(list.size>10)
        {

            LazyColumn(modifier = Modifier
                .padding(2.dp)
                .simpleVerticalScrollbar(listState), contentPadding = PaddingValues(1.dp), state = listState) {
                item { RoomImagesRow() }
                items(list,key = {
                    it.id
                },)  { item ->
                    WeatherItem(
                        item = item,
                        onItemClicked = {

                            //TODO("Refactor this abomination")

                            val response: Call<Weather> =
                                profileViewModel.getMovieById(item.id)
                            response.enqueue(object : retrofit2.Callback<Weather> {
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
                        })})}
//                if (scrollContext.isBottom)
//                {
//                   // profileViewModel.ShowPlaylistsSongs(profileViewModel,context)
//                }
            }
        }
        else
        {
            CircularProgressIndicator(modifier = Modifier.fillMaxSize())
        }
 }}

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
fun rememberScrollContext(listState: LazyListState): ScrollContext {
    val scrollContext by remember {
        derivedStateOf {
            ScrollContext(
                isTop = listState.isFirstItemVisible,
                isBottom = listState.isLastItemVisible
            )
        }
    }
    return scrollContext
}

val LazyListState.isLastItemVisible: Boolean
    get() = layoutInfo.visibleItemsInfo.lastOrNull()?.index == layoutInfo.totalItemsCount - 1

val LazyListState.isFirstItemVisible: Boolean
    get() = firstVisibleItemIndex == 0


data class ScrollContext(
    val isTop: Boolean,
    val isBottom: Boolean,
)


@Composable
fun WeatherItem(item: Data, onItemClicked: () -> Unit) {

Box(modifier = Modifier.wrapContentWidth().wrapContentHeight()) {
    ClippedShadow(
        elevation = 10.dp,
        shape = RoundedCornerShape(15.dp),
        modifier = Modifier.matchParentSize().padding(4.dp)
    )
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .height(height = 100.dp)
            .clip(shape = RoundedCornerShape(15.dp))
            .background(
                brush = Brush.linearGradient(
                    0f to Color(0xfff0e1ed),
                    1f to Color(0xffd4b2c6),
                    start = Offset(0f, 0f),
                    end = Offset(20f, 500f)
                )
            ).border(BorderStroke(1.dp, brush = Brush.linearGradient(
                0f to Color(0xFFFBD4EB),
                1f to Color(0xffFFB0DF),
                start = Offset(0f, 0f),
                end = Offset(20f, 500f))))
        , contentAlignment = Alignment.CenterStart) {



        Row() {
            Image(
                    rememberAsyncImagePainter(
                        ImageRequest.Builder(LocalContext.current)
                            .diskCachePolicy(CachePolicy.DISABLED)
                            .data(data = item.artwork?.small)
                            .build()
                    ),
                    modifier = Modifier.size(100.dp)
                        .clip(shape = RoundedCornerShape(15.dp)),
                    contentScale = ContentScale.FillBounds,
                    contentDescription = null
                )
          Column(verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.Start) {
              item.playlist_name?.let {
                  Text(
                      text = it,
                      color = Color(0xff56434d),
                      lineHeight = 15.sp,
                      style = TextStyle(
                          fontSize = 13.sp,
                          fontWeight = FontWeight.Bold
                      ),
                      modifier = Modifier
                          .width(width = 209.dp).padding(10.dp)
                  )
              }
              item.user?.name?.let {
                  Text(
                      text = it,
                      color = Color(0xff56434d),
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
                      modifier = Modifier.matchParentSize().padding(4.dp)
                  )
              Box(
                  modifier = Modifier
                      .width(width = 63.dp)
                      .height(height = 22.dp)
                      .clip(shape = RoundedCornerShape(15.dp))
                      .background(
                          brush = Brush.linearGradient(
                              0f to Color(0xfffec5f3),
                              1f to Color(0xffbb70c8),
                              start = Offset(0f, 0f),
                              end = Offset(0f, 120f)
                          )
                      ), contentAlignment = Alignment.Center
              ) {
                  item.user?.track_count?.let {
                      Text(
                          text = it.toString(),
                          color = Color(0xff56434d),
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