package com.anshmidt.multialarm.repository

import com.anshmidt.multialarm.data.AlarmListEntry
import com.anshmidt.multialarm.data.AlarmSettings
import com.anshmidt.multialarm.data.getAlarmsList
import com.anshmidt.multialarm.datasources.SharedPreferencesStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class ScheduleSettingsRepository(
    private val sharedPreferencesStorage: SharedPreferencesStorage
) : IScheduleSettingsRepository {

    override fun getAlarmSettings(): Flow<AlarmSettings> =
        sharedPreferencesStorage.getAlarmSettings().distinctUntilChanged().flowOn(Dispatchers.IO)

    override suspend fun saveAlarmSettings(alarmSettings: AlarmSettings) {
        withContext(Dispatchers.IO) {
            sharedPreferencesStorage.saveAlarmSettings(alarmSettings)
        }
    }

    override fun getAlarmsList(): Flow<List<AlarmListEntry>> =
        getAlarmSettings().map { it.getAlarmsList() }.flowOn(Dispatchers.IO)

    companion object {
        val TAG = ScheduleSettingsRepository::class.java.simpleName
    }

}