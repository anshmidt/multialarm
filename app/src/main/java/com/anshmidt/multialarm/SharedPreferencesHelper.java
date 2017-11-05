package com.anshmidt.multialarm;

import android.content.Context;
import android.content.SharedPreferences;
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
    private final String FIRST_ALARM_HOUR = "firstAlarmHour";
    private final String FIRST_ALARM_MINUTE = "firstAlarmMinute";
    private final String SWITCH_STATE = "switchState";
    private final String INTERVAL = "interval";
    private final String NUMBER_OF_ALARMS = "numberOfAlarms";
    private final String DURATION = "duration";
    private final String INSTALLATION_DATE = "installationDate";
    private final String NUMBER_OF_ALREADY_RANG_ALARMS = "numberOfAlreadyRangAlarms";

    public SharedPreferencesHelper(Context context) {
        this.preferences = PreferenceManager.getDefaultSharedPreferences(context);
        this.context = context;
        if (! preferences.contains(NUMBER_OF_ALREADY_RANG_ALARMS)) {
            setNumberOfAlreadyRangAlarms(0);
        }
        printAll();  //temp
    }

    public boolean doPreferencesExist() {
        if (preferences.contains(FIRST_ALARM_HOUR)) {
            return true;
        } else {
            return false;
        }
    }

    public int getHour() {
        return preferences.getInt(FIRST_ALARM_HOUR, 23);
    }

    public int getMinute() {
        return preferences.getInt(FIRST_ALARM_MINUTE, 59);
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


//    public void registerOnSharedPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener listener) {
//        preferences.registerOnSharedPreferenceChangeListener(listener);
//    }

    public boolean isAlarmTurnedOn() {
        if (preferences.contains(SWITCH_STATE)) {
            return preferences.getBoolean(SWITCH_STATE, false);
        } else {
            Log.d(LOG_TAG, "SWITCH_STATE not found");
            return false;
        }
    }

    public void setAlarmState(boolean switchState) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(SWITCH_STATE, switchState);
        editor.apply();
    }

    public int getInterval() {
        if (preferences.contains(INTERVAL)) {
            return preferences.getInt(INTERVAL, 10);
        } else {
            Log.d(LOG_TAG, "INTERVAL not found");
            return 10;
        }
    }

    public String getIntervalStr() {
        return Integer.toString(getInterval());
    }

//    public void setInterval(int interval) {
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.putInt(INTERVAL, interval);
//        editor.apply();
//    }

    public void setInterval(String intervalStr) {
        int interval = Integer.parseInt(intervalStr);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(INTERVAL, interval);
        editor.apply();
    }


    public int getNumberOfAlarms() {
        if (preferences.contains(NUMBER_OF_ALARMS)) {
            return preferences.getInt(NUMBER_OF_ALARMS, 10);
        } else {
            Log.d(LOG_TAG, "NUMBER_OF_ALARMS not found");
            return 10;
        }
    }

    public String getNumberOfAlarmsStr() {
        return Integer.toString(getNumberOfAlarms());
    }

//    public void setNumberOfAlarms(int numberOfAlarms) {
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.putInt(NUMBER_OF_ALARMS, numberOfAlarms);
//        editor.apply();
//    }

    public void setNumberOfAlarms(String numberOfAlarmsStr) {
        int numberOfAlarms = Integer.parseInt(numberOfAlarmsStr);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(NUMBER_OF_ALARMS, numberOfAlarms);
        editor.apply();
    }

    public String getDurationStr() {
    if (preferences.contains(DURATION)) {
            return preferences.getString(DURATION, context.getString(R.string.preferences_default_duration));
        } else {
            Log.d(LOG_TAG, "DURATION not found");
            return context.getString(R.string.preferences_default_duration);
        }
    }

    public int getDurationInt() {
        String durationStr = getDurationStr();
        final String ONE_RINGTONE = context.getResources().getStringArray(R.array.preferences_duration_array)[0];
        //int durationInt = -1;
        int durationInt = 0;
        if (durationStr.contains("seconds")) {
            String[] durationParts = durationStr.split(" ");
            try {
                durationInt = Integer.parseInt(durationParts[0]);
            } catch (NumberFormatException e) {
                Log.e(LOG_TAG, "Invalid duration value");
            }
        }
//        else if (durationStr.equals(ONE_RINGTONE)) {
//            durationInt = 0;
//        }
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
        if (preferences.contains(NUMBER_OF_ALREADY_RANG_ALARMS)) {
            return preferences.getInt(NUMBER_OF_ALREADY_RANG_ALARMS, 0);
        } else {
            Log.d(LOG_TAG, "NUMBER_OF_ALREADY_RANG_ALARMS not found");
            return 0;
        }
    }

    public AlarmParams getParams() {
        return new AlarmParams(isAlarmTurnedOn(), getTime(), getInterval(), getNumberOfAlarms());
    }

    public void printAll() {
        Map<String,?> keys = preferences.getAll();
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
