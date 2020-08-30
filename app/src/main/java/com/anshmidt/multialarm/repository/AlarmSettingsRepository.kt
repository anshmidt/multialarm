package com.anshmidt.multialarm.repository

import android.content.Context
import android.content.SharedPreferences
import com.anshmidt.multialarm.data.AlarmSettings
import org.threeten.bp.LocalTime

class AlarmSettingsRepository(private val context: Context) {

    companion object {
        private const val PREFERENCES_NAME = "alarmPreferences"

        private const val ALARM_SWITCH_STATE_KEY = "alarmSwitchState"
        private const val FIRST_ALARM_HOURS_KEY = "firstAlarmHours"
        private const val FIRST_ALARM_MINUTES_KEY = "firstAlarmMinutes"
        private const val MINUTES_BETWEEN_ALARMS_KEY = "minutesBetweenAlarms"
        private const val NUMBER_OF_ALARMS_KEY = "numberOfAlarms"

        private val DEFAULT_SETTINGS = AlarmSettings(
                alarmSwitchState = false,
                firstAlarmTime = LocalTime.of(6, 0),
                minutesBetweenAlarms = 10,
                numberOfAlarms = 5
        )
    }

    private val preferences: SharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)

    var alarmSwitchState: Boolean
        get() = preferences.getBoolean(ALARM_SWITCH_STATE_KEY, DEFAULT_SETTINGS.alarmSwitchState)
        set(alarmState) {
            preferences.edit().putBoolean(ALARM_SWITCH_STATE_KEY, alarmState).apply()
        }

    var firstAlarmTime: LocalTime
        get() {
            val hours = preferences.getInt(FIRST_ALARM_HOURS_KEY, DEFAULT_SETTINGS.firstAlarmTime.hour)
            val minutes = preferences.getInt(FIRST_ALARM_MINUTES_KEY, DEFAULT_SETTINGS.firstAlarmTime.minute)
            return LocalTime.of(hours, minutes)
        }
        set(value) {
            preferences.edit().putInt(FIRST_ALARM_HOURS_KEY, value.hour).apply()
            preferences.edit().putInt(FIRST_ALARM_MINUTES_KEY, value.minute).apply()
        }

    var minutesBetweenAlarms: Int
        get() = preferences.getInt(MINUTES_BETWEEN_ALARMS_KEY, DEFAULT_SETTINGS.minutesBetweenAlarms)
        set(value) {
            preferences.edit().putInt(MINUTES_BETWEEN_ALARMS_KEY, value).apply()
        }

    var numberOfAlarms: Int
        get() = preferences.getInt(NUMBER_OF_ALARMS_KEY, DEFAULT_SETTINGS.numberOfAlarms)
        set(value) {
            preferences.edit().putInt(NUMBER_OF_ALARMS_KEY, value).apply()
        }


    /**
     * Method created for testing purposes. That's why it uses the main thread.
     */
    fun clearAll() {
        preferences.edit().clear().commit()
    }
}