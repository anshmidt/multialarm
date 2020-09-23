package com.anshmidt.multialarm

import android.os.RemoteException
import android.util.Log
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.rule.ActivityTestRule
import androidx.test.uiautomator.UiDevice
import com.anshmidt.multialarm.view.DismissAlarmActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.test.KoinTest


@RunWith(AndroidJUnit4ClassRunner::class)
class DismissAlarmActivityTest : KoinTest {

    val uiDevice = UiDevice.getInstance(getInstrumentation())
    val uiAutomation = getInstrumentation().getUiAutomation()

    private fun goToDozeMode() {
        uiAutomation.executeShellCommand("dumpsys battery unplug")
        uiAutomation.executeShellCommand("dumpsys deviceidle force-idle")
    }

    private fun returnFromDozeMode() {
        uiAutomation.executeShellCommand("dumpsys deviceidle unforce")
        uiAutomation.executeShellCommand("dumpsys battery reset")
    }




    @Test
    fun screenAppears() {
        val scenario = launchActivity<DismissAlarmActivity>()
        onView(withId(R.id.button_dismiss_alarm)).check(matches(isDisplayed()))
        scenario.close()
    }

    @Test
    fun screenAppearsIfScreenLocked() {
        //when
        uiDevice.sleep()
        val scenario = launchActivity<DismissAlarmActivity>()

        //then
        onView(withId(R.id.button_dismiss_alarm)).check(matches(isDisplayed()))

        //teardown
        uiDevice.wakeUp()
        scenario.close()
    }

    @Test
    fun screenAppearsInDozeMode() {
        //when
        goToDozeMode()

        val scenario = launchActivity<DismissAlarmActivity>()
        onView(withId(R.id.button_dismiss_alarm)).check(matches(isDisplayed()))

        //teardown
        returnFromDozeMode()
        scenario.close()

    }

}