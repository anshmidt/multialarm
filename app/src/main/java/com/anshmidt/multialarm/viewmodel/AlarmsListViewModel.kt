package com.anshmidt.multialarm.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anshmidt.multialarm.data.Alarm
import com.anshmidt.multialarm.repository.IScheduleSettingsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class AlarmsListViewModel(
        private val scheduleSettingsRepository: IScheduleSettingsRepository
) : ViewModel() {

    private var _alarms = MutableLiveData<List<Alarm>>()
    val alarms: LiveData<List<Alarm>> = _alarms

    fun onViewStarted() {
        viewModelScope.launch(Dispatchers.IO) {
            scheduleSettingsRepository.getAlarmSwitchState().combine(
                scheduleSettingsRepository.getAlarmsList()
            ) { switchState, alarmsList ->
                if (switchState) {
                    return@combine alarmsList
                } else {
                    // If switch is turned off, all alarms are displayed as disabled
                    return@combine getDisabledAlarms(alarmsList)
                }
            }.collect { alarmsList ->
                _alarms.postValue(alarmsList)
            }
        }

    }

    private fun getDisabledAlarms(alarmsList: List<Alarm>): List<Alarm> {
        return alarmsList.map { alarm ->
            alarm.copy(isEnabled = false)
        }
    }

    fun onViewStopped() {
    }

    fun onViewDestroyed() {
    }

    companion object {
        private val TAG = AlarmsListViewModel::class.java.simpleName
    }

}