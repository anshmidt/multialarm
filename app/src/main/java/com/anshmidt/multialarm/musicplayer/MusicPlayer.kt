package com.anshmidt.multialarm.musicplayer

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import android.os.PowerManager


class MusicPlayer(val context: Context) : IMusicPlayer {

    private var mediaPlayer: MediaPlayer? = null

    override fun play(songUri: Uri) {
        mediaPlayer = MediaPlayer().apply {
            setAudioAttributes(getAudioAttributes())
            setWakeMode(context, PowerManager.PARTIAL_WAKE_LOCK)
            setDataSource(context, songUri)
            prepare()
            start()
        }
    }

    private fun getAudioAttributes() = AudioAttributes.Builder()
        .setUsage(AudioAttributes.USAGE_ALARM)
        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
        .build()

    override fun stop() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}