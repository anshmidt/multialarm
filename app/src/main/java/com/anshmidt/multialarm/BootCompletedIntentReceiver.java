package com.anshmidt.multialarm;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;

public class BootCompletedIntentReceiver extends BroadcastReceiver {

    private final String LOG_TAG = this.getClass().getSimpleName();
    private final String INTENT_BOOT_COMPLETED_NAME = "android.intent.action.BOOT_COMPLETED";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(LOG_TAG, "BroadcaseReceiver: onReceive");
        if (intent.getAction().equals(INTENT_BOOT_COMPLETED_NAME)) {
            Log.d(LOG_TAG, "Boot completed action received");
            TimerManager timerManager = new TimerManager(context);
            SharedPreferencesHelper sharedPreferencesHelper = new SharedPreferencesHelper(context);
            AlarmParams alarmParams = sharedPreferencesHelper.getParams();
            timerManager.resetSingleAlarmTimer(alarmParams.firstAlarmTime.toMillis());
            Log.d(LOG_TAG, "Timer reset");
        }
    }



}