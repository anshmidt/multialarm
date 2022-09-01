package com.anshmidt.multialarm.repository

import com.anshmidt.multialarm.data.Alarm
import com.anshmidt.multialarm.data.AlarmSettings
import com.anshmidt.multialarm.data.AlarmsConverter
import com.anshmidt.multialarm.datasources.DataStoreStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.zip
import org.threeten.bp.LocalTime

class ScheduleSettingsRepository(
        private val dataStoreStorage: DataStoreStorage
) : IScheduleSettingsRepository {

    override suspend fun saveAlarmSwitchState(switchState: Boolean) {
        dataStoreStorage.saveAlarmSwitchState(switchState)
    }

    override fun getAlarmSwitchState(): Flow<Boolean> = dataStoreStorage.getAlarmSwitchState()

    override suspend fun saveMinutesBetweenAlarms(minutesBetweenAlarms: Int) {
        dataStoreStorage.saveMinutesBetweenAlarms(minutesBetweenAlarms)
    }

    override fun getMinutesBetweenAlarms(): Flow<Int> = dataStoreStorage.getMinutesBetweenAlarms()

    override suspend fun saveNumberOfAlarms(numberOfAlarms: Int) {
        dataStoreStorage.saveNumberOfAlarms(numberOfAlarms)
    }

    override fun getNumberOfAlarms(): Flow<Int> = dataStoreStorage.getNumberOfAlarms()

    override suspend fun saveFirstAlarmTime(firstAlarmTime: LocalTime) {
        dataStoreStorage.saveFirstAlarmHours(firstAlarmTime.hour)
        dataStoreStorage.saveFirstAlarmMinutes(firstAlarmTime.minute)
    }

    override fun getFirstAlarmTime(): Flow<LocalTime> = dataStoreStorage.getFirstAlarmHours()
            .zip(dataStoreStorage.getFirstAlarmMinutes()) { hours, minutes ->
                LocalTime.of(hours, minutes)
            }

//    override fun getAlarmsList(): Flow<List<Alarm>> = dataStoreStorage.getAlarmSettings().map {
//        AlarmsConverter.getAlarmsList(
//                firstAlarmTime = it.firstAlarmTime,
//                minutesBetweenAlarms = it.minutesBetweenAlarms,
//                numberOfAlarms = it.numberOfAlarms
//        )
//    }.distinctUntilChanged()

    override fun getAlarmsList(): Flow<List<Alarm>> = dataStoreStorage.getNumberOfAlreadyRangAlarms()
            .combine(dataStoreStorage.getAlarmSettings()) { numberOfAlreadyRangAlarms, alarmSettings ->
                AlarmsConverter.getAlarmsList(
                        firstAlarmTime = alarmSettings.firstAlarmTime,
                        minutesBetweenAlarms = alarmSettings.minutesBetweenAlarms,
                        numberOfAlarms = alarmSettings.numberOfAlarms,
                        numberOfAlreadyRangAlarms = numberOfAlreadyRangAlarms
                )
            }

    override fun getAlarmSettings(): Flow<AlarmSettings> = dataStoreStorage.getAlarmSettings().distinctUntilChanged()

    override suspend fun saveNumberOfAlreadyRangAlarms(numberOfAlreadyRangAlarms: Int) {
        dataStoreStorage.saveNumberOfAlreadyRangAlarms(numberOfAlreadyRangAlarms)
    }

    override fun getNumberOfAlreadyRangAlarms() = dataStoreStorage.getNumberOfAlreadyRangAlarms()

    companion object {
        val TAG = ScheduleSettingsRepository::class.java.simpleName
    }

}