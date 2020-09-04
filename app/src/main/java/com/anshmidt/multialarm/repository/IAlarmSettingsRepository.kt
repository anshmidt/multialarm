package com.anshmidt.multialarm.repository

import org.threeten.bp.LocalTime

interface IAlarmSettingsRepository {
    var alarmSwitchState: Boolean
    var firstAlarmTime: LocalTime
    var minutesBetweenAlarms: Int
    var numberOfAlarms: Int
    fun clearAll()
}