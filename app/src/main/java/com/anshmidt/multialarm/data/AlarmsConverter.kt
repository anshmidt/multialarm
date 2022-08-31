package com.anshmidt.multialarm.data

import org.threeten.bp.LocalTime

object AlarmsConverter {

    fun getAlarmsList(
            firstAlarmTime: LocalTime,
            minutesBetweenAlarms: Int,
            numberOfAlarms: Int
    ): List<Alarm> {

        val alarmsList: MutableList<Alarm> = mutableListOf()

        var alarmTime = firstAlarmTime
        for (interval in 0..numberOfAlarms - 1) {
            alarmsList.add(Alarm(
                    time = alarmTime,
                    isEnabled = true
            ))
            alarmTime = alarmTime.plusMinutes(minutesBetweenAlarms.toLong())
        }

        return alarmsList
    }

    fun getAlarmsList(
            firstAlarmTime: LocalTime,
            minutesBetweenAlarms: Int,
            numberOfAlarms: Int,
            numberOfAlreadyRangAlarms: Int
    ): List<Alarm> {
        val alarmsList: MutableList<Alarm> = mutableListOf()

        var alarmTime = firstAlarmTime
        var isAlarmEnabled: Boolean
        for (position in 0..numberOfAlarms - 1) {
            isAlarmEnabled = isAlarmEnabled(
                    alarmPosition = position,
                    numberOfAlreadyRangAlarms = numberOfAlreadyRangAlarms
            )
            alarmsList.add(Alarm(
                    time = alarmTime,
                    isEnabled = isAlarmEnabled
            ))
            alarmTime = alarmTime.plusMinutes(minutesBetweenAlarms.toLong())
        }

        return alarmsList
    }

    fun isAlarmEnabled(alarmPosition: Int, numberOfAlreadyRangAlarms: Int): Boolean {
        return (alarmPosition >= numberOfAlreadyRangAlarms)
    }
}