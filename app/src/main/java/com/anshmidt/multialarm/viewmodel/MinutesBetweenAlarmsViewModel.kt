package com.anshmidt.multialarm.viewmodel

import android.util.Log
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


    private var _minutesBetweenAlarms = MutableLiveData<Int>()
    val minutesBetweenAlarms: LiveData<Int> = _minutesBetweenAlarms
    val selectedVariantIndex = MutableLiveData<Int>()
    val allAvailableVariants = listOf(2, 3, 4, 5, 6, 8, 10, 15, 20, 25, 30, 40, 60, 90, 120)


    fun onViewCreated() {
        _minutesBetweenAlarms.value = repository.minutesBetweenAlarms
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
        _minutesBetweenAlarms.value = allAvailableVariants[selectedVariantIndex.value!!]
        repository.minutesBetweenAlarms = allAvailableVariants[selectedVariantIndex.value!!]
    }

    fun onCancelButtonClickInMinutesBetweenAlarmsDialog() {
        //close dialog without saving selected value
    }

}