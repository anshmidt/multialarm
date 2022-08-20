package com.anshmidt.multialarm.datasources

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.anshmidt.multialarm.data.AlarmSettings
import kotlinx.coroutines.flow.map
import org.threeten.bp.LocalTime

class DataStoreStorage(private val context: Context) {

    private val Context._dataStore: DataStore<Preferences> by preferencesDataStore(name = PREFERENCE_NAME)
    private val dataStore: DataStore<Preferences> = context._dataStore

    suspend fun saveAlarmSwitchState(switchState: Boolean) {
        dataStore.edit { pref ->
            pref[booleanPreferencesKey(ALARM_SWITCH_STATE_KEY)] = switchState
        }
    }

    fun getAlarmSwitchState() = dataStore.data.map { pref ->
        pref[booleanPreferencesKey(ALARM_SWITCH_STATE_KEY)] ?: DEFAULT_SETTINGS.switchState
    }

    suspend fun saveMinutesBetweenAlarms(minutesBetweenAlarms: Int) {
        dataStore.edit { pref ->
            pref[intPreferencesKey(MINUTES_BETWEEN_ALARMS_KEY)] = minutesBetweenAlarms
        }
    }

    fun getMinutesBetweenAlarms() = dataStore.data.map { pref ->
        pref[intPreferencesKey(MINUTES_BETWEEN_ALARMS_KEY)] ?: DEFAULT_SETTINGS.minutesBetweenAlarms
    }

    suspend fun saveNumberOfAlarms(numberOfAlarms: Int) {
        dataStore.edit { pref ->
            pref[intPreferencesKey(NUMBER_OF_ALARMS_KEY)] = numberOfAlarms
        }
    }

    fun getNumberOfAlarms() = dataStore.data.map { pref ->
        pref[intPreferencesKey(NUMBER_OF_ALARMS_KEY)] ?: DEFAULT_SETTINGS.numberOfAlarms
    }


    suspend fun saveFirstAlarmHours(firstAlarmHours: Int) {
        dataStore.edit { pref ->
            pref[intPreferencesKey(FIRST_ALARM_HOURS_KEY)] = firstAlarmHours
        }
    }

    fun getFirstAlarmHours() = dataStore.data.map { pref ->
        pref[intPreferencesKey(FIRST_ALARM_HOURS_KEY)] ?: DEFAULT_SETTINGS.firstAlarmTime.hour
    }

    suspend fun saveFirstAlarmMinutes(firstAlarmMinutes: Int) {
        dataStore.edit { pref ->
            pref[intPreferencesKey(FIRST_ALARM_MINUTES_KEY)] = firstAlarmMinutes
        }
    }

    fun getFirstAlarmMinutes() = dataStore.data.map { pref ->
        pref[intPreferencesKey(FIRST_ALARM_MINUTES_KEY)] ?: DEFAULT_SETTINGS.firstAlarmTime.minute
    }

    fun getAlarmSettings() = dataStore.data.map { pref ->
        pref.toAlarmSettings()
    }

    private fun Preferences.toAlarmSettings(): AlarmSettings {
        val switchState = this[booleanPreferencesKey(ALARM_SWITCH_STATE_KEY)] ?: DEFAULT_SETTINGS.switchState
        val numberOfAlarms = this[intPreferencesKey(NUMBER_OF_ALARMS_KEY)] ?: DEFAULT_SETTINGS.numberOfAlarms
        val firstAlarmHours = this[intPreferencesKey(FIRST_ALARM_HOURS_KEY)] ?: DEFAULT_SETTINGS.firstAlarmTime.hour
        val firstAlarmMinutes = this[intPreferencesKey(FIRST_ALARM_MINUTES_KEY)] ?: DEFAULT_SETTINGS.firstAlarmTime.minute
        val minutesBetweenAlarms = this[intPreferencesKey(MINUTES_BETWEEN_ALARMS_KEY)] ?: DEFAULT_SETTINGS.minutesBetweenAlarms
        return AlarmSettings(
                switchState = switchState,
                firstAlarmTime = LocalTime.of(firstAlarmHours, firstAlarmMinutes),
                minutesBetweenAlarms = minutesBetweenAlarms,
                numberOfAlarms = numberOfAlarms
        )
    }

    companion object {
        private const val PREFERENCE_NAME = "AlarmDataStore"

        private const val ALARM_SWITCH_STATE_KEY = "alarmSwitchState"
        private const val FIRST_ALARM_HOURS_KEY = "firstAlarmHours"
        private const val FIRST_ALARM_MINUTES_KEY = "firstAlarmMinutes"
        private const val MINUTES_BETWEEN_ALARMS_KEY = "minutesBetweenAlarms"
        private const val NUMBER_OF_ALARMS_KEY = "numberOfAlarms"
        private const val RINGTONE_DURATION_SECONDS_KEY = "ringtoneDurationSeconds"
        private const val RINGTONE_FILENAME_KEY = "ringtoneFilename"

        private val DEFAULT_SETTINGS = AlarmSettings(
                switchState = false,
                firstAlarmTime = LocalTime.of(6, 0),
                minutesBetweenAlarms = 10,
                numberOfAlarms = 5
        )

        private const val DEFAULT_RINGTONE_DURATION_SECONDS = 90
        private const val DEFAULT_RINGTONE_URI_STRING = ""
    }

}