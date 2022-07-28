package com.anshmidt.multialarm.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.threeten.bp.LocalTime

class AlarmsListViewModel : ViewModel() {
    private var _alarms = MutableLiveData<List<LocalTime>>()
    val alarms: LiveData<List<LocalTime>> = _alarms

    fun onViewCreated() {
        _alarms.value = listOf(
                LocalTime.of(1, 9),
                LocalTime.of(2, 11),
                LocalTime.of(3, 11),
                LocalTime.of(4, 11),
                LocalTime.of(5, 11),
                LocalTime.of(6, 11)
        )
    }

}