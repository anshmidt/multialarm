package com.anshmidt.multialarm.view

interface MainView {


    fun displayFirstAlarmTime(firstAlarmTime: String)

    fun displayIntervalBetweenAlarms(intervalBetweenAlarmsMinutes: Int)

    fun displayNumberOfAlarms(numberOfAlarms: Int)


}