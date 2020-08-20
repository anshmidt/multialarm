package com.anshmidt.multialarm.repository

import android.content.Context
import android.content.SharedPreferences

class AlarmSettingsRepository(val context: Context) {

    companion object {
        private const val PREFERENCES_NAME = "alarmPreferences"

        private const val ALARM_STATE_KEY = "alarmState"


        const val DEFAULT_ALARM_STATE = false
    }

    private val preferences: SharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)

    var isAlarmTurnedOn: Boolean
        get() = preferences.getBoolean(ALARM_STATE_KEY, DEFAULT_ALARM_STATE)
        set(alarmState) {
            preferences.edit().putBoolean(ALARM_STATE_KEY, alarmState).apply()
        }

}