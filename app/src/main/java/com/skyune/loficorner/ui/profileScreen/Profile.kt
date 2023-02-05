@file:OptIn(ExperimentalFoundationApi::class)

package com.skyune.loficorner.ui.profileScreen

import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.skyune.loficorner.exoplayer.MusicServiceConnection
import com.skyune.loficorner.model.Data
import com.skyune.loficorner.model.Weather
import com.skyune.loficorner.ui.homeScreen.WeatherItem
import com.skyune.loficorner.utils.playMusicFromId
import com.skyune.loficorner.viewmodels.ProfileViewModel
import retrofit2.Call
import retrofit2.Response


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
            .fillMaxHeight()
            .background(MaterialTheme.colors.primary),
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
            .background(Color(0xFFC1AEB9)),
        contentAlignment = Alignment.Center,

        ) {



        if(!isLoaded.value && list.isEmpty())
        {
            profileViewModel.ShowPlaylistsSongs(isLoaded = isLoaded)

        }
        if(list.size>10)
        {

            LazyColumn(modifier = Modifier
                .padding(2.dp)
                .simpleVerticalScrollbar(listState), contentPadding = PaddingValues(1.dp), state = listState) {
                //item { RoomImagesRow() }
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
    Surface(modifier = Modifier
        .padding(2.dp)
        .fillMaxWidth()
        .clickable { onItemClicked() },
        color = Color(0xFFCDBEC8),
        shape = RoundedCornerShape(4.dp)) {
        Row {
            Row {
                Image(
                    rememberAsyncImagePainter(
                        ImageRequest.Builder(LocalContext.current)
                            .diskCachePolicy(CachePolicy.DISABLED)
                            .data(data = item.artwork?.small)
                            .build()
                    ),
                    modifier = Modifier.size(150.dp),
                    contentScale = ContentScale.FillBounds,
                    contentDescription = null
                )

            Column(modifier = Modifier.height(150.dp)) {
                item.playlist_name?.let { Text(text = it) }
                item.user?.name?.let { Text(text = it) }

            }
        }
    }
}}

@Composable
@Preview
fun ProfileScreenPreview() {
}