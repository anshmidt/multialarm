package com.anshmidt.multialarm

import com.anshmidt.multialarm.data.AlarmListEntry
import com.anshmidt.multialarm.data.AlarmSettings
import com.anshmidt.multialarm.data.TimeFormatter
import com.anshmidt.multialarm.data.getAlarmsList
import com.anshmidt.multialarm.data.getNextAlarmTimeMillis
import com.anshmidt.multialarm.data.isAlarmEnabled
import com.anshmidt.multialarm.data.isThereNextAlarm
import org.junit.Assert
import org.junit.Test
import org.threeten.bp.LocalTime

class AlarmsConverterTest {

    @Test
    fun getAlarmsList_happyPath() {
        val firstAlarmTime = LocalTime.of(6, 0)
        val firstAlarmMillis = TimeFormatter.getAlarmTimeWithin24HoursMillis(firstAlarmTime)

        val alarmSettings = AlarmSettings(
            areOn = true,
            firstAlarmTimeMillis = firstAlarmMillis,
            minutesBetweenAlarms = 5,
            numberOfAlarms = 2,
            numberOfAlreadyRangAlarms = 1
        )

        val alarmsList = alarmSettings.getAlarmsList()

        Assert.assertEquals(AlarmListEntry(
            time = firstAlarmTime,
            isEnabled = false
        ), alarmsList[0])
        Assert.assertEquals(AlarmListEntry(
            time = firstAlarmTime.plusMinutes(5),
            isEnabled = true
        ), alarmsList[1])
    }

    @Test
    fun getAlarmsList_allOff() {
        val firstAlarmTime = LocalTime.of(6, 0)
        val firstAlarmMillis = TimeFormatter.getAlarmTimeWithin24HoursMillis(firstAlarmTime)

        val alarmSettings = AlarmSettings(
            areOn = false,
            firstAlarmTimeMillis = firstAlarmMillis,
            minutesBetweenAlarms = 5,
            numberOfAlarms = 3,
            numberOfAlreadyRangAlarms = 1
        )

        val alarmsList = alarmSettings.getAlarmsList()

        Assert.assertEquals(AlarmListEntry(
            time = firstAlarmTime,
            isEnabled = false
        ), alarmsList[0])

        Assert.assertEquals(AlarmListEntry(
            time = firstAlarmTime.plusMinutes(5),
            isEnabled = false
        ), alarmsList[1])

        Assert.assertEquals(AlarmListEntry(
            time = firstAlarmTime.plusMinutes(10),
            isEnabled = false
        ), alarmsList[2])
    }

    @Test
    fun getNextAlarmTimeMillis_happyPath() {
        val firstAlarmTime = LocalTime.of(6, 0)
        val firstAlarmMillis = TimeFormatter.getAlarmTimeWithin24HoursMillis(firstAlarmTime)

        val alarmSettings = AlarmSettings(
            areOn = true,
            firstAlarmTimeMillis = firstAlarmMillis,
            minutesBetweenAlarms = 5,
            numberOfAlarms = 2,
            numberOfAlreadyRangAlarms = 1
        )

        val actualNextAlarmMillis = alarmSettings.getNextAlarmTimeMillis()
        val expectedNextAlarmMillis = firstAlarmMillis + 5 * 60 * 1000

        Assert.assertEquals(expectedNextAlarmMillis, actualNextAlarmMillis)
    }

    @Test
    fun getNextAlarmTimeMillis_firstAlarm() {
        val firstAlarmTime = LocalTime.of(6, 0)
        val firstAlarmMillis = TimeFormatter.getAlarmTimeWithin24HoursMillis(firstAlarmTime)

        val alarmSettings = AlarmSettings(
            areOn = true,
            firstAlarmTimeMillis = firstAlarmMillis,
            minutesBetweenAlarms = 5,
            numberOfAlarms = 3,
            numberOfAlreadyRangAlarms = 0
        )

        val actualNextAlarmMillis = alarmSettings.getNextAlarmTimeMillis()
        val expectedNextAlarmMillis = firstAlarmMillis

        Assert.assertEquals(expectedNextAlarmMillis, actualNextAlarmMillis)
    }

