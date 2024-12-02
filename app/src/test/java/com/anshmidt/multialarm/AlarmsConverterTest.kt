package com.anshmidt.multialarm

import org.junit.Test

class AlarmsConverterTest {

    @Test
    fun getAlarmsList_happyPath() {
//        val alarmSettings = AlarmSettings(
//            areOn = true,
//            firstAlarmTimeMillis =
//        )
    }

//    @Test
//    fun getAlarmsList_happyPath() {
//        val firstAlarmTime = LocalTime.NOON
//        val numberOfAlarms = 2
//        val minutesBetweenAlarms = 10
//
//        val expectedAlarmsList = listOf(
//                AlarmListEntry(LocalTime.NOON, true),
//                AlarmListEntry(LocalTime.of(12, 10), true)
//        )
//
//        val actualAlarmsList = AlarmsConverter.getAlarmsList(
//                firstAlarmTime = firstAlarmTime,
//                numberOfAlarms = numberOfAlarms,
//                minutesBetweenAlarms = minutesBetweenAlarms
//        )
//        Assert.assertEquals(expectedAlarmsList, actualAlarmsList)
//    }
//
//    @Test
//    fun getAlarmsList_oneAlarm() {
//        val firstAlarmTime = LocalTime.NOON
//        val numberOfAlarms = 1
//        val minutesBetweenAlarms = 10
//
//        val expectedAlarmsList = listOf(
//                AlarmListEntry(LocalTime.NOON, true)
//        )
//
//        val actualAlarmsList = AlarmsConverter.getAlarmsList(
//                firstAlarmTime = firstAlarmTime,
//                numberOfAlarms = numberOfAlarms,
//                minutesBetweenAlarms = minutesBetweenAlarms
//        )
//        Assert.assertEquals(expectedAlarmsList, actualAlarmsList)
//    }
//
//    @Test
//    fun getAlarmsList_separatedByMidnight() {
//        val firstAlarmTime = LocalTime.of(23, 40)
//        val numberOfAlarms = 3
//        val minutesBetweenAlarms = 30
//
//        val expectedAlarmsList = listOf(
//                AlarmListEntry(LocalTime.of(23, 40), true),
//                AlarmListEntry(LocalTime.of(0, 10), true),
//                AlarmListEntry(LocalTime.of(0, 40), true)
//        )
//
//        val actualAlarmsList = AlarmsConverter.getAlarmsList(
//                firstAlarmTime = firstAlarmTime,
//                numberOfAlarms = numberOfAlarms,
//                minutesBetweenAlarms = minutesBetweenAlarms
//        )
//        Assert.assertEquals(expectedAlarmsList, actualAlarmsList)
//
//    }
//
//    @Test
//    fun isAlarmEnabled_notRang() {
//        val numberOfAlreadyRangAlarms = 0
//        Assert.assertTrue(AlarmsConverter.isAlarmEnabled(
//                alarmPosition = 0,
//                numberOfAlreadyRangAlarms = numberOfAlreadyRangAlarms
//        ))
//        Assert.assertTrue(AlarmsConverter.isAlarmEnabled(
//                alarmPosition = 1,
//                numberOfAlreadyRangAlarms = numberOfAlreadyRangAlarms
//        ))
//        Assert.assertTrue(AlarmsConverter.isAlarmEnabled(
//                alarmPosition = 2,
//                numberOfAlreadyRangAlarms = numberOfAlreadyRangAlarms
//        ))
//    }
//
//    @Test
//    fun isAlarmEnabled_allAlarmsRang() {
//        val numberOfAlreadyRangAlarms = 3
//        Assert.assertFalse(AlarmsConverter.isAlarmEnabled(
//                alarmPosition = 0,
//                numberOfAlreadyRangAlarms = numberOfAlreadyRangAlarms
//        ))
//        Assert.assertFalse(AlarmsConverter.isAlarmEnabled(
//                alarmPosition = 1,
//                numberOfAlreadyRangAlarms = numberOfAlreadyRangAlarms
//        ))
//        Assert.assertFalse(AlarmsConverter.isAlarmEnabled(
//                alarmPosition = 2,
//                numberOfAlreadyRangAlarms = numberOfAlreadyRangAlarms
//        ))
//    }
//
//    @Test
//    fun isAlarmEnabled_firstAlarmRang() {
//        val numberOfAlreadyRangAlarms = 1
//        Assert.assertFalse(AlarmsConverter.isAlarmEnabled(
//                alarmPosition = 0,
//                numberOfAlreadyRangAlarms = numberOfAlreadyRangAlarms
//        ))
//        Assert.assertTrue(AlarmsConverter.isAlarmEnabled(
//                alarmPosition = 1,
//                numberOfAlreadyRangAlarms = numberOfAlreadyRangAlarms
//        ))
//        Assert.assertTrue(AlarmsConverter.isAlarmEnabled(
//                alarmPosition = 2,
//                numberOfAlreadyRangAlarms = numberOfAlreadyRangAlarms
//        ))
//    }



}