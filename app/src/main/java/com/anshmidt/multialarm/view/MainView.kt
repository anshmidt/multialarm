package com.anshmidt.multialarm.view

interface MainView {

    fun displayAlarmSwitchState(switchState: Boolean)

    fun displayFirstAlarmTime(firstAlarmTime: String)

    fun displayIntervalBetweenAlarms(intervalBetweenAlarmsMinutes: Int)

    fun displayNumberOfAlarms(numberOfAlarms: Int)


}