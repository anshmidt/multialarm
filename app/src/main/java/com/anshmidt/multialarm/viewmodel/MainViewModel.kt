package com.anshmidt.multialarm.viewmodel

import androidx.lifecycle.*
import com.anshmidt.multialarm.alarmscheduler.AlarmScheduler
import com.anshmidt.multialarm.repository.ISettingsRepository

open class MainViewModel(
    private val repository: ISettingsRepository,
    private val alarmScheduler: AlarmScheduler
) : ViewModel() {


    companion object {
        private val TAG = MainViewModel::class.java.simpleName
    }


    var alarmTurnedOn: Boolean
        get() = repository.alarmTurnedOn
        set(value) {
            repository.alarmTurnedOn = value
            alarmScheduler.reschedule(repository.getSettings())
        }



}