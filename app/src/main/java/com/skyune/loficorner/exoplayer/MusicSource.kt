package com.skyune.loficorner.exoplayer

import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat
import com.skyune.loficorner.exoplayer.library.extension.*
import com.skyune.loficorner.model.Data
import com.skyune.loficorner.utils.Constants.BASE_URL
import com.skyune.loficorner.utils.Constants.appName

class MusicSource {

    private val onReadyListener = mutableListOf<(Boolean) -> Unit>()

    var songs: List<MediaMetadataCompat> = emptyList()

    var playlist: List<Data> = emptyList()

    fun fetchMediaData() {
        state = State.STATE_INITIALIZING

        songs = updateCatalog(playlist)

        state = State.STATE_INITIALIZED
    }

    private fun updateCatalog(playlist: List<Data>): List<MediaMetadataCompat> {
        val mediaMetadataCompat = playlist.map { song ->
            MediaMetadataCompat.Builder().from(song).build()
        }.toList()
        mediaMetadataCompat.forEach {
            it.description.extras?.putAll(it.bundle)
        }
        return mediaMetadataCompat
    }

    private fun MediaMetadataCompat.Builder.from(song: Data): MediaMetadataCompat.Builder {
        artist = song.user?.name
        id = song.id
        title = song.title
        displayIconUri = song.artwork?.small
        mediaUri = setSongUrl(song.id)
        //albumArtUri = song.songIconList.songImageURL480px
        displaySubtitle = song.title
        displayDescription = song.description
        //isFavorite = song.isFavorite.toString()
        duration = 120

        downloadStatus = MediaDescriptionCompat.STATUS_NOT_DOWNLOADED

        return this
    }

    private var state: State = State.STATE_CREATED
        set(value) {
            if (value == State.STATE_INITIALIZED || value == State.STATE_ERROR) {
                synchronized(onReadyListener) {
                    field = value
                    onReadyListener.forEach { listener ->
                        listener(state == State.STATE_INITIALIZED)
                    }
                }
            } else {
                field = value
            }
        }

    fun whenReady(performAction: (Boolean) -> Unit): Boolean {
        return if (state == State.STATE_CREATED || state == State.STATE_INITIALIZING) {
            onReadyListener += performAction
            false
        } else {
            performAction(state != State.STATE_ERROR)
            true
        }
    }

    private fun setSongUrl(songId: String): String {
        return "${BASE_URL}/v1/tracks/${songId}/stream?app_name=$appName"
    }
}

enum class State {
    STATE_CREATED,
    STATE_INITIALIZING,
    STATE_INITIALIZED,
    STATE_ERROR
}