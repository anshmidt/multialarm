package com.anshmidt.multialarm.view.helpers

import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import com.anshmidt.multialarm.R
import com.anshmidt.multialarm.repository.IAppSettingRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class AppThemeSelector(
        private val appSettingRepository: IAppSettingRepository
) {

    fun setBackgroundImageBasedOnTheme(view: View) {
        runBlocking {
            appSettingRepository.getNightModeSwitchState()
                .first()
                .let { nightModeSwitchState ->
                setBackgroundImage(view, nightModeSwitchState)
            }
        }
    }

    private fun setBackgroundImage(view: View, isNightModeOn: Boolean) {
        view.background = if (isNightModeOn) {
            ContextCompat.getDrawable(view.context, R.drawable.img_background_main_desaturated_2_night)
        } else {
            ContextCompat.getDrawable(view.context, R.drawable.img_background_main)
        }

    }

    fun checkAndShowTheme() {
        runBlocking {
            appSettingRepository.getNightModeSwitchState()
                .first()
                .let { nightModeSwitchState ->
                    selectAppTheme(nightModeSwitchState)
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