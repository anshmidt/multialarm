package com.anshmidt.multialarm.receivers

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent


class NotificationDismissButtonClickedReceiver : BroadcastReceiver() {

    companion object {
        val INTENT_KEY_NOTIFICATION_ID = "notificationId"
        private val defaultNotificationId = 0
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent == null || context == null) return

        val notificationId = intent.getIntExtra(INTENT_KEY_NOTIFICATION_ID, defaultNotificationId)
        cancelNotification(notificationId = notificationId, context = context)
    }

    private fun cancelNotification(notificationId: Int, context: Context) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(notificationId)
    }

}