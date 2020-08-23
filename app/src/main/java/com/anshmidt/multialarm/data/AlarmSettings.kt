package com.anshmidt.multialarm.data

import java.time.LocalTime
import java.util.*

data class AlarmSettings (
    var alarmSwitchState: Boolean,
    var firstAlarmTime: LocalTime,
    val intervalBetweenAlarmsMinutes: Int,
    val numberOfAlarms: Int
)