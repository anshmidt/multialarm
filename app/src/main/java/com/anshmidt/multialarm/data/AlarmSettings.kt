package com.anshmidt.multialarm.data

data class AlarmSettings(
    val areOn: Boolean,
    val firstAlarmTimeMillis: Long,
    val minutesBetweenAlarms: Int,
    val numberOfAlarms: Int,
    val numberOfAlreadyRangAlarms: Int
)