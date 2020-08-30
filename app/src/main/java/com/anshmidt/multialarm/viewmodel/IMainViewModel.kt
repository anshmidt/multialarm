package com.anshmidt.multialarm.viewmodel

import com.anshmidt.multialarm.data.AlarmSettings

interface IMainViewModel {
    fun onViewCreated()

    fun onAlarmSwitchTurnedOn()
    fun onAlarmSwitchTurnedOff()

    fun onAlarmSwitchStateChanged(isTurnedOn: Boolean)

    fun onFirstAlarmTimeClicked()

    fun onIntervalBetweenAlarmsClicked()

    fun onNumberOfAlarmsClicked()

    fun getAlarmSettingsFromModel(): AlarmSettings
}