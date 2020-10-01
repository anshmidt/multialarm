package com.anshmidt.multialarm

import android.app.AlarmManager
import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.anshmidt.multialarm.alarmscheduler.AlarmScheduler
import com.anshmidt.multialarm.data.AlarmSettings
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf
import org.robolectric.shadows.ShadowAlarmManager
import org.threeten.bp.LocalTime
import java.lang.Math.abs
import java.util.concurrent.TimeUnit


@RunWith(RobolectricTestRunner::class)
class AlarmSchedulingTest {


    lateinit var alarmScheduler: AlarmScheduler

    lateinit var alarmManager: AlarmManager
    lateinit var shadowAlarmManager: ShadowAlarmManager

    private fun getStandardAlarmSettings(): AlarmSettings {
        val firstAlarmTime = LocalTime.now().plusHours(1)
        return AlarmSettings(
                turnedOn = true,
                firstAlarmTime = firstAlarmTime,
                minutesBetweenAlarms = 5,
                numberOfAlarms = 5,
                songDurationSeconds = 60
        )
    }

    @Before
    fun executeBefore() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        shadowAlarmManager = shadowOf(alarmManager)
        alarmScheduler = AlarmScheduler(context)
    }

    @After
    fun tearDown() {
        alarmScheduler.cancel()
    }


    @Test
    fun alarmCanBeScheduled() {
        //given
        Assert.assertNull(shadowAlarmManager.peekNextScheduledAlarm())

        //when
        alarmScheduler.schedule(getStandardAlarmSettings())

        //then
        Assert.assertNotNull(shadowAlarmManager.peekNextScheduledAlarm())
    }

    @Test
    fun alarmCanBeCanceled() {
        //given
        alarmScheduler.schedule(getStandardAlarmSettings())
        Assert.assertNotNull(shadowAlarmManager.peekNextScheduledAlarm())

        //when
        alarmScheduler.cancel()

        //then
        Assert.assertNull(shadowAlarmManager.peekNextScheduledAlarm())
    }

    @Test
    fun alarmScheduledWithCorrectFirstAlarmTime() {
        //when
        alarmScheduler.schedule(getStandardAlarmSettings())

        //then
        val actualFirstAlarmTimeMillis = shadowAlarmManager.peekNextScheduledAlarm().triggerAtTime
        val expectedFirstAlarmTimeMillis = System.currentTimeMillis() + TimeUnit.HOURS.toMillis(1) //value from getStandardAlarmSettings()
        val MAX_RESULTS_DIFFERENCE_MILLIS = 2000
        Assert.assertTrue(abs(expectedFirstAlarmTimeMillis - actualFirstAlarmTimeMillis) < MAX_RESULTS_DIFFERENCE_MILLIS)
    }

    @Test
    fun alarmsScheduledWithCorrectInterval() {
        //when
        alarmScheduler.schedule(getStandardAlarmSettings())

        //then
        val actualIntervalMillis = shadowAlarmManager.peekNextScheduledAlarm().interval
        val expectedIntervalMillis = TimeUnit.MINUTES.toMillis(getStandardAlarmSettings().minutesBetweenAlarms.toLong())
        Assert.assertEquals(expectedIntervalMillis, actualIntervalMillis)
    }

    @Test
    fun scheduleTwiceWithSameTime() {
        //when
        alarmScheduler.schedule(getStandardAlarmSettings())
        alarmScheduler.schedule(getStandardAlarmSettings())

        //then
        val actualFirstAlarmTimeMillis = shadowAlarmManager.peekNextScheduledAlarm().triggerAtTime
        val expectedFirstAlarmTimeMillis = System.currentTimeMillis() + TimeUnit.HOURS.toMillis(1) //value from getStandardAlarmSettings()
        val MAX_RESULTS_DIFFERENCE_MILLIS = 2000
        Assert.assertTrue(abs(expectedFirstAlarmTimeMillis - actualFirstAlarmTimeMillis) < MAX_RESULTS_DIFFERENCE_MILLIS)
    }

    @Test
    fun scheduleTwiceWithDifferentTime() {
        //given
        val firstAlarmSettings = getStandardAlarmSettings()
        val secondAlarmSettings = AlarmSettings(
                turnedOn = true,
                firstAlarmTime = LocalTime.now().plusHours(2),
                minutesBetweenAlarms = 5,
                numberOfAlarms = 5,
                songDurationSeconds = 60
        )

        //when
        alarmScheduler.schedule(firstAlarmSettings)
        alarmScheduler.schedule(secondAlarmSettings)

        //then
        val actualFirstAlarmTimeMillis = shadowAlarmManager.peekNextScheduledAlarm().triggerAtTime
        val expectedFirstAlarmTimeMillis = System.currentTimeMillis() + TimeUnit.HOURS.toMillis(2)
        val MAX_RESULTS_DIFFERENCE_MILLIS = 2000
        Assert.assertTrue(abs(expectedFirstAlarmTimeMillis - actualFirstAlarmTimeMillis) < MAX_RESULTS_DIFFERENCE_MILLIS)
    }

    @Test
    fun scheduleAfterCanceling() {
        //given
        val firstAlarmSettings = getStandardAlarmSettings()
        val secondAlarmSettings = AlarmSettings(
                turnedOn = true,
                firstAlarmTime = LocalTime.now().plusHours(2),
                minutesBetweenAlarms = 5,
                numberOfAlarms = 5,
                songDurationSeconds = 60
        )

        //when
        alarmScheduler.schedule(secondAlarmSettings)
        alarmScheduler.cancel()
        alarmScheduler.schedule(firstAlarmSettings)

        //then
        val actualFirstAlarmTimeMillis = shadowAlarmManager.peekNextScheduledAlarm().triggerAtTime
        val expectedFirstAlarmTimeMillis = System.currentTimeMillis() + TimeUnit.HOURS.toMillis(1) //value from getStandardAlarmSettings()
        val MAX_RESULTS_DIFFERENCE_MILLIS = 2000
        Assert.assertTrue(abs(expectedFirstAlarmTimeMillis - actualFirstAlarmTimeMillis) < MAX_RESULTS_DIFFERENCE_MILLIS)
    }

    @Test
    fun alarmNotScheduledIfSwitchOff() {
        val alarmSettings = AlarmSettings(
                turnedOn = false,
                firstAlarmTime = LocalTime.MIDNIGHT,
                minutesBetweenAlarms = 5,
                numberOfAlarms = 5,
                songDurationSeconds = 60
        )

        //when
        alarmScheduler.schedule(alarmSettings)

        //then
        val nextAlarm = shadowAlarmManager.peekNextScheduledAlarm()
        Assert.assertNull(nextAlarm)
    }


}