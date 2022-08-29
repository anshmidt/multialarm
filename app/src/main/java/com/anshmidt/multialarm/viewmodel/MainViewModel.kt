package com.anshmidt.multialarm.viewmodel

import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anshmidt.multialarm.alarmscheduler.AlarmScheduler
import com.anshmidt.multialarm.data.SingleLiveEvent
import com.anshmidt.multialarm.repository.IAppSettingRepository
import com.anshmidt.multialarm.repository.IScheduleSettingsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.first
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

    fun onAlarmSwitchChanged(switchView: View, switchState: Boolean) {
        _displayAlarmSwitchChangedMessage.value = switchState
        viewModelScope.launch(Dispatchers.IO) {
            scheduleSettingsRepository.saveAlarmSwitchState(switchState)
        }

        viewModelScope.launch(Dispatchers.IO) {
            scheduleSettingsRepository.getAlarmSettings().first { alarmSettings ->
                val newAlarmSettings = alarmSettings.copy(switchState = switchState)
                Log.d(TAG, "Alarm settings changed, that's why we reschedule alarms. Old settings: $alarmSettings . New alarm settings: $newAlarmSettings")
                alarmScheduler.reschedule(newAlarmSettings)
                return@first true
            }
        }
    }

    fun onViewCreated() {

    }

    fun onViewStarted() {
        viewModelScope.launch(Dispatchers.IO) {
            scheduleSettingsRepository.getAlarmSwitchState().collect { switchState ->
                _alarmSwitchState.postValue(switchState)
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
        _alarmSwitchState.value?.let {
            if (it) {
                _displayAlarmsResetMessage.postCall()
            }
        }
    }

}