    @Test
    fun getNextAlarmTimeMillis_lastAlarm() {
        val firstAlarmTime = LocalTime.of(6, 0)
        val firstAlarmMillis = TimeFormatter.getAlarmTimeWithin24HoursMillis(firstAlarmTime)

        val alarmSettings = AlarmSettings(
            areOn = true,
            firstAlarmTimeMillis = firstAlarmMillis,
            minutesBetweenAlarms = 5,
            numberOfAlarms = 3,
            numberOfAlreadyRangAlarms = 2
        )

        val actualNextAlarmMillis = alarmSettings.getNextAlarmTimeMillis()
        val expectedNextAlarmMillis = firstAlarmMillis + 2 * 5 * 60 * 1000

        Assert.assertEquals(expectedNextAlarmMillis, actualNextAlarmMillis)
    }

    @Test
    fun getNextAlarmTimeMillis_allAlarmsAlreadyRang() {
        val firstAlarmTime = LocalTime.of(6, 0)
        val firstAlarmMillis = TimeFormatter.getAlarmTimeWithin24HoursMillis(firstAlarmTime)

        val alarmSettings = AlarmSettings(
            areOn = true,
            firstAlarmTimeMillis = firstAlarmMillis,
            minutesBetweenAlarms = 5,
            numberOfAlarms = 3,
            numberOfAlreadyRangAlarms = 3
        )

        val actualNextAlarmMillis = alarmSettings.getNextAlarmTimeMillis()
        Assert.assertNull(actualNextAlarmMillis)
    }

    @Test
    fun getNextAlarmTimeMillis_alarmsOff() {
        val firstAlarmTime = LocalTime.of(6, 0)
        val firstAlarmMillis = TimeFormatter.getAlarmTimeWithin24HoursMillis(firstAlarmTime)

        val alarmSettings = AlarmSettings(
            areOn = false,
            firstAlarmTimeMillis = firstAlarmMillis,
            minutesBetweenAlarms = 5,
            numberOfAlarms = 2,
            numberOfAlreadyRangAlarms = 1
        )

        val actualNextAlarmMillis = alarmSettings.getNextAlarmTimeMillis()
        Assert.assertNull(actualNextAlarmMillis)
    }

    @Test
    fun isThereNextAlarm_happyPath() {
        val alarmSettings = AlarmSettings(
            areOn = true,
            firstAlarmTimeMillis = 0,
            minutesBetweenAlarms = 5,
            numberOfAlarms = 3,
            numberOfAlreadyRangAlarms = 1
        )

        Assert.assertTrue(alarmSettings.isThereNextAlarm())
    }

    @Test
    fun isThereNextAlarm_noAlarmsRangYet() {
        val alarmSettings = AlarmSettings(
            areOn = true,
            firstAlarmTimeMillis = 0,
            minutesBetweenAlarms = 5,
            numberOfAlarms = 3,
            numberOfAlreadyRangAlarms = 0
        )

        Assert.assertTrue(alarmSettings.isThereNextAlarm())
    }

    @Test
    fun isThereNextAlarm_allAlarmsAlreadyRang() {
        val alarmSettings = AlarmSettings(
            areOn = true,
            firstAlarmTimeMillis = 0,
            minutesBetweenAlarms = 5,
            numberOfAlarms = 3,
            numberOfAlreadyRangAlarms = 3
        )

        Assert.assertFalse(alarmSettings.isThereNextAlarm())
    }

    @Test
    fun isThereNextAlarm_alarmsOff() {
        val alarmSettings = AlarmSettings(
            areOn = false,
            firstAlarmTimeMillis = 0,
            minutesBetweenAlarms = 5,
            numberOfAlarms = 3,
            numberOfAlreadyRangAlarms = 1
        )

        Assert.assertFalse(alarmSettings.isThereNextAlarm())
    }

    @Test
    fun isAlarmEnabled_happyPath() {
        val alarmSettings = AlarmSettings(
            areOn = true,
            firstAlarmTimeMillis = 0,
            minutesBetweenAlarms = 5,
            numberOfAlarms = 3,
            numberOfAlreadyRangAlarms = 1
        )

        Assert.assertFalse(alarmSettings.isAlarmEnabled(0))
        Assert.assertTrue(alarmSettings.isAlarmEnabled(1))
        Assert.assertTrue(alarmSettings.isAlarmEnabled(2))
    }



}