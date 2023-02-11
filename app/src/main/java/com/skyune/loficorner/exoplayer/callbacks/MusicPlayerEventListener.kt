package com.skyune.loficorner.exoplayer.callbacks

import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.widget.Toast
import androidx.media.MediaBrowserServiceCompat
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.PlaybackException
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.Player.DISCONTINUITY_REASON_AUTO_TRANSITION
import com.skyune.loficorner.exoplayer.MusicNotificationManager
import com.skyune.loficorner.exoplayer.MusicService
import com.skyune.loficorner.exoplayer.MusicService.Companion.songHasRepeated
import com.skyune.loficorner.exoplayer.library.extension.toMediaItem
import com.skyune.loficorner.exoplayer.utils.Constants

class MusicPlayerEventListener(
    private val musicService: MusicService,
    private val notificationManager: MusicNotificationManager,
    private val exoPlayer: ExoPlayer,
    val songHasRepeated: () -> Unit
) : Player.Listener {
    override fun onPlayWhenReadyChanged(playWhenReady: Boolean, reason: Int) {
        super.onPlayWhenReadyChanged(playWhenReady, reason)
        if (reason == Player.STATE_ENDED && !playWhenReady) {
            musicService.stopForeground(false)
            musicService.isForegroundService = false
        }
    }


    override fun onPlayerError(error: PlaybackException) {
        super.onPlayerError(error)
        Toast.makeText(
            musicService,
            "Song could not be played, skipping",
            Toast.LENGTH_SHORT
        ).show()
        var i = musicService.exoPlayer.currentMediaItemIndex
        i++
        if(i <= musicService.musicSource.songs.size)
        if (musicService.musicSource.songs.isNotEmpty()) {
            //this is not right..?
            //nvm it actually works... I AM KURWA AWESOME
            //needs shuffle too tho
            //TODO polish it for all edge cases
            preparePlayer(musicService.musicSource.songs, musicService.musicSource.songs[i], true)
        }
    }



    private fun preparePlayer(
        songs: List<MediaMetadataCompat>,
        itemToPlay: MediaMetadataCompat?,
        playNow: Boolean
    ) {
        val currentSongIndex = songs.indexOf(itemToPlay)

        musicService.exoPlayer.playWhenReady = playNow
        musicService.exoPlayer.stop()
        musicService.exoPlayer.setMediaItems(songs.map {
            it.toMediaItem()}, currentSongIndex, 0L)
        musicService.exoPlayer.prepare()
        exoPlayer.seekTo(0L)
    }
    override fun onPlaybackStateChanged(playbackState: Int) {
        when (playbackState) {
            Player.STATE_BUFFERING,
            Player.STATE_READY -> {
                notificationManager.showNotification(musicService.exoPlayer)
            }
            else -> {
                notificationManager.hideNotification()
            }
        }
    }

    override fun onPositionDiscontinuity(
        oldPosition: Player.PositionInfo,
        newPosition: Player.PositionInfo,
        reason: Int
    ) {

        if (exoPlayer.repeatMode == Player.REPEAT_MODE_ONE) {
            if (reason == DISCONTINUITY_REASON_AUTO_TRANSITION) {
                songHasRepeated()
            }
        }
    }
}