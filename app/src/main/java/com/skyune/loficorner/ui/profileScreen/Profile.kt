@file:OptIn(ExperimentalFoundationApi::class)

package com.skyune.loficorner.ui.profileScreen

import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.geometry.CornerRadius
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
import com.skyune.loficorner.exoplayer.isPlaying
import com.skyune.loficorner.model.Data
import com.skyune.loficorner.ui.profileScreen.components.RoomImagesRow
import com.skyune.loficorner.ui.theme.Theme
import com.skyune.loficorner.ui.utils.GifImage
import com.skyune.loficorner.viewmodels.ProfileViewModel
import kotlinx.coroutines.*


@Composable
fun ProfileScreen(
    profileViewModel: ProfileViewModel = hiltViewModel(),
    musicServiceConnection: MusicServiceConnection,
    bottomBarState: MutableState<Boolean>,
    isLoaded: MutableState<Boolean>,
    onToggleTheme: (Theme) -> Unit,
    topBarState: MutableState<Boolean>,
    title: String,
    listState: LazyListState,
    list: List<Data>,
    Sleepylist: List<Data>,
    Jazzylist: List<Data>
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        contentAlignment = Alignment.TopCenter
    ) {



        val selectedButtonIndex = remember { mutableStateOf(profileViewModel.selectedButtonIndexId.value) }


        val list by profileViewModel.allWords.observeAsState(listOf())
        val Sleepylist by profileViewModel.allSleepy.observeAsState(listOf())
        val Jazzylist by profileViewModel.allJazzy.observeAsState(listOf())

        ShowData(profileViewModel, musicServiceConnection, bottomBarState, isLoaded,PlaylistPicker(list,Sleepylist,Jazzylist,selectedButtonIndex),onToggleTheme,selectedButtonIndex,title,listState)
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ShowData(
    profileViewModel: ProfileViewModel,
    musicServiceConnection: MusicServiceConnection,
    bottomBarState: MutableState<Boolean>,
    isLoaded: MutableState<Boolean>,
    list: List<Data>,
    onToggleTheme: (Theme) -> Unit,
    selectedButtonIndex: MutableState<Int>,
    title: String,
    listState: LazyListState
) {
    val isPlayerReady: MutableState<Boolean> = remember{
        derivedStateOf {
            mutableStateOf(false)
        }
    }.value



    var currentIndex by remember {
        mutableStateOf(0)
    }


    val isLoading: MutableState<Boolean> = remember{
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
                    start = Offset(250f, 300f),
                    end = Offset(900f, 1900.5f)
                )
            ),
        contentAlignment = Alignment.TopCenter,
        ) {

        val showAll = remember { mutableStateOf(false) }
        if(profileViewModel.allWords.value?.isEmpty() == true)
        {
            profileViewModel.ShowPlaylistsSongs(isLoaded = isLoaded, songType = "Chill")
        }
        isLoading.value = list.size > 5

            val selectedItemId = remember { mutableStateOf(profileViewModel.selectedItemId.value) }

            LazyColumn(modifier = Modifier
                .simpleVerticalScrollbar(listState), contentPadding = PaddingValues(6.dp), state = listState, verticalArrangement = Arrangement.Top) {
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
                                .padding(10.dp, 4.dp, 0.dp, 4.dp)
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
                        .align(Alignment.TopCenter)
                        .wrapContentSize()
                        .padding(10.dp, 0.dp, 0.dp, 6.dp)

                    )}

                stickyHeader {
                    val backgroundModifier = if (listState.firstVisibleItemIndex > 0) {
                        Modifier.background(
                            brush = Brush.linearGradient(
                                0f to MaterialTheme.colors.background,
                                1f to MaterialTheme.colors.onBackground,
                                start = Offset(250f, 300f),
                                end = Offset(900f, 1900.5f)
                            )
                        )
                    } else {
                        Modifier
                    }
                    Column {
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .then(backgroundModifier),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            MusicSelectionButtons(profileViewModel, selectedButtonIndex, isLoaded, listState.firstVisibleItemIndex > 0)
                        }
                    }
                }

                        item {
                            Row(
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.Top,
                                modifier = Modifier.padding(0.dp, 10.dp, 0.dp, 0.dp)
                            ) {
                                Text(
                                    text = "Rooms",
                                    color = Color(MaterialTheme.colors.surface.value),
                                    textAlign = TextAlign.End,
                                    lineHeight = 26.sp,
                                    style = TextStyle(

                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold
                                    ),
                                    modifier = Modifier
                                        .padding(10.dp, 0.dp, 0.dp, 0.dp)
                                        .height(42.dp)

                                )

                                Text(
                                    text = "Show All",
                                    color = Color(MaterialTheme.colors.onSurface.value),
                                    textAlign = TextAlign.End,
                                    lineHeight = 20.sp,
                                    style = TextStyle(
                                        fontSize = 20.sp
                                    ),
                                    modifier = Modifier
                                        .padding(0.dp, 0.dp, 10.dp, 0.dp)
                                        .weight(0.2f)
                                        .height(42.dp)
                                        .clickable {
                                            showAll.value = !showAll.value
                                        }
                                )
                            }
                            RoomImagesRow(showAll = showAll.value, onToggleTheme, profileViewModel)
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
                        .padding(10.dp, 0.dp, 10.dp, 5.dp)
                        .wrapContentSize())
                }



                if(isLoading.value) {
                    items(list, key = {
                        it.id
                    }) { item ->
                        WeatherItem(
                            item = item,
                            isLoading = profileViewModel.isLoading.value &&  selectedItemId.value == item.id,
                            isSelected = profileViewModel.isSelected.value && profileViewModel.currentPlaylistSelected.value == item.id && musicServiceConnection.playbackState.value?.isPlaying == true,
                            onItemClicked = {
                                selectedItemId.value = item.id
                                profileViewModel.selectItem(item.id)
                                profileViewModel.isLoading.value = true
                                Log.d("TAG", "ShowData: ${item}")
                                profileViewModel.PlayPlaylist(
                                    item,
                                    isPlayerReady,
                                    musicServiceConnection,
                                )
                            }
                        )
                    }
                }
                if (!isLoading.value) {
                item {
                    Column(Modifier.fillMaxHeight(),Arrangement.Center,Alignment.CenterHorizontally) {
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .fillMaxHeight()
                                .padding(top = 10.dp),
                            Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            CircularProgressIndicator(
                                Modifier.fillParentMaxSize(0.1f),
                                MaterialTheme.colors.surface
                            )
                        }
                    }
                }
            }
        }
    }
 }


