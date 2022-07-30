package com.anshmidt.multialarm.data

import org.threeten.bp.LocalTime

object AlarmsConverter {

    fun getAlarmsList(
            firstAlarmTime: LocalTime,
            minutesBetweenAlarms: Int,
            numberOfAlarms: Int
    ): List<LocalTime> {

        val alarmsList: MutableList<LocalTime> = mutableListOf()

        var alarmTime = firstAlarmTime
        for (interval in 0..numberOfAlarms - 1) {
            alarmsList.add(alarmTime)
            alarmTime = alarmTime.plusMinutes(minutesBetweenAlarms.toLong())
        }

        return alarmsList
    }
}