package com.anshmidt.multialarm.data

import org.threeten.bp.LocalTime
import java.util.*

data class AlarmSettings (
        var alarmSwitchState: Boolean,
        var firstAlarmTime: LocalTime,
        val minutesBetweenAlarms: Int,
        val numberOfAlarms: Int
)