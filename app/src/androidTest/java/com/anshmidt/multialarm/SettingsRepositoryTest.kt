package com.anshmidt.multialarm

import androidx.test.platform.app.InstrumentationRegistry
import com.anshmidt.multialarm.datasources.SharedPreferencesStorage
import com.anshmidt.multialarm.repository.ScheduleSettingsRepository
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.threeten.bp.LocalTime

class SettingsRepositoryTest {

    val appContext = InstrumentationRegistry.getInstrumentation().targetContext
    val sharedPreferencesStorage = SharedPreferencesStorage(appContext)
    val repository = ScheduleSettingsRepository(sharedPreferencesStorage)

    @Before
    fun setUp() {
        repository.clearAll()
    }

    @Test
    fun initialValue_switchState() {
        Assert.assertEquals(false, repository.alarmTurnedOn)
    }

    @Test
    fun initialValue_firstAlarmTime() {
        val expectedFirstAlarmTime = LocalTime.of(6, 0)
        Assert.assertEquals(expectedFirstAlarmTime, repository.firstAlarmTime)
    }

    @Test
    fun initialValue_intervalBetweenAlarms() {
        val expectedInterval = 10
        Assert.assertEquals(expectedInterval, repository.minutesBetweenAlarms)
    }

    @Test
    fun initialValue_numberOfAlarms() {
        val expectedValue = 5
        Assert.assertEquals(expectedValue, repository.numberOfAlarms)
    }

    @Test
    fun saveSwitchState_ifPreferencesEmpty() {
        val newSwitchState = true
        repository.alarmTurnedOn = newSwitchState
        val actualSwitchStateFromRepository = repository.alarmTurnedOn
        Assert.assertEquals(newSwitchState, actualSwitchStateFromRepository)
    }

    @Test
    fun saveSwitchState_ifPreferencesNotEmpty() {
        repository.alarmTurnedOn = true

        val newSwitchState = false
        repository.alarmTurnedOn = newSwitchState
        val actualSwitchStateFromRepository = repository.alarmTurnedOn
        Assert.assertEquals(newSwitchState, actualSwitchStateFromRepository)
    }

    @Test
    fun saveFirstAlarmTime() {
        repository.firstAlarmTime = LocalTime.NOON

        val newFirstAlarmTime = LocalTime.of(7, 23)
        repository.firstAlarmTime = newFirstAlarmTime
        val actualValueFromRepository = repository.firstAlarmTime
        Assert.assertEquals(newFirstAlarmTime, actualValueFromRepository)
    }

    @Test
    fun saveMinutesBetweenAlarms() {
        repository.minutesBetweenAlarms = 66

        val newValue = 3
        repository.minutesBetweenAlarms = newValue
        val actualValueFromRepository = repository.minutesBetweenAlarms
        Assert.assertEquals(newValue, actualValueFromRepository)
    }

    @Test
    fun saveNumberOfAlarms() {
        val newValue = 555
        repository.numberOfAlarms = newValue
        val actualValueFromRepository = repository.numberOfAlarms
        Assert.assertEquals(newValue, actualValueFromRepository)
    }
}