package com.anshmidt.multialarm

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.rule.ActivityTestRule
import com.anshmidt.multialarm.di.appModule
import com.anshmidt.multialarm.view.MainActivity
import com.anshmidt.multialarm.viewmodel.MainViewModel
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module
import org.koin.standalone.StandAloneContext.loadKoinModules
import org.koin.standalone.StandAloneContext.startKoin
import org.koin.standalone.StandAloneContext.stopKoin
import org.koin.standalone.inject
import org.koin.test.KoinTest
import org.mockito.ArgumentMatchers
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */

@RunWith(AndroidJUnit4ClassRunner::class)
class MainActivityTest : KoinTest {


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

}