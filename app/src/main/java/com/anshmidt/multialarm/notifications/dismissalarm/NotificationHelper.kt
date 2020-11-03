package com.anshmidt.multialarm.notifications.dismissalarm

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.anshmidt.multialarm.R
import com.anshmidt.multialarm.receivers.NotificationDismissButtonClickedReceiver
import com.anshmidt.multialarm.receivers.NotificationDismissedBySwipeReceiver
import com.anshmidt.multialarm.view.DismissAlarmActivity
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter
import java.util.concurrent.TimeUnit


class NotificationHelper(val context: Context, val notificationParams: NotificationParams) {

    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    fun showNotification() {
        setNotificationChannel()

        val notificationId = notificationParams.notificationId
        val notification = buildNotification(notificationId)

        notificationManager.notify(notificationId, notification)
    }

    private fun getChannelName(): String {
        return context.getString(R.string.app_name)
    }

    private fun setNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val notificationChannel = NotificationChannel(NotificationParams.CHANNEL_ID, getChannelName(), importance)
            notificationManager.createNotificationChannel(notificationChannel)
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

    private fun buildNotification(notificationId: Int): Notification {
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
        val formatter = DateTimeFormatter.ofPattern("HH:mm")
        val currentTime = LocalDateTime.now()
        val formattedDateTime = currentTime.format(formatter)
        return formattedDateTime
    }

    fun cancelNotification() {
        notificationManager.cancelAll()
    }


}