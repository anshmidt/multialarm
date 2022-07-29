package com.anshmidt.multialarm

import com.anshmidt.multialarm.data.AlarmsConverter
import org.junit.Assert
import org.junit.Test
import org.threeten.bp.LocalTime

class AlarmsConverterTest {

    @Test
    fun getAlarmsList_happyPath() {
        val firstAlarmTime = LocalTime.NOON
        val numberOfAlarms = 2
        val minutesBetweenAlarms = 10

        val expectedAlarmsList = listOf(
                LocalTime.NOON,
                LocalTime.of(12, 10)
        )

        val actualAlarmsList = AlarmsConverter.getAlarmsList(
                firstAlarmTime = firstAlarmTime,
                numberOfAlarms = numberOfAlarms,
                minutesBetweenAlarms = minutesBetweenAlarms
        )
        Assert.assertEquals(expectedAlarmsList, actualAlarmsList)
    }

    @Test
    fun getAlarmsList_oneAlarm() {
        val firstAlarmTime = LocalTime.NOON
        val numberOfAlarms = 1
        val minutesBetweenAlarms = 10

        val expectedAlarmsList = listOf(
                LocalTime.NOON
        )

        val actualAlarmsList = AlarmsConverter.getAlarmsList(
                firstAlarmTime = firstAlarmTime,
                numberOfAlarms = numberOfAlarms,
                minutesBetweenAlarms = minutesBetweenAlarms
        )
        Assert.assertEquals(expectedAlarmsList, actualAlarmsList)
    }

    @Test
    fun getAlarmsList_separatedByMidnight() {
        val firstAlarmTime = LocalTime.of(23, 40)
        val numberOfAlarms = 3
        val minutesBetweenAlarms = 30

        val expectedAlarmsList = listOf(
                LocalTime.of(23, 40),
                LocalTime.of(0, 10),
                LocalTime.of(0, 40)
        )

        val actualAlarmsList = AlarmsConverter.getAlarmsList(
                firstAlarmTime = firstAlarmTime,
                numberOfAlarms = numberOfAlarms,
                minutesBetweenAlarms = minutesBetweenAlarms
        )
        Assert.assertEquals(expectedAlarmsList, actualAlarmsList)

    }


}