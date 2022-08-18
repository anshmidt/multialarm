package com.anshmidt.multialarm

import android.view.View
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.uiautomator.UiDevice
import com.anshmidt.multialarm.view.activities.DismissAlarmActivity
import org.hamcrest.Matcher
import org.junit.After
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.test.KoinTest


@RunWith(AndroidJUnit4ClassRunner::class)
class DismissAlarmActivityTest : KoinTest {

    val uiDevice = UiDevice.getInstance(getInstrumentation())
    val deviceStateController = DeviceStateController()

    lateinit var scenario: ActivityScenario<DismissAlarmActivity>

    @After
    fun tearDown() {
        uiDevice.wakeUp()
        deviceStateController.returnFromDozeMode()
        scenario.close()
    }

    @Test
    fun screenAppears() {
        scenario = launchActivity<DismissAlarmActivity>()
        onView(withId(R.id.button_dismiss_alarm)).check(matches(isDisplayed()))
    }

    @Test
    fun screenAppearsIfScreenLocked() {
        scenario = launchActivity<DismissAlarmActivity>()

        //when
        uiDevice.sleep()

        //then
        onView(withId(R.id.button_dismiss_alarm)).check(matches(isDisplayed()))
    }

    @Test
    fun screenAppearsInDozeMode() {
        scenario = launchActivity<DismissAlarmActivity>()

        //when
        deviceStateController.goToDozeMode()

        onView(withId(R.id.button_dismiss_alarm)).check(matches(isDisplayed()))
    }

    @Test
    fun openScreenAndWait() {
        scenario = launchActivity<DismissAlarmActivity>()

        onView(isRoot()).perform(waitFor(5000))
    }

    private fun waitFor(delay: Long): ViewAction? {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View> = isRoot()
            override fun getDescription(): String = "wait for $delay milliseconds"
            override fun perform(uiController: UiController, v: View?) {
                uiController.loopMainThreadForAtLeast(delay)
            }
        }
    }

}