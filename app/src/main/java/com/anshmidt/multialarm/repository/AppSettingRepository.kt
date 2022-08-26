package com.anshmidt.multialarm.repository

import com.anshmidt.multialarm.datasources.DataStoreStorage

class AppSettingRepository(
        val dataStoreStorage: DataStoreStorage
) : IAppSettingRepository {

    override suspend fun saveNightModeSwitchState(nightModeSwitchState: Boolean) {
        dataStoreStorage.saveNightModeSwitchState(nightModeSwitchState)
    }

    override fun getNightModeSwitchState() = dataStoreStorage.getNightModeSwitchState()
}