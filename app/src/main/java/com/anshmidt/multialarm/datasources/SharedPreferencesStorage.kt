package com.anshmidt.multialarm.datasources

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import com.anshmidt.multialarm.data.AlarmSettings
import com.anshmidt.multialarm.repository.SettingsRepository
import io.reactivex.subjects.BehaviorSubject
import org.threeten.bp.LocalTime

class SharedPreferencesStorage(private val context: Context) {

    private val preferences: SharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)

    companion object {
        private const val PREFERENCES_NAME = "alarmPreferences"

        private const val ALARM_SWITCH_STATE_KEY = "alarmSwitchState"
        private const val FIRST_ALARM_HOURS_KEY = "firstAlarmHours"
        private const val FIRST_ALARM_MINUTES_KEY = "firstAlarmMinutes"
        private const val MINUTES_BETWEEN_ALARMS_KEY = "minutesBetweenAlarms"
        private const val NUMBER_OF_ALARMS_KEY = "numberOfAlarms"
        private const val RINGTONE_DURATION_SECONDS_KEY = "ringtoneDurationSeconds"
        private const val RINGTONE_FILENAME_KEY = "ringtoneFilename"

        private val DEFAULT_SETTINGS = AlarmSettings(
                turnedOn = false,
                firstAlarmTime = LocalTime.of(6, 0),
                minutesBetweenAlarms = 10,
                numberOfAlarms = 5
        )

        private const val DEFAULT_RINGTONE_DURATION_SECONDS = 90
        private const val DEFAULT_RINGTONE_URI_STRING = ""
    }

    var alarmTurnedOn: Boolean
        get() = preferences.getBoolean(ALARM_SWITCH_STATE_KEY, DEFAULT_SETTINGS.turnedOn)
        set(alarmState) {
            preferences.edit().putBoolean(ALARM_SWITCH_STATE_KEY, alarmState).apply()
        }

    var firstAlarmHours: Int
        get() = preferences.getInt(FIRST_ALARM_HOURS_KEY, DEFAULT_SETTINGS.firstAlarmTime.hour)
        set(value) {
            preferences.edit().putInt(FIRST_ALARM_HOURS_KEY, value).apply()
        }

    var firstAlarmMinutes: Int
        get() = preferences.getInt(FIRST_ALARM_MINUTES_KEY, DEFAULT_SETTINGS.firstAlarmTime.minute)
        set(value) {
            preferences.edit().putInt(FIRST_ALARM_MINUTES_KEY, value).apply()
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

    var ringtoneDurationSeconds: Int
        get() = preferences.getInt(RINGTONE_DURATION_SECONDS_KEY, DEFAULT_RINGTONE_DURATION_SECONDS)
        set(value) {
            preferences.edit().putInt(RINGTONE_DURATION_SECONDS_KEY, value).apply()
        }

    var ringtoneUriString: String?
        get() = preferences.getString(RINGTONE_FILENAME_KEY, DEFAULT_RINGTONE_URI_STRING)
        set(value) {
            preferences.edit().putString(RINGTONE_FILENAME_KEY, value).apply()
        }

    val alarmSettingsChangedObservable = BehaviorSubject.createDefault(Unit)

    private val prefChangeListener = SharedPreferences.OnSharedPreferenceChangeListener { _, _ ->
        alarmSettingsChangedObservable.onNext(Unit)
    }

    fun subscribeOnChangeListener() {
        preferences.registerOnSharedPreferenceChangeListener(prefChangeListener)
    }

    fun unsubscribeOnChangeListener() {
        preferences.unregisterOnSharedPreferenceChangeListener(prefChangeListener)
    }

    /**
     * Method created for testing purposes. That's why it uses the main thread.
     */
    @SuppressLint("ApplySharedPref")
    fun clearAll() {
        preferences.edit().clear().commit()
    }

}