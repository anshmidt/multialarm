package com.anshmidt.multialarm;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.Calendar;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by Ilya Anshmidt on 24.09.2017.
 */

public class SharedPreferencesHelper {
    private SharedPreferences preferences;
    private Context context;
    private final String LOG_TAG = SharedPreferencesHelper.class.getSimpleName();
    private final String FIRST_ALARM_HOUR;
    private final String FIRST_ALARM_MINUTE;
    private final String SWITCH_STATE;
    private final String INTERVAL;
    private final String NUMBER_OF_ALARMS;
    private final String DURATION;
    private final String INSTALLATION_DATE;
    private final String NUMBER_OF_ALREADY_RANG_ALARMS;
    private final String RINGTONE_FILE_NAME;

    private final int DEFAULT_INTERVAL = 10;
    private final int DEFAULT_FIRST_ALARM_HOUR = 6;
    private final int DEFAULT_FIRST_ALARM_MINUTE = 0;
    private final int DEFAULT_NUMBER_OF_ALARMS = 5;
    private final String DEFAULT_RINGTONE_FILE_NAME = "";

    public SharedPreferencesHelper(Context context) {
        this.preferences = PreferenceManager.getDefaultSharedPreferences(context);
        this.context = context;
        FIRST_ALARM_HOUR = context.getResources().getString(R.string.key_first_alarm_hour);
        FIRST_ALARM_MINUTE = context.getResources().getString(R.string.key_first_alarm_minute);
        SWITCH_STATE = context.getResources().getString(R.string.key_switch_state);
        INTERVAL = context.getResources().getString(R.string.key_interval);
        NUMBER_OF_ALARMS = context.getResources().getString(R.string.key_number_of_alarms);
        DURATION = context.getResources().getString(R.string.key_duration);
        INSTALLATION_DATE = context.getResources().getString(R.string.key_installation_date);
        NUMBER_OF_ALREADY_RANG_ALARMS = context.getResources().getString(R.string.key_number_of_already_rang_alarms);
        RINGTONE_FILE_NAME = context.getResources().getString(R.string.key_ringtone_filename);

        if (! preferences.contains(NUMBER_OF_ALREADY_RANG_ALARMS)) {
            setNumberOfAlreadyRangAlarms(0);
        }

        if (! preferences.contains(RINGTONE_FILE_NAME)) {
            setDefaultRingtoneFileName();
        }
    }

    public boolean doPreferencesExist() {
        if (preferences.contains(FIRST_ALARM_HOUR)) {
            return true;
        } else {
            return false;
        }
    }

    public int getHour() {
        return preferences.getInt(FIRST_ALARM_HOUR, DEFAULT_FIRST_ALARM_HOUR);
    }

    public int getMinute() {
        return preferences.getInt(FIRST_ALARM_MINUTE, DEFAULT_FIRST_ALARM_MINUTE);
    }

