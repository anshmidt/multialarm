package com.anshmidt.multialarm

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.anshmidt.multialarm.alarmscheduler.AlarmScheduler
import com.anshmidt.multialarm.repository.SettingsRepository
import com.anshmidt.multialarm.viewmodel.FirstAlarmTimeViewModel
import com.anshmidt.multialarm.viewmodel.MainViewModel
import com.nhaarman.mockitokotlin2.anyOrNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

class MainViewModelTest {

    @Mock
    private lateinit var settingsRepository: SettingsRepository

    @Mock
    private lateinit var alarmScheduler: AlarmScheduler

    private lateinit var mainViewModel: MainViewModel

    // Allows to set livedata
    @get:Rule
    var instantExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        mainViewModel = MainViewModel(settingsRepository, alarmScheduler)
    }

    @Test
    fun alarmScheduledIfSwitchTurnedOn() {
        mainViewModel.alarmTurnedOn = true

        verify(alarmScheduler, times(1)).reschedule(anyOrNull())
    }
}