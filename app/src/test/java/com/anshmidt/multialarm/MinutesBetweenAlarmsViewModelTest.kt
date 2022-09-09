package com.anshmidt.multialarm

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.anshmidt.multialarm.alarmscheduler.AlarmScheduler
import com.anshmidt.multialarm.repository.ScheduleSettingsRepository
import com.anshmidt.multialarm.viewmodel.MinutesBetweenAlarmsViewModel
import com.nhaarman.mockitokotlin2.anyOrNull
import com.nhaarman.mockitokotlin2.verify
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class MinutesBetweenAlarmsViewModelTest {

    @Mock
    private lateinit var settingsRepository: ScheduleSettingsRepository

    @Mock
    private lateinit var alarmScheduler: AlarmScheduler

    private lateinit var minutesBetweenAlarmsViewModel: MinutesBetweenAlarmsViewModel

    // Allows to set livedata
    @get:Rule
    var instantExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        minutesBetweenAlarmsViewModel = MinutesBetweenAlarmsViewModel(settingsRepository, alarmScheduler)
    }

    @Test
    fun alarmRescheduledWhenValueChanges() {
        minutesBetweenAlarmsViewModel.selectedVariantIndex.value = 5
        minutesBetweenAlarmsViewModel.onOkButtonClickInMinutesBetweenAlarmsDialog()
        verify(alarmScheduler).reschedule(anyOrNull())
    }

}