@Composable
fun MusicSelectionButtons(
    profileViewModel: ProfileViewModel,
    selectedButtonIndex: MutableState<Int>,
    isLoaded: MutableState<Boolean>,
    isOnTop: Boolean
) {

    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        BouncingMusicSelectionButton(R.drawable.iconoir_coffee_cup__4_, "Chill", selectedButtonIndex.value == 0,isOnTop,Modifier.weight(1f)) {
            profileViewModel.selectButtonIndex(0)
            selectedButtonIndex.value = 0
            if(profileViewModel.allWords.value?.isEmpty() == true)
            {
                profileViewModel.ShowPlaylistsSongs(isLoaded = isLoaded, songType = "Chill")
            }
        }
        BouncingMusicSelectionButton(
            R.drawable.fluent_sleep_20_filled__1_,
            "Sleepy",
            selectedButtonIndex.value == 1,
            isOnTop,
            Modifier.weight(1f)
        ) { profileViewModel.selectButtonIndex(1)
            selectedButtonIndex.value = 1
            if(profileViewModel.allSleepy.value?.isEmpty() == true)
            {
                profileViewModel.ShowPlaylistsSongs(isLoaded = isLoaded, songType = "Sleepy")
            }
        }
        BouncingMusicSelectionButton(
            R.drawable.fluent_emoji_high_contrast_saxophone__1_,
            "Jazzy",
            selectedButtonIndex.value == 2,
            isOnTop,
            Modifier.weight(1f)
        ) { profileViewModel.selectButtonIndex(2)
            selectedButtonIndex.value = 2
            if(profileViewModel.allJazzy.value?.isEmpty() == true)
            {
                profileViewModel.ShowPlaylistsSongs(isLoaded = isLoaded, songType = "Jazzy")
            }
        }
    }
}

