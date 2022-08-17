package com.anshmidt.multialarm.viewmodel

import android.view.View
import androidx.lifecycle.LiveData
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

    // used only for displaying the initial switch state when the view is created
    val alarmTurnedOn: Boolean
        get() = scheduleSettingsRepository.alarmTurnedOn

    private var _displayAlarmSwitchChangedMessage = SingleLiveEvent<Boolean>()
    val displayAlarmSwitchChangedMessage: LiveData<Boolean> = _displayAlarmSwitchChangedMessage

    fun onAlarmSwitchChanged(switchView: View, switchState: Boolean) {
        _displayAlarmSwitchChangedMessage.value = switchState
        scheduleSettingsRepository.alarmTurnedOn = switchState
        alarmScheduler.reschedule(scheduleSettingsRepository.getSettings())
    }

}