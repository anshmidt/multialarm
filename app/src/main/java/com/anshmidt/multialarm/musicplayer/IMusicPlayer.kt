package com.anshmidt.multialarm.musicplayer

import android.net.Uri

interface IMusicPlayer {
    fun play(songUri: Uri)
    fun stop()
}