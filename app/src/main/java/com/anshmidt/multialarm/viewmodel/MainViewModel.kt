package com.anshmidt.multialarm.viewmodel

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anshmidt.multialarm.alarmscheduler.AlarmScheduler
import com.anshmidt.multialarm.data.SingleLiveEvent
import com.anshmidt.multialarm.repository.IScheduleSettingsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.launch

class MainViewModel(
        private val scheduleSettingsRepository: IScheduleSettingsRepository,
        private val alarmScheduler: AlarmScheduler
) : ViewModel() {

    companion object {
        private val TAG = MainViewModel::class.java.simpleName
    }

    // used only for displaying the initial switch state when the view is created
    var alarmSwitchState: Boolean = false

    private var _displayAlarmSwitchChangedMessage = SingleLiveEvent<Boolean>()
    val displayAlarmSwitchChangedMessage: LiveData<Boolean> = _displayAlarmSwitchChangedMessage

    private var _displayAlarmsResetMessage = SingleLiveEvent<Any>()
    val displayAlarmsResetMessage: LiveData<Any> = _displayAlarmsResetMessage

    fun onAlarmSwitchChanged(switchView: View, switchState: Boolean) {
        _displayAlarmSwitchChangedMessage.value = switchState
        viewModelScope.launch(Dispatchers.IO) {
            scheduleSettingsRepository.saveAlarmSwitchState(switchState)
        }

        viewModelScope.launch(Dispatchers.IO) {
            scheduleSettingsRepository.getAlarmSettings().collect { alarmSettings ->
                val newAlarmSettings = alarmSettings.copy(switchState = switchState)
                alarmScheduler.reschedule(newAlarmSettings)
            }
        }
    }

    fun onViewStarted() {
        viewModelScope.launch(Dispatchers.IO) {
            scheduleSettingsRepository.getAlarmSwitchState().collect { switchState ->
                alarmSwitchState = switchState
            }
        }

        viewModelScope.launch(Dispatchers.IO) {
            scheduleSettingsRepository.getAlarmsList()
                    .drop(1) // ignore initial value since we're only interested when alarm list changes
                    .collect {
                onAlarmsListChanged()
            }
        }
    }

    fun onViewStopped() {

    }

    fun onViewDestroyed() {

    }

    private fun onAlarmsListChanged() {
        _displayAlarmsResetMessage.postCall()
    }

}