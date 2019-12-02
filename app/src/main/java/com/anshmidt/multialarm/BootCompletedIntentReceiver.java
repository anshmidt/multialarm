package com.anshmidt.multialarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootCompletedIntentReceiver extends BroadcastReceiver {

    private final String LOG_TAG = this.getClass().getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(LOG_TAG, "BroadcaseReceiver: onReceive");
        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
            Log.d(LOG_TAG, "Boot completed action received");
            TimerManager timerManager = new TimerManager(context);
            SharedPreferencesHelper sharedPreferencesHelper = new SharedPreferencesHelper(context);
            AlarmParams alarmParams = sharedPreferencesHelper.getParams();
            timerManager.resetSingleAlarmTimer(alarmParams.firstAlarmTime.toMillis());
            Log.d(LOG_TAG, "Timer reset");
        }
    }

}