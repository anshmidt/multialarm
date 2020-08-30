package com.anshmidt.multialarm

import androidx.test.platform.app.InstrumentationRegistry
import com.anshmidt.multialarm.data.AlarmSettings
import com.anshmidt.multialarm.repository.AlarmSettingsRepository
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.threeten.bp.LocalTime

class AlarmSettingsRepositoryTest {

    val appContext = InstrumentationRegistry.getInstrumentation().targetContext
    val repository = AlarmSettingsRepository(appContext)

    @Before
    fun executeBefore() {
        repository.clearAll()
    }

    @Test
    fun initialValue_switchState() {
        Assert.assertEquals(false, repository.alarmSwitchState)
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
        repository.alarmSwitchState = newSwitchState
        val actualSwitchStateFromRepository = repository.alarmSwitchState
        Assert.assertEquals(newSwitchState, actualSwitchStateFromRepository)
    }

    @Test
    fun saveSwitchState_ifPreferencesNotEmpty() {
        repository.alarmSwitchState = true

        val newSwitchState = false
        repository.alarmSwitchState = newSwitchState
        val actualSwitchStateFromRepository = repository.alarmSwitchState
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