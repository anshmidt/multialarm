package com.anshmidt.multialarm.datasources

import android.content.Context
import android.os.Build
import com.anshmidt.multialarm.data.AlarmSettings
import com.anshmidt.multialarm.data.TimeFormatter
import com.anshmidt.multialarm.di.DpsContext
import com.fredporciuncula.flow.preferences.FlowSharedPreferences
import com.fredporciuncula.flow.preferences.Serializer
import kotlinx.coroutines.flow.Flow
import org.threeten.bp.LocalTime

/**
 * The only reason why DataStore is not used - it doesn't support Direct Boot mode yet
 * (returns default values in Direct Boot when device context is used).
 * The same issue happens with Proto DataStore.
 */
class SharedPreferencesStorage(private val dpsContext: DpsContext) {

    private val flowSharedPreferences = getFlowSharedPreferences(dpsContext.context)

    private val alarmSettingsSerializer =
        object : Serializer<AlarmSettings> {
            override fun deserialize(serialized: String): AlarmSettings {
                val parts = serialized.split(",")
                return AlarmSettings(
                    areOn = parts[0].toBoolean(),
                    firstAlarmTimeMillis = parts[1].toLong(),
                    minutesBetweenAlarms = parts[2].toInt(),
                    numberOfAlarms = parts[3].toInt(),
                    numberOfAlreadyRangAlarms = parts[4].toInt()
                )
            }

            override fun serialize(value: AlarmSettings): String {
                return listOf(
                    value.areOn.toString(),
                    value.firstAlarmTimeMillis.toString(),
                    value.minutesBetweenAlarms.toString(),
                    value.numberOfAlarms.toString(),
                    value.numberOfAlreadyRangAlarms.toString()
                ).joinToString(",")
            }
        }

    private val alarmSettingsPreference = flowSharedPreferences
        .getObject(
            key = ALARM_SETTINGS_KEY,
            serializer = alarmSettingsSerializer,
            defaultValue = DEFAULT_SETTINGS
        )

    private val ringtoneDurationSecondsPreference = flowSharedPreferences
            .getInt(key = RINGTONE_DURATION_SECONDS_KEY, defaultValue = DEFAULT_RINGTONE_DURATION_SECONDS)
    private val ringtoneUriStringPreference = flowSharedPreferences
            .getString(key = RINGTONE_URI_KEY, defaultValue = DEFAULT_RINGTONE_URI_STRING)
    private val nightModeSwitchStatePreference = flowSharedPreferences
            .getBoolean(key = NIGHT_MODE_KEY, defaultValue = DEFAULT_NIGHT_MODE_SWITCH_STATE)
    private val musicVolumePreference = flowSharedPreferences
            .getInt(key = MUSIC_VOLUME_KEY, defaultValue = DEFAULT_MUSIC_VOLUME)

    private fun getFlowSharedPreferences(context: Context): FlowSharedPreferences {
        val preferencesContext = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            context.createDeviceProtectedStorageContext()
        } else {
            context
        }
        val sharedPreferences = preferencesContext.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        return FlowSharedPreferences(sharedPreferences)
    }

    fun getAlarmSettings(): Flow<AlarmSettings> = alarmSettingsPreference.asFlow()

    suspend fun saveAlarmSettings(alarmSettings: AlarmSettings) {
        alarmSettingsPreference.set(alarmSettings)
    }

    suspend fun saveRingtoneDurationSeconds(ringtoneDurationSeconds: Int) {
        ringtoneDurationSecondsPreference.set(ringtoneDurationSeconds)
    }

    fun getRingtoneDurationSeconds() = ringtoneDurationSecondsPreference.asFlow()

    suspend fun saveRingtoneUriString(ringtoneUriString: String) {
        ringtoneUriStringPreference.set(ringtoneUriString)
    }

    fun getRingtoneUriString() = ringtoneUriStringPreference.asFlow()

    suspend fun saveNightModeSwitchState(nightModeSwitchState: Boolean) {
        nightModeSwitchStatePreference.set(nightModeSwitchState)
    }

    fun getNightModeSwitchState() = nightModeSwitchStatePreference.asFlow()

    suspend fun saveMusicVolume(musicVolumePercents: Int) {
        musicVolumePreference.set(musicVolumePercents)
    }

    fun getMusicVolumePercents() = musicVolumePreference.asFlow()

    companion object {
        const val PREFERENCE_NAME = "AlarmSharedPreferences"

        private const val ALARM_SETTINGS_KEY = "alarmSettings"
        private const val RINGTONE_DURATION_SECONDS_KEY = "ringtoneDurationSeconds"
        private const val RINGTONE_URI_KEY = "ringtoneUri"
        private const val NIGHT_MODE_KEY = "nightMode"
        private const val MUSIC_VOLUME_KEY = "musicVolume"

        private val DEFAULT_SETTINGS = AlarmSettings(
            areOn = false,
            firstAlarmTimeMillis = getDefaultFirstAlarmTimeMillis(),
            minutesBetweenAlarms = 10,
            numberOfAlarms = 5,
            numberOfAlreadyRangAlarms = 0
        )

        private const val DEFAULT_RINGTONE_DURATION_SECONDS = 60
        private const val DEFAULT_RINGTONE_URI_STRING = ""
        private const val DEFAULT_NIGHT_MODE_SWITCH_STATE = false
        private const val DEFAULT_MUSIC_VOLUME = 100

        private val TAG = DataStoreStorage::class.java.simpleName

        /**
         * Is used only when app is opened for the first time.
         * Get instant in the morning that is in the future from current moment,
         * but no further than 24 hours.
         */
        private fun getDefaultFirstAlarmTimeMillis(): Long {
            val defaultLocalTime = LocalTime.of(6, 0)
            return TimeFormatter.getAlarmMillisWithin24Hours(defaultLocalTime)
        }
    }
}