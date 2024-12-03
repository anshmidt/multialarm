package com.anshmidt.multialarm.repository

import com.anshmidt.multialarm.datasources.SharedPreferencesStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class AppSettingRepository(
    val sharedPreferencesStorage: SharedPreferencesStorage
) : IAppSettingRepository {

    override suspend fun saveNightModeSwitchState(nightModeSwitchState: Boolean) {
        withContext(Dispatchers.IO) {
            sharedPreferencesStorage.saveNightModeSwitchState(nightModeSwitchState)
        }
    }

    override fun getNightModeSwitchState() =
        sharedPreferencesStorage.getNightModeSwitchState().flowOn(Dispatchers.IO)
}