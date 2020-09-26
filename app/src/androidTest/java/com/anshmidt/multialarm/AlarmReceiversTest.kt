package com.anshmidt.multialarm

import android.app.PendingIntent
import android.content.Intent
import android.util.Log
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.rule.ActivityTestRule
import androidx.test.uiautomator.UiDevice
import com.anshmidt.multialarm.data.AlarmSettings
import com.anshmidt.multialarm.view.MainActivity
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.test.KoinTest
import org.threeten.bp.LocalTime


@RunWith(AndroidJUnit4ClassRunner::class)
class AlarmReceiversTest : KoinTest {

    @get:Rule
    var mainActivityRule: ActivityTestRule<MainActivity>
            = ActivityTestRule(MainActivity::class.java)

    lateinit var alarmScheduler: AlarmScheduler

    val uiAutomation = InstrumentationRegistry.getInstrumentation().getUiAutomation()
    val uiDevice = UiDevice.getInstance(getInstrumentation())


    private fun getStandardAlarmSettings(): AlarmSettings {
        val firstAlarmTime = LocalTime.now().plusHours(1)
        return AlarmSettings(
                alarmSwitchState = true,
                firstAlarmTime = firstAlarmTime,
                minutesBetweenAlarms = 5,
                numberOfAlarms = 5,
                songDurationSeconds = 60
        )
    }

    private fun getScheduledAlarms(): String {
        val output = uiDevice.executeShellCommand("dumpsys alarm | grep ${BuildConfig.APPLICATION_ID}")
        return output
    }



    @Before
    fun setUp() {
        alarmScheduler = AlarmScheduler(mainActivityRule.activity)
    }

    @After
    fun tearDown() {
        alarmScheduler.cancel()
    }

    @Test
    fun temp() {
        alarmScheduler.schedule(getStandardAlarmSettings())

        Thread.sleep(1000000)
        val output1 = uiDevice.executeShellCommand("dumpsys alarm")
        Log.d("tdd", output1)
    }

}