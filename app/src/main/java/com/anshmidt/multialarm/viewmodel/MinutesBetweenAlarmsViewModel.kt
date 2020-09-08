package com.anshmidt.multialarm.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.anshmidt.multialarm.SingleLiveEvent
import com.anshmidt.multialarm.repository.IAlarmSettingsRepository

class MinutesBetweenAlarmsViewModel(
        private val repository: IAlarmSettingsRepository
) : ViewModel() {
    private val _openMinutesBetweenAlarmsDialog = SingleLiveEvent<Any>()
    val openMinutesBetweenAlarmsDialog: LiveData<Any>
        get() = _openMinutesBetweenAlarmsDialog


    var _minutesBetweenAlarms = MutableLiveData<Int>()
    val minutesBetweenAlarmsLiveData: LiveData<Int> = _minutesBetweenAlarms
    val minutesBetweenAlarmsVariantIndex = MutableLiveData<Int>()
    val minutesBetweenAlarmsAllAvailableVariants = listOf(2, 3, 4, 5, 6, 8, 10, 15, 20, 25, 30, 40, 60, 90, 120)


    fun onViewCreated() {
        _minutesBetweenAlarms.value = repository.minutesBetweenAlarms
        minutesBetweenAlarmsVariantIndex.value = minutesBetweenAlarmsAllAvailableVariants.indexOf(_minutesBetweenAlarms.value!!)
    }

    fun onMinutesBetweenAlarmsChangedByUser(newValueIndex: Int) {
        minutesBetweenAlarmsVariantIndex.value = newValueIndex
    }

    fun onMinutesBetweenAlarmsClicked() {
        _openMinutesBetweenAlarmsDialog.call()
    }

    fun onOkButtonClickInMinutesBetweenAlarmsDialog() {
        if (minutesBetweenAlarmsVariantIndex.value == null) {
            return
        }
        _minutesBetweenAlarms.value = minutesBetweenAlarmsAllAvailableVariants[minutesBetweenAlarmsVariantIndex.value!!]
        repository.minutesBetweenAlarms = minutesBetweenAlarmsAllAvailableVariants[minutesBetweenAlarmsVariantIndex.value!!]
    }

    fun onCancelButtonClickInMinutesBetweenAlarmsDialog() {
        //close dialog without saving selected value
    }

}