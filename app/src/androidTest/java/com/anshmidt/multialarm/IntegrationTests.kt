package com.anshmidt.multialarm

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.rule.ActivityTestRule
import com.anshmidt.multialarm.view.MainActivity
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.test.KoinTest
import org.threeten.bp.LocalTime
import org.threeten.bp.format.DateTimeFormatter

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */

@RunWith(AndroidJUnit4ClassRunner::class)
class IntegrationTests : KoinTest {


    @get:Rule
    var mainActivityRule: ActivityTestRule<MainActivity>
            = ActivityTestRule(MainActivity::class.java)

    private fun turnAlarmSwitchOn() {
        onView(withId(R.id.switch_main)).perform(ViewActions.swipeRight())
        onView(withId(R.id.switch_main)).check(matches(isChecked()))
    }

    private fun turnAlarmSwitchOff() {
        onView(withId(R.id.switch_main)).perform(ViewActions.swipeLeft())
        onView(withId(R.id.switch_main)).check(matches(isNotChecked()))
    }

    private fun restartApp() {
        with(mainActivityRule) {
            finishActivity()
            launchActivity(null)
        }
    }

    private fun localTimeToString(localTime: LocalTime): String {
        val format = DateTimeFormatter.ofPattern("HH:mm")
        return localTime.format(format)
    }

    private fun getFirstAlarmTimeFromMainScreen(): String {
        val timeOnMainScreenFullString: String = ViewHelper.getTextFromTextView(onView(withId(R.id.textview_main_firstalarm_time)))
        return timeOnMainScreenFullString.split(" ").last()
    }

    private fun getMinutesBetweenAlarmsFromMainScreen(): Int {
        val fullText = ViewHelper.getTextFromTextView(onView(withId(R.id.textview_main_interval)))
        return fullText.split(" ").first().toInt()
    }

    private fun clickPositiveButtonOnDialog() {
        onView(withId(android.R.id.button1)).perform(click())
    }

    @Test
    fun saveSwitchStateOff_whenAppRestarted() {
        turnAlarmSwitchOff()
        restartApp()
        onView(withId(R.id.switch_main)).check(matches(isNotChecked()))
    }

    @Test
    fun saveSwitchStateOn_whenAppRestarted() {
        turnAlarmSwitchOn()
        restartApp()
        onView(withId(R.id.switch_main)).check(matches(isChecked()))
    }

    @Test
    fun passFirstAlarmTime_toDialog() {
        val timeOnMainScreenString = getFirstAlarmTimeFromMainScreen()
        onView(withId(R.id.fragment_first_alarm_time)).perform(ViewActions.click())
        val timeOnDialog: LocalTime = ViewHelper.getTimeFromTimePicker(onView(withId(R.id.timepicker_firstalarmdialog)))
        Assert.assertEquals(timeOnMainScreenString, localTimeToString(timeOnDialog))
    }

    @Test
    fun passFirstAlarmTime_fromDialog() {
        onView(withId(R.id.fragment_first_alarm_time)).perform(ViewActions.click())
        ViewHelper.setTimeOnTimePicker(1, 1, onView(withId(R.id.timepicker_firstalarmdialog)))
        clickPositiveButtonOnDialog()
        val timeOnMainScreenString = getFirstAlarmTimeFromMainScreen()
        Assert.assertEquals("01:01", timeOnMainScreenString)
    }

    @Test
    fun setMinutesBetweenAlarms_firstValue() {
        onView(withId(R.id.fragment_interval)).perform(click())
        ViewHelper.setValueOnNumberPicker(0, onView(withId(R.id.numberpicker_intervaldialog)))
        clickPositiveButtonOnDialog()
        val actualValue = getMinutesBetweenAlarmsFromMainScreen()
        Assert.assertEquals(2, actualValue)
    }

    @Test
    fun setMinutesBetweenAlarms_lastValue() {
        onView(withId(R.id.fragment_interval)).perform(click())
        ViewHelper.setValueOnNumberPicker(14, onView(withId(R.id.numberpicker_intervaldialog)))
        clickPositiveButtonOnDialog()
        val actualValue = getMinutesBetweenAlarmsFromMainScreen()
        Assert.assertEquals(120, actualValue)
    }

}