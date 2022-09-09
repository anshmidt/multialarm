package com.anshmidt.multialarm.datasources

import android.content.Context
import android.os.Build
import com.anshmidt.multialarm.data.AlarmSettings
import com.fredporciuncula.flow.preferences.FlowSharedPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import org.threeten.bp.LocalTime

/**
 * The only reason why DataStore is not used - it doesn't support Direct Boot mode yet
 * (returns default values in Direct Boot when device context is used).
 * The same issue happens with Proto DataStore.
 */
class SharedPreferencesStorage(private val context: Context) {

//    private val preferencesContext = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            context.createDeviceProtectedStorageContext()
//    } else {
//        context
//    }
//
////    private val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(preferencesContext)
//    private val sharedPreferences = preferencesContext.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
//    private val flowSharedPreferences = FlowSharedPreferences(sharedPreferences)

    private val flowSharedPreferences = getFlowSharedPreferences(context)

    private val alarmSwitchStatePreference = flowSharedPreferences
            .getBoolean(key = ALARM_SWITCH_STATE_KEY, defaultValue = DEFAULT_SETTINGS.switchState)
    private val minutesBetweenAlarmsPreference = flowSharedPreferences
            .getInt(key = MINUTES_BETWEEN_ALARMS_KEY, defaultValue = DEFAULT_SETTINGS.minutesBetweenAlarms)
    private val numberOfAlarmsPreference = flowSharedPreferences
            .getInt(key = NUMBER_OF_ALARMS_KEY, defaultValue = DEFAULT_SETTINGS.numberOfAlarms)
    private val numberOfAlreadyRangAlarmsPreference = flowSharedPreferences
            .getInt(key = NUMBER_OF_ALREADY_RANG_ALARMS_KEY, defaultValue = DEFAULT_NUMBER_OF_ALREADY_RANG_ALARMS)
    private val firstAlarmHoursPreference = flowSharedPreferences
            .getInt(key = FIRST_ALARM_HOURS_KEY, defaultValue = DEFAULT_SETTINGS.firstAlarmTime.hour)
    private val firstAlarmMinutesPreference = flowSharedPreferences
            .getInt(key = FIRST_ALARM_MINUTES_KEY, defaultValue = DEFAULT_SETTINGS.firstAlarmTime.minute)
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

    suspend fun saveAlarmSwitchState(switchState: Boolean) {
        alarmSwitchStatePreference.set(switchState)
    }

    fun getAlarmSwitchState(): Flow<Boolean> = alarmSwitchStatePreference.asFlow()

    suspend fun saveMinutesBetweenAlarms(minutesBetweenAlarms: Int) {
        minutesBetweenAlarmsPreference.set(minutesBetweenAlarms)
    }

    fun getMinutesBetweenAlarms() = minutesBetweenAlarmsPreference.asFlow()

    suspend fun saveNumberOfAlarms(numberOfAlarms: Int) {
        numberOfAlarmsPreference.set(numberOfAlarms)
    }

    fun getNumberOfAlarms() = numberOfAlarmsPreference.asFlow()

    suspend fun saveFirstAlarmHours(firstAlarmHours: Int) {
        firstAlarmHoursPreference.set(firstAlarmHours)
    }

    fun getFirstAlarmHours() = firstAlarmHoursPreference.asFlow()

    suspend fun saveFirstAlarmMinutes(firstAlarmMinutes: Int) {
        firstAlarmMinutesPreference.set(firstAlarmMinutes)
    }

    fun getFirstAlarmMinutes() = firstAlarmMinutesPreference.asFlow()

    fun getAlarmSettings(): Flow<AlarmSettings> = combine(
            getAlarmSwitchState(),
            getFirstAlarmHours(),
            getFirstAlarmMinutes(),
            getMinutesBetweenAlarms(),
            getNumberOfAlarms()
    ) { alarmSwitchState, firstAlarmHours, firstAlarmMinutes, minutesBetweenAlarms, numberOfAlarms ->
        AlarmSettings(
                switchState = alarmSwitchState,
                firstAlarmTime = LocalTime.of(firstAlarmHours, firstAlarmMinutes),
                minutesBetweenAlarms = minutesBetweenAlarms,
                numberOfAlarms = numberOfAlarms
        )
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

    suspend fun saveNumberOfAlreadyRangAlarms(numberOfAlreadyRangAlarms: Int) {
        numberOfAlreadyRangAlarmsPreference.set(numberOfAlreadyRangAlarms)
    }

    fun getNumberOfAlreadyRangAlarms() = numberOfAlreadyRangAlarmsPreference.asFlow()

    suspend fun saveMusicVolume(musicVolumePercents: Int) {
        musicVolumePreference.set(musicVolumePercents)
    }

    fun getMusicVolumePercents() = musicVolumePreference.asFlow()

    companion object {
        const val PREFERENCE_NAME = "AlarmSharedPreferences"

        private const val ALARM_SWITCH_STATE_KEY = "alarmSwitchState"
        private const val FIRST_ALARM_HOURS_KEY = "firstAlarmHours"
        private const val FIRST_ALARM_MINUTES_KEY = "firstAlarmMinutes"
        private const val MINUTES_BETWEEN_ALARMS_KEY = "minutesBetweenAlarms"
        private const val NUMBER_OF_ALARMS_KEY = "numberOfAlarms"
        private const val RINGTONE_DURATION_SECONDS_KEY = "ringtoneDurationSeconds"
        private const val RINGTONE_URI_KEY = "ringtoneUri"
        private const val NIGHT_MODE_KEY = "nightMode"
        private const val NUMBER_OF_ALREADY_RANG_ALARMS_KEY = "numberOfAlreadyRangAlarms"
        private const val MUSIC_VOLUME_KEY = "musicVolume"

        private val DEFAULT_SETTINGS = AlarmSettings(
                switchState = false,
                firstAlarmTime = LocalTime.of(6, 0),
                minutesBetweenAlarms = 10,
                numberOfAlarms = 5
        )

        private const val DEFAULT_RINGTONE_DURATION_SECONDS = 60
        private const val DEFAULT_RINGTONE_URI_STRING = ""
        private const val DEFAULT_NIGHT_MODE_SWITCH_STATE = false
        private const val DEFAULT_NUMBER_OF_ALREADY_RANG_ALARMS = 0
        private const val DEFAULT_MUSIC_VOLUME = 100

        private val TAG = DataStoreStorage::class.java.simpleName


    }
}