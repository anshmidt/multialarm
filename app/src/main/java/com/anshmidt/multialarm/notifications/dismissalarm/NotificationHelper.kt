package com.anshmidt.multialarm.notifications.dismissalarm

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.anshmidt.multialarm.R
import com.anshmidt.multialarm.data.TimeFormatter
import com.anshmidt.multialarm.receivers.NotificationDismissButtonClickedReceiver
import com.anshmidt.multialarm.receivers.NotificationDismissedBySwipeReceiver
import com.anshmidt.multialarm.view.activities.DismissAlarmActivity
import org.threeten.bp.LocalTime


class NotificationHelper(val context: Context, val notificationParams: NotificationParams) {

    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    fun showNotification() {
        createNotificationChannel()

        val notificationId = notificationParams.notificationId
        val notification = buildNotification(notificationId)

        notificationManager.notify(notificationId, notification)
    }

    private fun getChannelName(): String {
        return context.getString(R.string.app_name)
    }

    fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val notificationChannel = NotificationChannel(NotificationParams.CHANNEL_ID, getChannelName(), importance)
            notificationChannel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            notificationManager.createNotificationChannel(notificationChannel)
            Log.d(TAG, "Notification channel created")
        }
    }

    private fun createFullScreenIntent(): PendingIntent {
        val fullScreenIntent = Intent(context, DismissAlarmActivity::class.java)
        val fullScreenPendingIntent = PendingIntent.getActivity(
                context,
                0,
                fullScreenIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
        )
        return fullScreenPendingIntent
    }

    private fun createOnNotificationDismissedIntent(): PendingIntent {
        val intent = Intent(context, NotificationDismissedBySwipeReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
                context.applicationContext,
                0,
                intent,
                0
        )
        return pendingIntent
    }

    fun buildNotification(notificationId: Int): Notification {
        Log.d(TAG, "Building notification with id=$notificationId")
        val notificationBuilder = NotificationCompat.Builder(context, NotificationParams.CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_alarm_on_notification)
                .setContentTitle(context.getString(R.string.dismiss_alarm_notification_title))
                .setContentText(context.getString(R.string.dismiss_alarm_notification_text, getCurrentTime()))
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setAutoCancel(true)
                .addAction(getDismissNotificationAction(notificationId = notificationId))
                .setFullScreenIntent(createFullScreenIntent(), true)
                .setDeleteIntent(createOnNotificationDismissedIntent())
        return notificationBuilder.build()
    }

    private fun getDismissNotificationAction(notificationId: Int): NotificationCompat.Action? {
        val dismissIntent = Intent(context, NotificationDismissButtonClickedReceiver::class.java)
        dismissIntent.putExtra(NotificationDismissButtonClickedReceiver.INTENT_KEY_NOTIFICATION_ID, notificationId)
        val dismissNotificationPendingIntent = PendingIntent.getBroadcast(context, 0, dismissIntent, PendingIntent.FLAG_CANCEL_CURRENT)
        return NotificationCompat.Action.Builder(
                0,
                context.getString(R.string.dismiss_alarm_notification_dismiss_button_title),
                dismissNotificationPendingIntent
        ).build()
    }

    private fun getCurrentTime(): String {
        val currentTime = LocalTime.now()
        val displayableTime = TimeFormatter.getDisplayableTime(currentTime)
        return displayableTime
    }

    fun cancelNotification() {
        Log.d(TAG, "Canceling all notifications")
        notificationManager.cancelAll()
    }

    companion object {
        val TAG = NotificationHelper::class.java.simpleName
    }

}