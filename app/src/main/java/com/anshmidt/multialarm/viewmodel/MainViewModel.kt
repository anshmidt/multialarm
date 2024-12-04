package com.anshmidt.multialarm.viewmodel

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anshmidt.multialarm.alarmscheduler.AlarmScheduler
import com.anshmidt.multialarm.data.SingleLiveEvent
import com.anshmidt.multialarm.logging.Log
import com.anshmidt.multialarm.repository.IAppSettingRepository
import com.anshmidt.multialarm.repository.IScheduleSettingsRepository
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class MainViewModel(
        private val scheduleSettingsRepository: IScheduleSettingsRepository,
        private val appSettingRepository: IAppSettingRepository,
        private val alarmScheduler: AlarmScheduler
) : ViewModel() {

    companion object {
        private val TAG = MainViewModel::class.java.simpleName
    }

    private var _alarmSwitchState = MutableLiveData<Boolean>()
    val alarmSwitchState: LiveData<Boolean> = _alarmSwitchState

    private var _displayAlarmSwitchChangedMessage = SingleLiveEvent<Boolean>()
    val displayAlarmSwitchChangedMessage: LiveData<Boolean> = _displayAlarmSwitchChangedMessage

    private var _displayAlarmsResetMessage = SingleLiveEvent<Any>()
    val displayAlarmsResetMessage: LiveData<Any> = _displayAlarmsResetMessage

    private var _isNightModeOn = MutableLiveData<Boolean>()
    val isNightModeOn: LiveData<Boolean> = _isNightModeOn

    fun onAlarmSwitchChanged(switchView: View, newSwitchState: Boolean) {
        _displayAlarmSwitchChangedMessage.value = newSwitchState

        viewModelScope.launch {
            scheduleSettingsRepository.getAlarmSettings()
                .distinctUntilChanged { old, new -> old.areOn == new.areOn }
                .first { alarmSettings ->
                    val newAlarmSettings = alarmSettings.copy(areOn = newSwitchState)
                    Log.d(TAG, "Rescheduling alarm because switch state changed. Old settings: $alarmSettings . New alarm settings: $newAlarmSettings")
                    alarmScheduler.rescheduleAlarms(newAlarmSettings)
                    return@first true
                }
        }
    }

    fun onViewCreated() {

    }

    fun onViewStarted() {
        viewModelScope.launch {
            scheduleSettingsRepository.getAlarmSettings()
                .map { it.areOn }
                .distinctUntilChanged()
                .collect { switchState ->
                    _alarmSwitchState.postValue(switchState)
                }
        }

        viewModelScope.launch {
            scheduleSettingsRepository.getAlarmSettings()
                .distinctUntilChanged { old, new -> old.areOn != new.areOn } // ignore switch state changes
                .drop(1) // ignore initial value since we're only interested in changes
                .collect { onAlarmSettingsChanged() }
        }

        viewModelScope.launch {
            appSettingRepository
                .getNightModeSwitchState()
                .collect {
                    _isNightModeOn.postValue(it)
                }
        }
    }

    fun onViewStopped() {

    }

    fun onViewDestroyed() {

    }

    private fun onAlarmSettingsChanged() {
        _alarmSwitchState.value?.let {
            if (it) { // Makes no sense to show the reset message if alarms are turned off
                _displayAlarmsResetMessage.postCall()
            }
        }
    }

}