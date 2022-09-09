package com.anshmidt.multialarm.musicplayer

import android.content.Context
import android.content.Context.AUDIO_SERVICE
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.PowerManager
import android.util.Log


class MusicPlayer(private val context: Context) : IMusicPlayer {

    private var mediaPlayer: MediaPlayer? = null
    // Volume level is changed only when music is playing, after that the volume is set to the initial value.
    private var initialVolumeLevel: VolumeLevel? = null
    private val volumeManager = VolumeManager(context)


    override fun play(ringtoneUri: Uri, musicVolumePercents: Int) {
        Log.d(TAG, "Music started")
        initialVolumeLevel = volumeManager.getSystemVolumeLevel()
        val volumeLevel = volumeManager.percentsToVolumeLevel(musicVolumePercents)
        volumeManager.setSystemVolumeLevel(volumeLevel)
        mediaPlayer = MediaPlayer().apply {
            setAudioAttributes(getAudioAttributes())
            setWakeMode(context, PowerManager.PARTIAL_WAKE_LOCK)
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
        initialVolumeLevel?.let {
            volumeManager.setSystemVolumeLevel(it)
        }
    }



    companion object {
        val TAG = MusicPlayer::class.java.simpleName
    }

    /**
     * VolumeLevel value range: 0..15
     */
    data class VolumeLevel(val value: Int)

    class VolumeManager(context: Context) {
        private val audioManager = context.getSystemService(AUDIO_SERVICE) as AudioManager

        fun getSystemVolumeLevel(): VolumeLevel {
            return VolumeLevel(audioManager.getStreamVolume(AudioManager.STREAM_ALARM))
        }

        fun setSystemVolumeLevel(systemVolumeLevel: VolumeLevel) {
            audioManager.setStreamVolume(AudioManager.STREAM_ALARM, systemVolumeLevel.value, 0)
        }

        fun percentsToVolumeLevel(musicVolumePercents: Int): VolumeLevel {
            val maxSystemVolumeLevel = audioManager.getStreamMaxVolume(AudioManager.STREAM_ALARM)
            val volumeLevelValue: Float = musicVolumePercents.toFloat() * maxSystemVolumeLevel / 100
            return VolumeLevel(volumeLevelValue.toInt())
        }
    }
}