package com.anshmidt.multialarm.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.anshmidt.multialarm.services.MusicService

class NotificationDismissedBySwipeReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null) return
        stopMusicService(context)
    }

    private fun stopMusicService(context: Context) {
        val intent = Intent(context, MusicService::class.java)
        context.stopService(intent)
    }
}