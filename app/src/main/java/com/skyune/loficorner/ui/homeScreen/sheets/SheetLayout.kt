package com.rld.justlisten.android.ui.bottombars.sheets

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.graphics.painter.Painter

@Composable
fun SheetLayout(
    currentScreen: BottomSheetScreen,
    onCloseBottomSheet: () -> Unit,
    title: String,
    author: String,
    mutablePainter: MutableState<Painter?>,
    openSheet: (BottomSheetScreen) -> Unit,
    clickedToAddSongToPlaylist: (String, String?, List<String>) -> Unit
) {
//    when (currentScreen) {
//        BottomSheetScreen.AddPlaylist -> {
//            AddPlaylistOption(
//                title,
//                mutablePainter,
//                addPlaylistList,
//                onAddPlaylistClicked,
//                clickedToAddSongToPlaylist,
//                events
//            )
//            getLatestPlaylist()
//        }
//        BottomSheetScreen.More -> PlayBarMoreAction(
//            title,
//            mutablePainter,
//        ) {
//            onCloseBottomSheet()
//            openSheet(BottomSheetScreen.AddPlaylist)
//        }
//    }
}