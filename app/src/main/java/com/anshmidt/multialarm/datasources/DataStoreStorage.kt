package com.anshmidt.multialarm.datasources

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.anshmidt.multialarm.data.AlarmSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.threeten.bp.LocalTime

class DataStoreStorage(private val context: Context) {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = PREFERENCE_NAME)

    suspend fun saveAlarmSwitchState(switchState: Boolean) {
        context.writeBoolean(ALARM_SWITCH_STATE_KEY, switchState)
    }

    fun getAlarmSwitchStateFlow() = context.readBoolean(ALARM_SWITCH_STATE_KEY)


    private suspend fun Context.writeString(key: String, value: String) {
        dataStore.edit { pref -> pref[stringPreferencesKey(key)] = value }
    }

    private fun Context.readString(key: String): Flow<String> {
        return dataStore.data.map{ pref ->
            pref[stringPreferencesKey(key)] ?: ""
        }
    }

    private suspend fun Context.writeInt(key: String, value: Int) {
        dataStore.edit { pref -> pref[intPreferencesKey(key)] = value }
    }

    private fun Context.readInt(key: String): Flow<Int> {
        return dataStore.data.map { pref ->
            pref[intPreferencesKey(key)] ?: 0
        }
    }

    private suspend fun Context.writeBoolean(key: String, value: Boolean) {
        dataStore.edit { pref -> pref[booleanPreferencesKey(key)] = value }
    }

    private fun Context.readBoolean(key: String): Flow<Boolean> {
        return dataStore.data.map { pref ->
            pref[booleanPreferencesKey(key)] ?: false
        }
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
                turnedOn = false,
                firstAlarmTime = LocalTime.of(6, 0),
                minutesBetweenAlarms = 10,
                numberOfAlarms = 5
        )

        private const val DEFAULT_RINGTONE_DURATION_SECONDS = 90
        private const val DEFAULT_RINGTONE_URI_STRING = ""
    }

}