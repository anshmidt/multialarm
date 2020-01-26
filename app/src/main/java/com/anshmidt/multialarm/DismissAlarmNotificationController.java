package com.anshmidt.multialarm;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.anshmidt.multialarm.activities.DismissAlarmActivity;

import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.core.app.NotificationCompat;

/**
 * This notification is used for displaying DissmissAlarmActivity because of Android Q limitations.
 */
public class DismissAlarmNotificationController {

    public final int NOTIFICATION_ID = 1;
    public static final String INTENT_KEY_NOTIFICATION_ID = "notificationId";
    public final String CHANNEL_ID = "channel-01";
    private NotificationManager notificationManager;
    private Context context;
    private final int IMPORTANCE = NotificationManager.IMPORTANCE_HIGH;

    public DismissAlarmNotificationController(Context context) {
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        this.context = context;
    }

    public void showNotification() {

        Intent fullScreenIntent = new Intent(context, DismissAlarmActivity.class);
        PendingIntent fullScreenPendingIntent = PendingIntent.getActivity(context, 0,
                fullScreenIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(
                    CHANNEL_ID, getChannelName(), IMPORTANCE);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_alarm_on_notification)
                .setContentTitle(context.getString(R.string.dismiss_alarm_notification_title))
                .setContentText(context.getString(R.string.dismiss_alarm_notification_body, getCurrentTime()))
                .setAutoCancel(true)
                .addAction(getDismissNotificationAction())
                .setFullScreenIntent(fullScreenPendingIntent, true);

        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());

    }

    public String getChannelName() {
        return context.getString(R.string.app_name) + "Channel";
    }

    public void cancelNotification() {
        notificationManager.cancelAll();
    }

    private NotificationCompat.Action getDismissNotificationAction() {
        Intent dismissIntent = new Intent(context, DismissNotificationReceiver.class);
        dismissIntent.putExtra(INTENT_KEY_NOTIFICATION_ID, NOTIFICATION_ID);

        PendingIntent dismissNotificationPendingIntent = PendingIntent.getBroadcast(context, 0, dismissIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        return new NotificationCompat.Action.Builder(
                0,
                context.getString(R.string.dismiss_alarm_notification_dismiss_button_title),
                dismissNotificationPendingIntent)
                .build();
    }

    private String getCurrentTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        Date currentTime = new Date();
        return dateFormat.format(currentTime);
    }
}
