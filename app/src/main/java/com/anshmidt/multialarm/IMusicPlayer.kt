package com.anshmidt.multialarm

import android.net.Uri

interface IMusicPlayer {
    fun play(songUri: Uri)
    fun stop()
}