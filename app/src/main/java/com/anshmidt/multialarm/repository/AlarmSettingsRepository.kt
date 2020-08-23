package com.anshmidt.multialarm.repository

import android.content.Context
import android.content.SharedPreferences

class AlarmSettingsRepository(private val context: Context) {

    companion object {
        private const val PREFERENCES_NAME = "alarmPreferences"

        private const val ALARM_SWITCH_STATE_KEY = "alarmSwitchState"


        const val DEFAULT_ALARM_SWITCH_STATE = false
    }

    private val preferences: SharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)

    var alarmSwitchState: Boolean
        get() = preferences.getBoolean(ALARM_SWITCH_STATE_KEY, DEFAULT_ALARM_SWITCH_STATE)
        set(alarmState) {
            preferences.edit().putBoolean(ALARM_SWITCH_STATE_KEY, alarmState).apply()
        }

}