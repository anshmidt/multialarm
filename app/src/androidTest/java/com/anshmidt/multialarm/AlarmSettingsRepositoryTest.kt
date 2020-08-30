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

    }

    @Test
    fun initialValue_switchState() {
        repository.clearAll()
        Assert.assertEquals(false, repository.alarmSwitchState)
    }

    @Test
    fun initialValue_firstAlarmTime() {
        repository.clearAll()
        val expectedFirstAlarmTime = LocalTime.of(6, 0)
        Assert.assertEquals(expectedFirstAlarmTime, repository.firstAlarmTime)
    }

    @Test
    fun saveSwitchState_ifPreferencesEmpty() {
        repository.clearAll()

        val newSwitchState = true
        repository.alarmSwitchState = newSwitchState
        val actualSwitchStateFromRepository = repository.alarmSwitchState
        Assert.assertEquals(newSwitchState, actualSwitchStateFromRepository)
    }

    @Test
    fun saveSwitchState_ifPreferencesNotEmpty() {
        repository.clearAll()
        repository.alarmSwitchState = true

        val newSwitchState = false
        repository.alarmSwitchState = newSwitchState
        val actualSwitchStateFromRepository = repository.alarmSwitchState
        Assert.assertEquals(newSwitchState, actualSwitchStateFromRepository)
    }

    @Test
    fun saveFirstAlarmTime() {
        repository.clearAll()
        repository.firstAlarmTime = LocalTime.NOON

        val newFirstAlarmTime = LocalTime.of(7, 23)
        repository.firstAlarmTime = newFirstAlarmTime
        val actualValueFromRepository = repository.firstAlarmTime
        Assert.assertEquals(newFirstAlarmTime, actualValueFromRepository)
    }
}