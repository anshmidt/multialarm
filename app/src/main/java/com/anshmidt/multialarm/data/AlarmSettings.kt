package com.anshmidt.multialarm.data

import org.threeten.bp.LocalTime
import java.util.*

data class AlarmSettings (
        var alarmSwitchState: Boolean,

        /**
         * Important note:
         * Always use org.threeten.bp.LocalTime and not java.time in order to support API < 26
         */
        var firstAlarmTime: LocalTime,

        val minutesBetweenAlarms: Int,
        val numberOfAlarms: Int,
        val songDurationSeconds: Int
)