package com.anshmidt.multialarm;

/**
 * Created by Ilya Anshmidt on 19.09.2017.
 */

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.anshmidt.multialarm.activities.DismissAlarmActivity;

import androidx.core.app.NotificationCompat;

public class AlarmBroadcastReceiver extends BroadcastReceiver {

    public static final String KEY_IS_ONE_TIME = "onetime";
    NotificationController notificationController;

    @Override
    public void onReceive(Context context, Intent intent) {

        notificationController = new NotificationController(context);

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
            notificationController.showNotification(context);
        } else {
            Intent dismissAlarmIntent = new Intent(context, DismissAlarmActivity.class);
            context.startActivity(dismissAlarmIntent);
        }
    }



}
