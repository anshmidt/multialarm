package com.anshmidt.multialarm.viewmodel

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.anshmidt.multialarm.alarmscheduler.AlarmScheduler
import com.anshmidt.multialarm.data.SingleLiveEvent
import com.anshmidt.multialarm.repository.IScheduleSettingsRepository

class MainViewModel(
        private val scheduleSettingsRepository: IScheduleSettingsRepository,
        private val alarmScheduler: AlarmScheduler
) : ViewModel() {


    companion object {
        private val TAG = MainViewModel::class.java.simpleName
    }


//    var alarmTurnedOn: Boolean
//        get() = scheduleSettingsRepository.alarmTurnedOn
//        set(value) {
//            scheduleSettingsRepository.alarmTurnedOn = value
//            alarmScheduler.reschedule(scheduleSettingsRepository.getSettings())
//        }

    private var _alarmTurnedOn = MutableLiveData<Boolean>()
    val alarmTurnedOn: LiveData<Boolean> = _alarmTurnedOn

    private var _displayAlarmSwitchChangedMessage = SingleLiveEvent<Boolean>()
    val displayAlarmSwitchChangedMessage: LiveData<Boolean> = _displayAlarmSwitchChangedMessage

    fun onViewCreated() {
        _alarmTurnedOn.value = scheduleSettingsRepository.alarmTurnedOn
    }

    fun onAlarmSwitchChanged(switchView: View, switchState: Boolean) {
        _displayAlarmSwitchChangedMessage.value = switchState
        scheduleSettingsRepository.alarmTurnedOn = switchState
        alarmScheduler.reschedule(scheduleSettingsRepository.getSettings())
    }

}