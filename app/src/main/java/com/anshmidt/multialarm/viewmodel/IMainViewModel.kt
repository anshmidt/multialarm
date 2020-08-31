package com.anshmidt.multialarm.viewmodel

import com.anshmidt.multialarm.data.AlarmSettings

interface IMainViewModel {

    fun getFirstAlarmTimeDisplayable(): String

    fun onFirstAlarmTimeClicked()

    fun onMinutesBetweenAlarmsClicked()

    fun onNumberOfAlarmsClicked()

}