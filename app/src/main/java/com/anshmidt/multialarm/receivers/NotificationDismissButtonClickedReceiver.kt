package com.anshmidt.multialarm.receivers

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.anshmidt.multialarm.services.MusicService


class NotificationDismissButtonClickedReceiver : BroadcastReceiver() {

    companion object {
        val INTENT_KEY_NOTIFICATION_ID = "notificationId"
        private val defaultNotificationId = 0
        private val TAG = NotificationDismissButtonClickedReceiver::class.java.simpleName
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent == null || context == null) return

        val notificationId = intent.getIntExtra(INTENT_KEY_NOTIFICATION_ID, defaultNotificationId)
        cancelNotification(notificationId = notificationId, context = context)
        stopMusicService(context)
    }

    private fun cancelNotification(notificationId: Int, context: Context) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        Log.d(TAG, "Canceling notification with id=$notificationId")
        notificationManager.cancel(notificationId)
    }

    private fun stopMusicService(context: Context) {
        val intent = Intent(context, MusicService::class.java)
        context.stopService(intent)
    }

}