package com.anshmidt.multialarm.repository

import kotlinx.coroutines.flow.Flow

interface IAppSettingRepository {
    suspend fun saveNightModeSwitchState(nightModeSwitchState: Boolean)
    fun getNightModeSwitchState(): Flow<Boolean>
}