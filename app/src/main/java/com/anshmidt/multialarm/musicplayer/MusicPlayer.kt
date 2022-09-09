package com.anshmidt.multialarm.musicplayer

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import android.os.PowerManager
import android.util.Log


class MusicPlayer(private val context: Context) : IMusicPlayer {

    private var mediaPlayer: MediaPlayer? = null


    override fun play(ringtoneUri: Uri, musicVolumePercents: Int) {
        Log.d(TAG, "Music started")
        val musicVolume: Float = VolumeConverter.percentsToFloat(musicVolumePercents)
        mediaPlayer = MediaPlayer().apply {
            setAudioAttributes(getAudioAttributes())
            setWakeMode(context, PowerManager.PARTIAL_WAKE_LOCK)
            setVolume(musicVolume, musicVolume)
            setDataSource(context, ringtoneUri)
            prepare()
            start()
        }
    }

    private fun getAudioAttributes() = AudioAttributes.Builder()
        .setUsage(AudioAttributes.USAGE_ALARM)
        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
        .build()

    override fun stop() {
        Log.d(TAG, "Music stopped")
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }

    companion object {
        val TAG = MusicPlayer::class.java.simpleName
    }

    object VolumeConverter {
        fun percentsToFloat(musicVolumePercents: Int): Float {
            return musicVolumePercents.toFloat() / 100
        }
    }
}