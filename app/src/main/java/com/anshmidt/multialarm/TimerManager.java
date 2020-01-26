package com.anshmidt.multialarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import static com.anshmidt.multialarm.AlarmBroadcastReceiver.KEY_IS_ONE_TIME;

/**
 * Created by Ilya Anshmidt on 23.09.2017.
 */

public class TimerManager {
    private Context context;
    private AlarmManager alarmManager;
    private SharedPreferencesHelper sharPrefHelper;
    private final String LOG_TAG = TimerManager.class.getSimpleName();

    public TimerManager(Context context) {
        this.context = context;
        this.alarmManager = (AlarmManager) this.context.getSystemService(Context.ALARM_SERVICE);
        this.sharPrefHelper = new SharedPreferencesHelper(context);
    }

    public void startTimer(long firstAlarmTimeMillis, int intervalMin) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(firstAlarmTimeMillis);
        Log.d(LOG_TAG, "firstAlarmTime is set to: "+ calendar.getTime());

        Intent intent = new Intent(context, AlarmBroadcastReceiver.class);
        intent.putExtra(KEY_IS_ONE_TIME, Boolean.FALSE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, firstAlarmTimeMillis, TimeUnit.MINUTES.toMillis(intervalMin), pendingIntent);

        sharPrefHelper.setNumberOfAlreadyRangAlarms(0);
    }

    public void startSingleAlarmTimer(long alarmTimeMillis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(alarmTimeMillis);
        Log.d(LOG_TAG, "single alarm scheduled to: "+ calendar.getTime());

        Intent intent = new Intent(context, AlarmBroadcastReceiver.class);
        intent.putExtra(KEY_IS_ONE_TIME, Boolean.FALSE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.setAlarmClock(new AlarmManager.AlarmClockInfo(alarmTimeMillis, pendingIntent), pendingIntent);

    }


    public void startTimer(AlarmParams alarmParams) {
        startTimer(alarmParams.firstAlarmTime.toMillis(), alarmParams.interval);
    }

    public void cancelTimer() {
        Intent intent = new Intent(context, AlarmBroadcastReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        alarmManager.cancel(sender);
    }


    public void resetTimer(AlarmParams alarmParams) { //if alarm is turned on and preferences have changed
        cancelTimer();
        startTimer(alarmParams);
        Log.d(LOG_TAG, "Alarm is reset");
    }

    public void resetTimer(long firstAlarmTimeMillis, int intervalMin) {
        cancelTimer();
        startTimer(firstAlarmTimeMillis, intervalMin);
    }

    public void resetSingleAlarmTimer(long alarmTimeMillis) {  //if alarm is turned on and preferences have changed
        cancelTimer();
        startSingleAlarmTimer(alarmTimeMillis);
        Log.d(LOG_TAG, "Alarm is reset");
    }

}
