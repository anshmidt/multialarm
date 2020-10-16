package com.anshmidt.multialarm.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import com.anshmidt.multialarm.notifications.dismissalarm.NotificationHelper
import com.anshmidt.multialarm.view.DismissAlarmActivity
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject


class AlarmBroadcastReceiver : BroadcastReceiver(), KoinComponent {

    private val dismissAlarmNotificationHelper: NotificationHelper by inject()

    override fun onReceive(context: Context?, intent: Intent?) {
        // it's not allowed to start activity from the background since Android Q
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
            dismissAlarmNotificationHelper.showNotification()
            //TODO start music even if screen is on
        } else {
            startDismissAlarmActivity(context)
        }
    }

    private fun startDismissAlarmActivity(context: Context?) {
        if (context == null) return
        val dismissAlarmIntent = Intent(context, DismissAlarmActivity::class.java)
        context.startActivity(dismissAlarmIntent)
    }
}