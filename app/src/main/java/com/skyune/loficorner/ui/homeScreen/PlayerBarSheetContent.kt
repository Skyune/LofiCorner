package com.skyune.loficorner.ui.homeScreen

import androidx.activity.compose.BackHandler
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import com.rld.justlisten.android.ui.bottombars.sheets.BottomSheetScreen
import com.rld.justlisten.android.ui.bottombars.sheets.SheetLayout
import com.skyune.loficorner.exoplayer.MusicServiceConnection
import com.skyune.loficorner.exoplayer.library.extension.author
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch

@ExperimentalMaterialApi
@OptIn(InternalCoroutinesApi::class)
@ExperimentalCoilApi
@Composable
fun PlayerBarSheetContent(
    isExtended: Boolean,
    onSkipNextPressed: () -> Unit,
    musicServiceConnection: MusicServiceConnection,
    currentFraction: Float,
    //events: Events
) {
    val songIcon by remember { derivedStateOf { musicServiceConnection.currentPlayingSong.value?.description?.iconUri.toString() } }
    val title by remember { derivedStateOf {
        musicServiceConnection.currentPlayingSong.value?.description?.title.toString()  } }
    val artist by remember { derivedStateOf {
        musicServiceConnection.currentPlayingSong.value?.description?.subtitle.toString() } }

    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberBottomSheetState(initialValue = BottomSheetValue.Collapsed)
    )

    val mutablePainter = remember { mutableStateOf<Painter?>(null) }

    val coroutines = rememberCoroutineScope()

    var currentBottomSheet: BottomSheetScreen? by remember { mutableStateOf(null) }

    val closeSheet: () -> Unit = {
        coroutines.launch {
            if (scaffoldState.bottomSheetState.isExpanded) {
                scaffoldState.bottomSheetState.collapse()
            }
        }
    }

    val openSheet: (BottomSheetScreen) -> Unit = {
        coroutines.launch {
            currentBottomSheet = it
            scaffoldState.bottomSheetState.expand()
        }
    }

    if (scaffoldState.bottomSheetState.isCollapsed)
        currentBottomSheet = null

    if (scaffoldState.bottomSheetState.isExpanded) {
        BackHandler {
            closeSheet()
        }
    }

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetContent = {
            currentBottomSheet?.let { currentSheet ->
                SheetLayout(
                    currentSheet,
                    closeSheet,
                    title,
                    artist,
                    mutablePainter,
                    openSheet,

                    )
                //addPlaylistList,
                //onAddPlaylistClicked,
                //getLatestPlaylist,
                { playlistTitle, playlistDescription, songList ->
                    closeSheet()
                    //clickedToAddSongToPlaylist(playlistTitle, playlistDescription, songList)
                }
            }
        },
        sheetPeekHeight = (-1).dp
    ) {
        PlayerBottomBar(
//            onCollapsedClicked = onCollapsedClicked,
//            bottomPadding = bottomPadding,
            currentFraction = currentFraction,
            isExtended = isExtended,
            songIcon = songIcon, title = title,
            artist = artist,
            musicServiceConnection = musicServiceConnection,
            onSkipNextPressed = onSkipNextPressed,
            onMoreClicked = {
                openSheet(BottomSheetScreen.More)
            },
            onBackgroundClicked = {
                if (scaffoldState.bottomSheetState.isExpanded) {
                    closeSheet()
                }
            },
        ) { painter ->
            mutablePainter.value = painter
        }
        //playBarMinimizedClicked = playBarMinimizedClicked,
//            onFavoritePressed = onFavoritePressed,
//            newDominantColor = newDominantColor

    }
}

