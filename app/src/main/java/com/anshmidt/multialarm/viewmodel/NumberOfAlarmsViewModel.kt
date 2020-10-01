package com.anshmidt.multialarm.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.anshmidt.multialarm.SingleLiveEvent
import com.anshmidt.multialarm.repository.ISettingsRepository

class NumberOfAlarmsViewModel(
        private val repository: ISettingsRepository
) : ViewModel()  {

    private val _openNumberOfAlarmsDialog = SingleLiveEvent<Any>()
    val openNumberOfAlarmsDialog: LiveData<Any>
        get() = _openNumberOfAlarmsDialog


    private var _numberOfAlarms = MutableLiveData<Int>()
    val numberOfAlarms: LiveData<Int> = _numberOfAlarms
    val selectedVariantIndex = MutableLiveData<Int>()
    val allAvailableVariants: List<Int> = List(200) { i -> i + 1 }  // 1, 2, 3, ...


    fun onViewCreated() {
        _numberOfAlarms.value = repository.numberOfAlarms
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
        _numberOfAlarms.value = allAvailableVariants[selectedVariantIndex.value!!]
        repository.numberOfAlarms = allAvailableVariants[selectedVariantIndex.value!!]
    }

    fun onCancelButtonClickInNumberOfAlarmsDialog() {
        //close dialog without saving selected value
    }

}