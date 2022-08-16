package com.anshmidt.multialarm.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.anshmidt.multialarm.alarmscheduler.AlarmScheduler
import com.anshmidt.multialarm.data.SingleLiveEvent
import com.anshmidt.multialarm.repository.IScheduleSettingsRepository

class NumberOfAlarmsViewModel(
        private val scheduleSettingsRepository: IScheduleSettingsRepository,
        private val alarmScheduler: AlarmScheduler
) : ViewModel()  {

    private val _openNumberOfAlarmsDialog = SingleLiveEvent<Any>()
    val openNumberOfAlarmsDialog: LiveData<Any>
        get() = _openNumberOfAlarmsDialog


    private var _numberOfAlarms = MutableLiveData<Int>()
    val numberOfAlarms: LiveData<Int> = _numberOfAlarms
    val selectedVariantIndex = MutableLiveData<Int>()
    val allAvailableVariants: List<Int> = List(200) { i -> i + 1 }  // 1, 2, 3, ...


    fun onViewCreated() {
        _numberOfAlarms.value = scheduleSettingsRepository.numberOfAlarms
        selectedVariantIndex.value = allAvailableVariants.indexOf(_numberOfAlarms.value!!)
    }

    fun onNumberOfAlarmsChangedByUser(newValueIndex: Int) {
        selectedVariantIndex.value = newValueIndex
    }

    fun onNumberOfAlarmsClicked() {
        _openNumberOfAlarmsDialog.call()
    }

    fun onOkButtonClickInNumberOfAlarmsDialog() {
        if (selectedVariantIndex.value == null) {
            return
        }
        val selectedVariant = allAvailableVariants[selectedVariantIndex.value!!]
        _numberOfAlarms.value = selectedVariant
        scheduleSettingsRepository.numberOfAlarms = selectedVariant
        alarmScheduler.reschedule(scheduleSettingsRepository.getSettings())
    }

    fun onCancelButtonClickInNumberOfAlarmsDialog() {
        //close dialog without saving selected value
    }

}