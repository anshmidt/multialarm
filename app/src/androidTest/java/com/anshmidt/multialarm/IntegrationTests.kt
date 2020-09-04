package com.anshmidt.multialarm

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.rule.ActivityTestRule
import com.anshmidt.multialarm.repository.AlarmSettingsRepository
import com.anshmidt.multialarm.view.MainActivity
import com.anshmidt.multialarm.viewmodel.MainViewModel
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.test.KoinTest
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations
import org.mockito.invocation.InvocationOnMock
import org.mockito.stubbing.Answer
import org.threeten.bp.LocalTime

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */

@RunWith(AndroidJUnit4ClassRunner::class)
class IntegrationTests : KoinTest {

//    @Mock
    private lateinit var alarmSettingsRepository: AlarmSettingsRepository

    private lateinit var viewModel: MainViewModel
    val INITIAL_FIRST_ALARM_TIME = LocalTime.of(1,9)

    @Before
    fun setUp() {
//        MockitoAnnotations.initMocks(this)
        alarmSettingsRepository = mock(AlarmSettingsRepository::class.java)
        Mockito.`when`(alarmSettingsRepository.firstAlarmTime).thenAnswer(object : Answer<LocalTime> {
            override fun answer(invocation: InvocationOnMock?): LocalTime {
                return INITIAL_FIRST_ALARM_TIME
            }
        })
//        alarmSettingsRepository.firstAlarmTime = INITIAL_FIRST_ALARM_TIME
        viewModel = MainViewModel(alarmSettingsRepository)
    }

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

    @Test
    fun passFirstAlarmTime_toDialog() {
        val timeOnMainScreen: String = ViewHelper.getTextFromTextView(onView(withId(R.id.textview_main_firstalarm_time)))
        onView(withId(R.id.layout_first_alarm)).perform(ViewActions.click())
        val timeOnDialog = ViewHelper.getTimeFromTimePicker(onView(withId(R.id.timepicker_firstalarmdialog)))
    }

}