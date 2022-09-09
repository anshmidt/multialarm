package com.anshmidt.multialarm.view.helpers

import androidx.appcompat.app.AppCompatDelegate
import com.anshmidt.multialarm.repository.IAppSettingRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first

class AppThemeSelector(
        private val appSettingRepository: IAppSettingRepository
) {

    fun checkAndShowTheme() {
        runBlocking {
            appSettingRepository.getNightModeSwitchState().first { nightModeSwitchState ->
                selectAppTheme(nightModeSwitchState)
                return@first true
            }
        }
    }

    fun showTheme(isNightModeOn: Boolean) {
        runBlocking {
            selectAppTheme(isNightModeOn)
        }
    }

    private fun selectAppTheme(isNightModeOn: Boolean) {
        if (isNightModeOn) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }
}