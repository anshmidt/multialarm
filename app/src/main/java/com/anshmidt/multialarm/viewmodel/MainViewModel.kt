package com.anshmidt.multialarm.viewmodel

import androidx.lifecycle.ViewModel
import com.anshmidt.multialarm.alarmscheduler.AlarmScheduler
import com.anshmidt.multialarm.repository.IScheduleSettingsRepository

class MainViewModel(
        private val scheduleSettingsRepository: IScheduleSettingsRepository,
        private val alarmScheduler: AlarmScheduler
) : ViewModel() {


    companion object {
        private val TAG = MainViewModel::class.java.simpleName
    }


    var alarmTurnedOn: Boolean
        get() = scheduleSettingsRepository.alarmTurnedOn
        set(value) {
            scheduleSettingsRepository.alarmTurnedOn = value
            alarmScheduler.reschedule(scheduleSettingsRepository.getSettings())
        }



}