@Composable
fun BouncingMusicSelectionButton(
    ImageId: Int,
    Title: String,
    isSelected: Boolean,
    isOnTop: Boolean,
    modifier: Modifier,
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
        modifier = modifier
            .clickable(onClick = onClick)
            .padding(0.dp, 0.dp, 0.dp, 0.dp)
            .fillMaxSize()
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioLowBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
            .scale(scale)
    ) {
        MusicSelectionButton(ImageId,Title, isSelected, onClick,isOnTop)
    }
}
@Composable
private fun MusicSelectionButton(
    ImageId: Int,
    Title: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    isOnTop: Boolean
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(6.dp)
    ) {
        Box(
            modifier = Modifier
                .wrapContentSize()
                .clip(shape = RoundedCornerShape(15.dp))
                .clickable(onClick = onClick)
                .background(
                    if (!isSelected) {
                        Brush.linearGradient(
                            0f to Color(MaterialTheme.colors.secondary.value),
                            1f to Color(MaterialTheme.colors.secondaryVariant.value),
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
                ),
                contentAlignment = Alignment.Center
        ) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Card(
                    modifier = Modifier
                        .fillMaxSize(),
                    shape = CircleShape,
                    backgroundColor = Color.Transparent,
                    elevation = 0.dp
                ) {}
            }
            Column(Modifier.padding(top = 6.dp,bottom = 6.dp),verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                if (!isOnTop) {
                    Image(
                        rememberAsyncImagePainter(
                            ImageRequest.Builder(LocalContext.current)
                                .diskCachePolicy(CachePolicy.ENABLED)
                                .data(data = ImageId)
                                .build()
                        ),
                        modifier = Modifier
                            .size(60.dp)
                            .composed {
                                alpha(if (isSelected) 1f else 0.7f)
                            },
                        contentScale = ContentScale.FillBounds,
                        contentDescription = null
                    )
                }
                    Text(text = Title, Modifier.padding(4.dp), color = MaterialTheme.colors.error)
            }
        }
    }
}

fun PlaylistPicker(
    list: List<Data>,
    Sleepylist: List<Data>,
    Jazzylist: List<Data>,
    selectedButtonIndex: MutableState<Int>
): List<Data> {
    if (selectedButtonIndex.value==0) {
        return list
    }
    else if (selectedButtonIndex.value == 1)
    {
        return Sleepylist
    }
    else if (selectedButtonIndex.value ==2)
    {
        return Jazzylist
    }
    return list
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

    val currentColor = MaterialTheme.colors.onSecondary
    val cornerRadius = 8.dp

        drawWithContent {
        drawContent()

        val firstVisibleElementIndex = state.layoutInfo.visibleItemsInfo[1]?.index
        val needDrawScrollbar = state.isScrollInProgress || alpha > 0.0f

        // Draw scrollbar if scrolling or if the animation is still running and lazy column has content
        if (needDrawScrollbar && firstVisibleElementIndex != null) {
            val elementHeight = this.size.height / state.layoutInfo.totalItemsCount
            val scrollbarOffsetY = firstVisibleElementIndex * elementHeight
            val scrollbarHeight = state.layoutInfo.visibleItemsInfo.size * elementHeight

            val padding = 4.dp.toPx()
            val topLeft = Offset(this.size.width - width.toPx() - padding, scrollbarOffsetY)

            drawRoundRect(
                color = currentColor,
                topLeft = topLeft,
                size = Size(width.toPx(), scrollbarHeight),
                cornerRadius = CornerRadius(cornerRadius.toPx()), // add corner radius
                alpha = alpha,
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
fun WeatherItem(item: Data, onItemClicked: () -> Unit, isSelected: Boolean, isLoading: Boolean) {
Box(modifier = Modifier
    .wrapContentWidth()
    .wrapContentHeight()
    .padding(start = 6.dp, end = 6.dp)) {
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
                Brush.linearGradient(
                    0f to Color(MaterialTheme.colors.primary.value),
                    1f to Color(MaterialTheme.colors.primaryVariant.value),
                    start = Offset(0f, 0f),
                    end = Offset(20f, 500f)
                )
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
        Row( verticalAlignment = Alignment.CenterVertically) {
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
                          .padding(0.dp)
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
                      elevation = 7.dp,
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

}
            Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                val targetAlpha = if (isSelected) 1f else 0f
                val duration = if (isSelected) 300 else 800

                val alpha by animateFloatAsState(
                    targetValue = targetAlpha,
                    animationSpec = tween(durationMillis = duration)

                )

                if (alpha != 0f) {
                    GifImage(
                        Modifier
                            .size(30.dp)
                            .alpha(alpha),
                        R.drawable.audiowave,
                        colorFilter = ColorFilter.tint(MaterialTheme.colors.surface)
                    )
                }
                if(!isSelected) {
                    if (isLoading)
                    {
                        CircularProgressIndicator()
                    }
                }
            }

        }

    }

}}

@Composable
fun ProfileScreenPreview() {
}