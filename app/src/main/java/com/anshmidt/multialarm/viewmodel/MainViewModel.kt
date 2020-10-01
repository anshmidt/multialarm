package com.anshmidt.multialarm.viewmodel

import androidx.lifecycle.*
import com.anshmidt.multialarm.repository.ISettingsRepository

open class MainViewModel(
    private val repository: ISettingsRepository
) : ViewModel() {


    companion object {
        private val TAG = MainViewModel::class.java.simpleName
        val TIME_LEFT_REFRESH_INTERVAL_SECONDS = 10
    }


    var alarmTurnedOn: Boolean
        get() = repository.alarmTurnedOn
        set(value) {
            repository.alarmTurnedOn = value
        }



}