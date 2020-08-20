package com.anshmidt.multialarm.viewmodel

import com.anshmidt.multialarm.data.AlarmSettings

interface IMainViewModel {
    fun onAlarmSwitchTurnedOn()
    fun onAlarmSwitchTurnedOff()

    fun onFirstAlarmTimeClicked()

    fun onIntervalBetweenAlarmsClicked()

    fun onNumberOfAlarmsClicked()

    fun getAlarmSettingsFromModel(): AlarmSettings
}