package com.anshmidt.multialarm.data

import java.time.LocalTime
import java.util.*

data class AlarmSettings (
    var alarmTurnedOn: Boolean,
    var firstAlarmTime: LocalTime,
    val intervalBetweenAlarmsMinutes: Int,
    val numberOfAlarms: Int
)