package com.anshmidt.multialarm

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.anshmidt.multialarm.repository.SettingsRepository
import com.anshmidt.multialarm.viewmodel.FirstAlarmTimeViewModel
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.threeten.bp.LocalTime


class FirstAlarmTimeViewModelTest {

    @Mock
    private lateinit var settingsRepository: SettingsRepository

    private lateinit var firstAlarmTimeViewModel: FirstAlarmTimeViewModel


    // Allows to set livedata
    @get:Rule
    var instantExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        firstAlarmTimeViewModel = FirstAlarmTimeViewModel(settingsRepository)
    }

    @Test
    fun displayedFormat_ofFirstAlarmTime() {
        // given
        val firstAlarmTime = LocalTime.of(1, 9)
        `when`(settingsRepository.firstAlarmTime).thenReturn(firstAlarmTime)
        val expectedDisplayableTime = "01:09"
        firstAlarmTimeViewModel.firstAlarmTimeDisplayable.observeForever { } //needed to be called in order to read livedata value

        // when
        firstAlarmTimeViewModel.onViewCreated()

        val actualDisplayableTime = firstAlarmTimeViewModel.firstAlarmTimeDisplayable.value

        // then
        Assert.assertEquals(expectedDisplayableTime, actualDisplayableTime)
    }


}