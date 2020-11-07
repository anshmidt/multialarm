package com.anshmidt.multialarm

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.anshmidt.multialarm.alarmscheduler.AlarmScheduler
import com.anshmidt.multialarm.repository.SettingsRepository
import com.anshmidt.multialarm.viewmodel.MinutesBetweenAlarmsViewModel
import com.anshmidt.multialarm.viewmodel.NumberOfAlarmsViewModel
import com.nhaarman.mockitokotlin2.anyOrNull
import com.nhaarman.mockitokotlin2.verify
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class NumberOfAlarmsViewModelTest {

    @Mock
    private lateinit var settingsRepository: SettingsRepository

    @Mock
    private lateinit var alarmScheduler: AlarmScheduler

    private lateinit var numberOfAlarmsViewModel: NumberOfAlarmsViewModel

    // Allows to set livedata
    @get:Rule
    var instantExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        numberOfAlarmsViewModel = NumberOfAlarmsViewModel(settingsRepository, alarmScheduler)
    }

    @Test
    fun alarmRescheduledWhenValueChanges() {
        numberOfAlarmsViewModel.selectedVariantIndex.value = 3
        numberOfAlarmsViewModel.onOkButtonClickInNumberOfAlarmsDialog()

        verify(alarmScheduler).reschedule(anyOrNull())
    }
}