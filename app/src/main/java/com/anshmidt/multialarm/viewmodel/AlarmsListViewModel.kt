package com.anshmidt.multialarm.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anshmidt.multialarm.repository.IScheduleSettingsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.threeten.bp.LocalTime

class AlarmsListViewModel(
        private val scheduleSettingsRepository: IScheduleSettingsRepository
) : ViewModel() {

    private var _alarms = MutableLiveData<List<LocalTime>>()
    val alarms: LiveData<List<LocalTime>> = _alarms

    private var _alarmTurnedOn = MutableLiveData<Boolean>()
    val alarmTurnedOn: LiveData<Boolean> = _alarmTurnedOn

    fun onViewStarted() {
        viewModelScope.launch(Dispatchers.IO) {
            scheduleSettingsRepository.getAlarmSwitchState().collect { switchState ->
                onAlarmSwitchChanged(switchState)
            }
        }

        viewModelScope.launch(Dispatchers.IO) {
            scheduleSettingsRepository.getAlarmsList().collect { alarmsList ->
                onAlarmsListChanged(alarmsList)
            }
        }

    }

    private fun onAlarmsListChanged(alarmsList: List<LocalTime>) {
        _alarms.postValue(alarmsList)
    }

    private fun onAlarmSwitchChanged(alarmTurnedOn: Boolean) {
        _alarmTurnedOn.postValue(alarmTurnedOn)
    }

    fun onViewStopped() {
    }

    fun onViewDestroyed() {
    }

    companion object {
        private val TAG = AlarmsListViewModel::class.java.simpleName
    }

}