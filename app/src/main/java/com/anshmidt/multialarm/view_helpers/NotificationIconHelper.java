package com.anshmidt.multialarm.view_helpers;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.anshmidt.multialarm.R;
import com.anshmidt.multialarm.activities.MainActivity;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by Ilya Anshmidt on 03.10.2017.
 */

public class NotificationIconHelper {

    private NotificationManager notificationManager;
    private final int NOTIFICATION_ID = 1;
    private Context context;

    public NotificationIconHelper(Context context) {
        this.context = context;
        notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
    }

    public void showNotificationIcon() {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_alarm_on_notification)
                .setContentTitle(context.getString(R.string.notification_icon_title));
        Intent resultIntent = new Intent(context, MainActivity.class);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(
                context,
                0,
                resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);
        Notification notification = mBuilder.build();
        notification.flags |= Notification.FLAG_NO_CLEAR | Notification.FLAG_ONGOING_EVENT;

        notificationManager.notify(NOTIFICATION_ID, notification);
    }

    public void hideNotificationIcon() {
        notificationManager.cancel(NOTIFICATION_ID);
    }

    public void setNotificationIcon(boolean shouldBeDisplayed) {
        if (shouldBeDisplayed) {
            showNotificationIcon();
        }
    }
}
