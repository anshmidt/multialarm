package com.anshmidt.multialarm.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anshmidt.multialarm.data.AlarmListEntry
import com.anshmidt.multialarm.repository.IScheduleSettingsRepository
import kotlinx.coroutines.launch

class AlarmsListViewModel(
    private val scheduleSettingsRepository: IScheduleSettingsRepository
) : ViewModel() {

    private var _alarms = MutableLiveData<List<AlarmListEntry>>()
    val alarms: LiveData<List<AlarmListEntry>> = _alarms

    fun onViewStarted() {
        viewModelScope.launch {
            scheduleSettingsRepository.getAlarmsList()
                .collect { alarmsList ->
                    _alarms.postValue(alarmsList)
                }
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