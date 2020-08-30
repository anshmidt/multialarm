package com.anshmidt.multialarm.repository

import android.content.Context
import android.content.SharedPreferences
import com.anshmidt.multialarm.data.AlarmSettings
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.threeten.bp.LocalTime
import java.util.concurrent.TimeUnit

class AlarmSettingsRepository(private val context: Context) {

    companion object {
        private const val PREFERENCES_NAME = "alarmPreferences"

        private const val ALARM_SWITCH_STATE_KEY = "alarmSwitchState"
        private const val FIRST_ALARM_HOURS_KEY = "firstAlarmHours"
        private const val FIRST_ALARM_MINUTES_KEY = "firstAlarmMinutes"

        private const val DEFAULT_ALARM_SWITCH_STATE = false
        private val DEFAULT_FIRST_ALARM_TIME = LocalTime.of(6, 0)
    }

    private val preferences: SharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)

    var alarmSwitchState: Boolean
        get() = preferences.getBoolean(ALARM_SWITCH_STATE_KEY, DEFAULT_ALARM_SWITCH_STATE)
        set(alarmState) {
            preferences.edit().putBoolean(ALARM_SWITCH_STATE_KEY, alarmState).apply()
        }

    var firstAlarmTime: LocalTime
        get() {
            val hours = preferences.getInt(FIRST_ALARM_HOURS_KEY, DEFAULT_FIRST_ALARM_TIME.hour)
            val minutes = preferences.getInt(FIRST_ALARM_MINUTES_KEY, DEFAULT_FIRST_ALARM_TIME.minute)
            return LocalTime.of(hours, minutes)
        }
        set(value) {
            preferences.edit().putInt(FIRST_ALARM_HOURS_KEY, value.hour).apply()
            preferences.edit().putInt(FIRST_ALARM_MINUTES_KEY, value.minute).apply()
        }



    /**
     * Method created for testing purposes. That's why it uses the main thread.
     */
    fun clearAll() {
        preferences.edit().clear().commit()
    }
}