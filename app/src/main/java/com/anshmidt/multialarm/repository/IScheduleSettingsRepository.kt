package com.anshmidt.multialarm.repository

import com.anshmidt.multialarm.data.AlarmListEntry
import com.anshmidt.multialarm.data.AlarmSettings
import kotlinx.coroutines.flow.Flow

interface IScheduleSettingsRepository {
    fun getAlarmsList(): Flow<List<AlarmListEntry>>
    fun getAlarmSettings(): Flow<AlarmSettings>
    suspend fun saveAlarmSettings(alarmSettings: AlarmSettings)
}