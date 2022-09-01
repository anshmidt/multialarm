package com.anshmidt.multialarm.repository

import com.anshmidt.multialarm.data.Alarm
import com.anshmidt.multialarm.data.AlarmSettings
import kotlinx.coroutines.flow.Flow
import org.threeten.bp.LocalTime

interface IScheduleSettingsRepository {

    suspend fun saveAlarmSwitchState(switchState: Boolean)
    fun getAlarmSwitchState(): Flow<Boolean>
    suspend fun saveMinutesBetweenAlarms(minutesBetweenAlarms: Int)
    fun getMinutesBetweenAlarms(): Flow<Int>
    suspend fun saveNumberOfAlarms(numberOfAlarms: Int)
    fun getNumberOfAlarms(): Flow<Int>
    suspend fun saveFirstAlarmTime(firstAlarmTime: LocalTime)
    fun getFirstAlarmTime(): Flow<LocalTime>
    fun getAlarmsList(): Flow<List<Alarm>>
    fun getAlarmSettings(): Flow<AlarmSettings>
    suspend fun saveNumberOfAlreadyRangAlarms(numberOfAlreadyRangAlarms: Int)
    fun getNumberOfAlreadyRangAlarms(): Flow<Int>
}