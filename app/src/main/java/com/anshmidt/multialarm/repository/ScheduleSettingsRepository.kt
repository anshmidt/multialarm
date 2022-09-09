package com.anshmidt.multialarm.repository

import com.anshmidt.multialarm.data.Alarm
import com.anshmidt.multialarm.data.AlarmSettings
import com.anshmidt.multialarm.data.AlarmsConverter
import com.anshmidt.multialarm.datasources.SharedPreferencesStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import org.threeten.bp.LocalTime

class ScheduleSettingsRepository(
        private val sharedPreferencesStorage: SharedPreferencesStorage
) : IScheduleSettingsRepository {

    override suspend fun saveAlarmSwitchState(switchState: Boolean) {
        sharedPreferencesStorage.saveAlarmSwitchState(switchState)
    }

    override fun getAlarmSwitchState(): Flow<Boolean> = sharedPreferencesStorage.getAlarmSwitchState()

    override suspend fun saveMinutesBetweenAlarms(minutesBetweenAlarms: Int) {
        sharedPreferencesStorage.saveMinutesBetweenAlarms(minutesBetweenAlarms)
    }

    override fun getMinutesBetweenAlarms(): Flow<Int> = sharedPreferencesStorage.getMinutesBetweenAlarms()

    override suspend fun saveNumberOfAlarms(numberOfAlarms: Int) {
        sharedPreferencesStorage.saveNumberOfAlarms(numberOfAlarms)
    }

    override fun getNumberOfAlarms(): Flow<Int> = sharedPreferencesStorage.getNumberOfAlarms()

    override suspend fun saveFirstAlarmTime(firstAlarmTime: LocalTime) {
        sharedPreferencesStorage.saveFirstAlarmHours(firstAlarmTime.hour)
        sharedPreferencesStorage.saveFirstAlarmMinutes(firstAlarmTime.minute)
    }

    override fun getFirstAlarmTime(): Flow<LocalTime> = sharedPreferencesStorage.getFirstAlarmHours()
            .combine(sharedPreferencesStorage.getFirstAlarmMinutes()) { hours, minutes ->
                LocalTime.of(hours, minutes)
            }

    override fun getAlarmsList(): Flow<List<Alarm>> = sharedPreferencesStorage.getNumberOfAlreadyRangAlarms()
            .combine(sharedPreferencesStorage.getAlarmSettings()) { numberOfAlreadyRangAlarms, alarmSettings ->
                AlarmsConverter.getAlarmsList(
                        firstAlarmTime = alarmSettings.firstAlarmTime,
                        minutesBetweenAlarms = alarmSettings.minutesBetweenAlarms,
                        numberOfAlarms = alarmSettings.numberOfAlarms,
                        numberOfAlreadyRangAlarms = numberOfAlreadyRangAlarms
                )
            }

    override fun getAlarmSettings(): Flow<AlarmSettings> = sharedPreferencesStorage.getAlarmSettings().distinctUntilChanged()

    override suspend fun saveNumberOfAlreadyRangAlarms(numberOfAlreadyRangAlarms: Int) {
        sharedPreferencesStorage.saveNumberOfAlreadyRangAlarms(numberOfAlreadyRangAlarms)
    }

    override fun getNumberOfAlreadyRangAlarms() = sharedPreferencesStorage.getNumberOfAlreadyRangAlarms()

    override fun getUpcomingAlarmTime(): Flow<LocalTime> = getAlarmsList()
            .combine(getNumberOfAlreadyRangAlarms()) { alarmsList, numberOfAlreadyRangAlarms ->
                alarmsList.get(numberOfAlreadyRangAlarms).time
            }

    companion object {
        val TAG = ScheduleSettingsRepository::class.java.simpleName
    }

}