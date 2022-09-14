package com.anshmidt.multialarm.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anshmidt.multialarm.alarmscheduler.AlarmScheduler
import com.anshmidt.multialarm.data.SingleLiveEvent
import com.anshmidt.multialarm.logging.Log
import com.anshmidt.multialarm.repository.IScheduleSettingsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MinutesBetweenAlarmsViewModel(
        private val scheduleSettingsRepository: IScheduleSettingsRepository,
        private val alarmScheduler: AlarmScheduler
) : ViewModel() {

    private val _openMinutesBetweenAlarmsDialog = SingleLiveEvent<Any>()
    val openMinutesBetweenAlarmsDialog: LiveData<Any>
        get() = _openMinutesBetweenAlarmsDialog

    private var _minutesBetweenAlarms = MutableLiveData<Int>()
    val minutesBetweenAlarms: LiveData<Int> = _minutesBetweenAlarms
    val selectedVariantIndex = MutableLiveData<Int>()
    val allAvailableVariants = listOf(2, 3, 4, 5, 6, 8, 10, 15, 20, 25, 30, 40, 60, 90, 120)


    fun onViewCreated() {
        viewModelScope.launch(Dispatchers.IO) {
            scheduleSettingsRepository.getMinutesBetweenAlarms().first { minutesBetweenAlarms ->
                _minutesBetweenAlarms.postValue(minutesBetweenAlarms)
                val selectedVariant = allAvailableVariants.indexOf(minutesBetweenAlarms)
                selectedVariantIndex.postValue(selectedVariant)
                return@first true
            }
        }
    }

    fun onMinutesBetweenAlarmsChangedByUser(newValueIndex: Int) {
        selectedVariantIndex.value = newValueIndex
    }

    fun onMinutesBetweenAlarmsClicked() {
        _openMinutesBetweenAlarmsDialog.call()
    }

    fun onOkButtonClickInMinutesBetweenAlarmsDialog() {
        if (selectedVariantIndex.value == null) {
            return
        }
        val selectedVariant = allAvailableVariants[selectedVariantIndex.value!!]

        _minutesBetweenAlarms.value = selectedVariant
        viewModelScope.launch(Dispatchers.IO) {
            scheduleSettingsRepository.getAlarmSettings().first { alarmSettings ->
                val newAlarmSettings = alarmSettings.copy(minutesBetweenAlarms = selectedVariant)
                Log.d(TAG, "Rescheduling alarm because minutesBetweenAlarms changed by user")
                alarmScheduler.reschedule(newAlarmSettings)
                scheduleSettingsRepository.saveMinutesBetweenAlarms(selectedVariant)
                return@first true
            }
        }
    }

    fun onCancelButtonClickInMinutesBetweenAlarmsDialog() {
        //close dialog without saving selected value
    }

    companion object {
        private val TAG = MinutesBetweenAlarmsViewModel::class.java.simpleName
    }

}