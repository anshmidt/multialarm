package com.anshmidt.multialarm.repository

import android.net.Uri
import org.threeten.bp.LocalTime

interface IAlarmSettingsRepository {
    var alarmSwitchState: Boolean
    var firstAlarmTime: LocalTime
    var minutesBetweenAlarms: Int
    var numberOfAlarms: Int
    var songDurationSeconds: Int
    fun clearAll()
    val songUri: Uri
}