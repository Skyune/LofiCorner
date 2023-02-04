package com.skyune.loficorner.utils

import android.support.v4.media.MediaBrowserCompat
import com.skyune.loficorner.exoplayer.MusicServiceConnection
import com.skyune.loficorner.model.Data
import com.skyune.loficorner.model.Weather
import retrofit2.Call


fun playMusicFromId(
    musicServiceConnection: MusicServiceConnection,
    playlist: List<Data>,
    songId: String,
    isPlayerReady: Boolean
) {
    if (isPlayerReady) {
        musicServiceConnection.transportControls.playFromMediaId(songId, null)
    } else {
        playMusic(musicServiceConnection, playlist, isPlayerReady, songId)
    }
}

fun playMusic(
    musicServiceConnection: MusicServiceConnection,
    playlist: List<Data>,
    isPlayerReady: Boolean,
    playFromId: String = ""
) {
    if (!isPlayerReady) {
        musicServiceConnection.updatePlaylist(playlist)
        musicServiceConnection.subscribe(
            Constants.CLICKED_PLAYLIST,
            object : MediaBrowserCompat.SubscriptionCallback() {})
    }
    if (playFromId != "") {
        musicServiceConnection.transportControls.playFromMediaId(playFromId, null)
    }
}
