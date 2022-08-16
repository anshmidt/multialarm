package com.anshmidt.multialarm.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.anshmidt.multialarm.alarmscheduler.AlarmScheduler
import com.anshmidt.multialarm.data.SingleLiveEvent
import com.anshmidt.multialarm.repository.IScheduleSettingsRepository

class MinutesBetweenAlarmsViewModel(
        private val scheduleSettingsRepository: IScheduleSettingsRepository,
        private val alarmScheduler: AlarmScheduler
) : ViewModel() {
    private val _openMinutesBetweenAlarmsDialog = SingleLiveEvent<Any>()
    val openMinutesBetweenAlarmsDialog: LiveData<Any>
        get() = _openMinutesBetweenAlarmsDialog


    var _minutesBetweenAlarms = MutableLiveData<Int>()
    val minutesBetweenAlarms: LiveData<Int> = _minutesBetweenAlarms
    val selectedVariantIndex = MutableLiveData<Int>()
    val allAvailableVariants = listOf(2, 3, 4, 5, 6, 8, 10, 15, 20, 25, 30, 40, 60, 90, 120)


    fun onViewCreated() {
        _minutesBetweenAlarms.value = scheduleSettingsRepository.minutesBetweenAlarms
        selectedVariantIndex.value = allAvailableVariants.indexOf(_minutesBetweenAlarms.value!!)
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
        scheduleSettingsRepository.minutesBetweenAlarms = selectedVariant
        alarmScheduler.reschedule(scheduleSettingsRepository.getSettings())
    }

    fun onCancelButtonClickInMinutesBetweenAlarmsDialog() {
        //close dialog without saving selected value
    }

}