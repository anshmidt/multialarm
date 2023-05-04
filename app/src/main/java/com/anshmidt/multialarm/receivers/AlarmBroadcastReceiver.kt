package com.anshmidt.multialarm.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import com.anshmidt.multialarm.logging.Log
import com.anshmidt.multialarm.services.MusicService


class AlarmBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d(TAG, "onReceive")
        if (context == null) {
            Log.d(TAG, "context is null")
            return
        }

        // music starts playing when view appears, and stops playing when notification dismissed or clicked, or button clicked on activity
        startMusicService(context)
    }

    private fun startMusicService(context: Context) {
        val serviceIntent = Intent(context, MusicService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(serviceIntent)
        } else {
            context.startService(serviceIntent)
        }
    }

    companion object {
        private val TAG = AlarmBroadcastReceiver::class.java.simpleName
    }
}