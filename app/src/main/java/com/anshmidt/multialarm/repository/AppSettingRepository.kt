package com.anshmidt.multialarm.repository

import com.anshmidt.multialarm.datasources.SharedPreferencesStorage

class AppSettingRepository(
        val sharedPreferencesStorage: SharedPreferencesStorage
) : IAppSettingRepository {

    override suspend fun saveNightModeSwitchState(nightModeSwitchState: Boolean) {
        sharedPreferencesStorage.saveNightModeSwitchState(nightModeSwitchState)
    }

    override fun getNightModeSwitchState() = sharedPreferencesStorage.getNightModeSwitchState()
}