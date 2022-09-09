package com.anshmidt.multialarm.data

import org.threeten.bp.LocalTime

data class AlarmSettings (
        var switchState: Boolean,

        /**
         * Always use org.threeten.bp.LocalTime and not java.time in order to support API < 26
         */
        var firstAlarmTime: LocalTime,

        val minutesBetweenAlarms: Int,
        val numberOfAlarms: Int
)