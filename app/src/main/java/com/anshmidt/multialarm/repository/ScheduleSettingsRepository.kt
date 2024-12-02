package com.anshmidt.multialarm.repository

import com.anshmidt.multialarm.data.AlarmListEntry
import com.anshmidt.multialarm.data.AlarmSettings
import com.anshmidt.multialarm.data.getAlarmsList
import com.anshmidt.multialarm.datasources.SharedPreferencesStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

class ScheduleSettingsRepository(
    private val sharedPreferencesStorage: SharedPreferencesStorage
) : IScheduleSettingsRepository {

    override fun getAlarmSettings(): Flow<AlarmSettings> =
        sharedPreferencesStorage.getAlarmSettings().distinctUntilChanged()

    override suspend fun saveAlarmSettings(alarmSettings: AlarmSettings) {
        sharedPreferencesStorage.saveAlarmSettings(alarmSettings)
    }

    override fun getAlarmsList(): Flow<List<AlarmListEntry>> =
        getAlarmSettings().map { it.getAlarmsList() }

    companion object {
        val TAG = ScheduleSettingsRepository::class.java.simpleName
    }

}