    public void setTime(AlarmTime time) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(FIRST_ALARM_HOUR, time.getHour());
        editor.putInt(FIRST_ALARM_MINUTE, time.getMinute());
        editor.apply();
    }

    public void setTime(int hour, int minute) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(FIRST_ALARM_HOUR, hour);
        editor.putInt(FIRST_ALARM_MINUTE, minute);
        editor.apply();
    }

    public AlarmTime getTime() {
        return new AlarmTime(getHour(), getMinute());
    }

    public boolean isAlarmTurnedOn() {
        return preferences.getBoolean(SWITCH_STATE, false);
    }

    public void setAlarmState(boolean switchState) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(SWITCH_STATE, switchState);
        editor.apply();
    }

    public int getInterval() {  //minutes
        return preferences.getInt(INTERVAL, DEFAULT_INTERVAL);
    }

    public String getIntervalStr() {
        return Integer.toString(getInterval());
    }


    public void setInterval(String intervalStr) {
        int interval = Integer.parseInt(intervalStr);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(INTERVAL, interval);
        editor.apply();
    }


    public int getNumberOfAlarms() {
        return preferences.getInt(NUMBER_OF_ALARMS, DEFAULT_NUMBER_OF_ALARMS);
    }

    public String getNumberOfAlarmsStr() {
        return Integer.toString(getNumberOfAlarms());
    }

    public void setNumberOfAlarms(String numberOfAlarmsStr) {
        int numberOfAlarms = Integer.parseInt(numberOfAlarmsStr);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(NUMBER_OF_ALARMS, numberOfAlarms);
        editor.apply();
    }

    public String getDurationStr() {
        return preferences.getString(DURATION, context.getString(R.string.preferences_default_duration));
    }

    public int getDurationInt() {
        String durationStr = getDurationStr();
        int durationInt = 0;
        if (durationStr.contains("seconds")) {
            String[] durationParts = durationStr.split(" ");
            try {
                durationInt = Integer.parseInt(durationParts[0]);
            } catch (NumberFormatException e) {
                Log.e(LOG_TAG, "Invalid duration value");
            }
        }
        Log.d(LOG_TAG, "DurationInt = "+durationInt);
        return durationInt;
    }

    private long getInstallationDate() {
        long defaultInstallationDate = Calendar.getInstance().getTimeInMillis();
        if (preferences.contains(INSTALLATION_DATE)) {
            return preferences.getLong(INSTALLATION_DATE, defaultInstallationDate);
        } else {
            // First run
            Log.d(LOG_TAG, "INSTALLATION_DATE not found");
            setInstallationDate();
            return defaultInstallationDate;
        }
    }

    private void setInstallationDate() {
        long currentDateMillis = Calendar.getInstance().getTimeInMillis();
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong(INSTALLATION_DATE, currentDateMillis);
        editor.apply();
    }

    public long getDaysSinceInstallation() {
        long currentDateMillis = Calendar.getInstance().getTimeInMillis();
        long installationDateMillis = getInstallationDate();
        long diffDays = TimeUnit.DAYS.convert(currentDateMillis - installationDateMillis, TimeUnit.MILLISECONDS);
        Log.d(LOG_TAG, "Days since installation: " + diffDays);
        return diffDays;
    }

    public void setNumberOfAlreadyRangAlarms(int numberOfAlreadyRangAlarms) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(NUMBER_OF_ALREADY_RANG_ALARMS, numberOfAlreadyRangAlarms);
        editor.apply();
    }

    public int getNumberOfAlreadyRangAlarms() {
        return preferences.getInt(NUMBER_OF_ALREADY_RANG_ALARMS, 0);
    }


    public void setDefaultRingtoneFileName() {
        setRingtoneFileName(DEFAULT_RINGTONE_FILE_NAME);
    }

    public void setRingtoneFileName(String fileName) {   //example of ringtoneFileName: amelie.mp3
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(RINGTONE_FILE_NAME, fileName);
        editor.apply();
        Log.d(LOG_TAG, "ringtoneFileName set to " + fileName);
    }

    public String getRingtoneFileName() {
        return preferences.getString(RINGTONE_FILE_NAME, DEFAULT_RINGTONE_FILE_NAME);
    }


    public Uri getDefaultRingtoneUri() {
        Uri defaultRingtoneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM); // content://settings/system/alarm_alert
        if (defaultRingtoneUri == null) {  // it could happen if user has never set alarm on a new device
            defaultRingtoneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        }
        return defaultRingtoneUri;
    }

    public AlarmParams getParams() {
        return new AlarmParams(isAlarmTurnedOn(), getTime(), getInterval(), getNumberOfAlarms());
    }

    public void printAll() {
        Map<String, ?> keys = preferences.getAll();
        Log.d(LOG_TAG, "Printing all shared preferences...");
        if (keys != null) {
            for (Map.Entry<String, ?> entry : keys.entrySet()) {
                Log.d(LOG_TAG, entry.getKey() + ": " +
                        entry.getValue().toString());
            }
            Log.d(LOG_TAG, "End of all preferences.");
        } else {
            Log.d(LOG_TAG, "Shared preferences don't exist");
        }
    }

    public void deleteAll() {
        preferences.edit().clear().commit();
        Log.d(LOG_TAG, "Shared preferences are deleted");
    }




}
