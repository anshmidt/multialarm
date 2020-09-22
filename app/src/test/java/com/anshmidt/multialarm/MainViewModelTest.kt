package com.anshmidt.multialarm

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.anshmidt.multialarm.repository.AlarmSettingsRepository
import com.anshmidt.multialarm.viewmodel.DismissAlarmViewModel
import com.anshmidt.multialarm.viewmodel.FirstAlarmTimeViewModel
import com.anshmidt.multialarm.viewmodel.MainViewModel
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.threeten.bp.LocalTime


class MainViewModelTest {

    @Mock
    private lateinit var alarmSettingsRepository: AlarmSettingsRepository

    private lateinit var firstAlarmTimeViewModel: FirstAlarmTimeViewModel


    // Allows to set livedata
    @get:Rule
    var instantExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        firstAlarmTimeViewModel = FirstAlarmTimeViewModel(alarmSettingsRepository)
    }

    @Test
    fun displayedFormat_ofFirstAlarmTime() {
        // given
        val firstAlarmTime = LocalTime.of(1, 9)
        `when`(alarmSettingsRepository.firstAlarmTime).thenReturn(firstAlarmTime)
        val expectedDisplayableTime = "01:09"
        firstAlarmTimeViewModel.firstAlarmTimeDisplayable.observeForever { } //needed to be called in order to read livedata value

        // when
        firstAlarmTimeViewModel.onViewCreated()

        val actualDisplayableTime = firstAlarmTimeViewModel.firstAlarmTimeDisplayable.value

        // then
        Assert.assertEquals(expectedDisplayableTime, actualDisplayableTime)
    }

    @Test
    fun firstAlarmTimeLeftCalculation() {
//        // given
//        val firstAlarmTime = LocalTime.of(1, 9)
//        `when`(alarmSettingsRepository.firstAlarmTime).thenReturn(firstAlarmTime)
//
//        // when
//        firstAlarmTimeViewModel.onViewCreated()
//        val actualTimeLeftMillis = firstAlarmTimeViewModel.timeLeftBeforeFirstAlarm.value!!.toMillis()
//
//        // then
//        //TODO
    }

}