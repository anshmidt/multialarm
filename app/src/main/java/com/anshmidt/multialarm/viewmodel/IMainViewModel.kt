package com.anshmidt.multialarm.viewmodel

import com.anshmidt.multialarm.data.AlarmSettings

interface IMainViewModel {
    fun onViewCreated()

    fun getFirstAlarmTimeDisplayable(): String

    fun onFirstAlarmTimeClicked()

    fun onIntervalBetweenAlarmsClicked()

    fun onNumberOfAlarmsClicked()

}