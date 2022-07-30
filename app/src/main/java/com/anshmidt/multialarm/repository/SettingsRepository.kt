package com.anshmidt.multialarm.repository

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.media.RingtoneManager
import android.net.Uri
import com.anshmidt.multialarm.data.AlarmSettings
import com.anshmidt.multialarm.data.AlarmsConverter
import com.anshmidt.multialarm.data.SingleLiveEvent
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import org.threeten.bp.LocalTime

class SettingsRepository(private val context: Context) : ISettingsRepository {

    companion object {
        private const val PREFERENCES_NAME = "alarmPreferences"

        private const val ALARM_SWITCH_STATE_KEY = "alarmSwitchState"
        private const val FIRST_ALARM_HOURS_KEY = "firstAlarmHours"
        private const val FIRST_ALARM_MINUTES_KEY = "firstAlarmMinutes"
        private const val MINUTES_BETWEEN_ALARMS_KEY = "minutesBetweenAlarms"
        private const val NUMBER_OF_ALARMS_KEY = "numberOfAlarms"
        private const val SONG_DURATION_SECONDS_KEY = "songDurationSeconds"

        private val DEFAULT_SETTINGS = AlarmSettings(
                turnedOn = false,
                firstAlarmTime = LocalTime.of(6, 0),
                minutesBetweenAlarms = 10,
                numberOfAlarms = 5,
                songDurationSeconds = 90
        )
    }

    private val preferences: SharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)

    override var alarmTurnedOn: Boolean
        get() = preferences.getBoolean(ALARM_SWITCH_STATE_KEY, DEFAULT_SETTINGS.turnedOn)
        set(alarmState) {
            alarmTurnedOnObservable.onNext(alarmState)
            preferences.edit().putBoolean(ALARM_SWITCH_STATE_KEY, alarmState).apply()
        }

    override var firstAlarmTime: LocalTime
        get() {
            val hours = preferences.getInt(FIRST_ALARM_HOURS_KEY, DEFAULT_SETTINGS.firstAlarmTime.hour)
            val minutes = preferences.getInt(FIRST_ALARM_MINUTES_KEY, DEFAULT_SETTINGS.firstAlarmTime.minute)
            return LocalTime.of(hours, minutes)
        }
        set(value) {
            preferences.edit().putInt(FIRST_ALARM_HOURS_KEY, value.hour).apply()
            preferences.edit().putInt(FIRST_ALARM_MINUTES_KEY, value.minute).apply()
        }

    override var minutesBetweenAlarms: Int
        get() = preferences.getInt(MINUTES_BETWEEN_ALARMS_KEY, DEFAULT_SETTINGS.minutesBetweenAlarms)
        set(value) {
            preferences.edit().putInt(MINUTES_BETWEEN_ALARMS_KEY, value).apply()
        }

    override var numberOfAlarms: Int
        get() = preferences.getInt(NUMBER_OF_ALARMS_KEY, DEFAULT_SETTINGS.numberOfAlarms)
        set(value) {
            preferences.edit().putInt(NUMBER_OF_ALARMS_KEY, value).apply()
        }

    override var songDurationSeconds: Int
        get() = preferences.getInt(SONG_DURATION_SECONDS_KEY, DEFAULT_SETTINGS.songDurationSeconds)
        set(value) {
            preferences.edit().putInt(SONG_DURATION_SECONDS_KEY, value).apply()
        }

    override val songUri: Uri
        get() = getDefaultRingtoneUri()

    private val prefChangeListener = SharedPreferences.OnSharedPreferenceChangeListener { _, _ ->
        alarmsListObservable.onNext(getAlarmsList())
    }

    override val alarmsListObservable = BehaviorSubject.createDefault(getAlarmsList())
    override val alarmTurnedOnObservable = BehaviorSubject.createDefault(alarmTurnedOn)

    override fun subscribeOnChangeListener() {
        preferences.registerOnSharedPreferenceChangeListener(prefChangeListener)
    }

    override fun unsubscribeOnChangeListener() {
        preferences.unregisterOnSharedPreferenceChangeListener(prefChangeListener)
    }

    private fun getDefaultRingtoneUri(): Uri {
        var defaultSongUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        if (defaultSongUri == null) {  // it could happen if user has never set alarm on a new device
            defaultSongUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        }
        return defaultSongUri
    }


    /**
     * Method created for testing purposes. That's why it uses the main thread.
     */
    @SuppressLint("ApplySharedPref")
    override fun clearAll() {
        preferences.edit().clear().commit()
    }

    override fun getSettings(): AlarmSettings {
        return AlarmSettings(
                turnedOn = alarmTurnedOn,
                firstAlarmTime = firstAlarmTime,
                minutesBetweenAlarms = minutesBetweenAlarms,
                numberOfAlarms = numberOfAlarms,
                songDurationSeconds = songDurationSeconds
        )
    }

    private fun getAlarmsList(): List<LocalTime> {
        return AlarmsConverter.getAlarmsList(
                firstAlarmTime = firstAlarmTime,
                numberOfAlarms = numberOfAlarms,
                minutesBetweenAlarms = minutesBetweenAlarms
        )
    }



}