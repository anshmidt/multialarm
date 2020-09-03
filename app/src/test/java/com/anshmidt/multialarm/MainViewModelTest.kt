package com.anshmidt.multialarm

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.anshmidt.multialarm.repository.AlarmSettingsRepository
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

    private lateinit var viewModel: MainViewModel

    // Allows to set livedata
    @get:Rule
    var instantExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        viewModel = MainViewModel(alarmSettingsRepository)
    }

    @Test
    fun displayedFormat_ofFirstAlarmTime() {
        val firstAlarmTime = LocalTime.of(1, 9)
        `when`(alarmSettingsRepository.firstAlarmTime).thenReturn(firstAlarmTime)

        val expectedDisplayableTime = "01:09"

        viewModel.firstAlarmTime.value = firstAlarmTime
        val actualDisplayableTime = viewModel.getFirstAlarmTimeDisplayable()
        Assert.assertEquals(expectedDisplayableTime, actualDisplayableTime)
    }

}