package com.anshmidt.multialarm.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.anshmidt.multialarm.notifications.dismissalarm.NotificationHelper
import com.anshmidt.multialarm.services.MusicService
import com.anshmidt.multialarm.view.activities.DismissAlarmActivity
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject


class AlarmBroadcastReceiver : BroadcastReceiver(), KoinComponent {

    private val dismissAlarmNotificationHelper: NotificationHelper by inject()

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null) return

        // music starts playing when view appears, and stops playing when notification dismissed or clicked, or button clicked on activity
        startMusicService(context)

        //showDismissView(context)
    }

    private fun showDismissView(context: Context) {
        // it's not allowed to start activity from the background since Android Q
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
            Log.d(TAG, "Showing notification")
            dismissAlarmNotificationHelper.showNotification()
        } else {
            startDismissAlarmActivity(context)
        }
    }

    private fun startDismissAlarmActivity(context: Context) {
        val activityIntent = Intent(context, DismissAlarmActivity::class.java)
        context.startActivity(activityIntent)
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