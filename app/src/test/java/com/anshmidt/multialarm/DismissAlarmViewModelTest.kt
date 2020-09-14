package com.anshmidt.multialarm

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.anshmidt.multialarm.repository.AlarmSettingsRepository
import com.anshmidt.multialarm.repository.IAlarmSettingsRepository
import com.anshmidt.multialarm.viewmodel.DismissAlarmViewModel
import com.anshmidt.multialarm.viewmodel.MainViewModel
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

class DismissAlarmViewModelTest {

    @Mock
    private lateinit var alarmSettingsRepository: IAlarmSettingsRepository

    @Mock
    private lateinit var musicPlayer: IMusicPlayer

    @Mock
    private lateinit var countDownTimer: ICountDownTimer

    private lateinit var dismissAlarmViewModel: DismissAlarmViewModel

    // Allows to set livedata
    @get:Rule
    var instantExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        dismissAlarmViewModel = DismissAlarmViewModel(alarmSettingsRepository, musicPlayer, countDownTimer)
    }

    @Test
    fun playerStartsOnViewCreated() {

        //when
        dismissAlarmViewModel.onViewCreated()

        //then
        verify(musicPlayer, times(1)).play()
    }

    @Test
    fun playerStopsOnViewDestroyed() {
        //when
        dismissAlarmViewModel.onViewPaused()

        //then
        verify(musicPlayer, times(1)).stop()
    }

    @Test
    fun playerStopsOnButtonClicked() {
        //gived
        dismissAlarmViewModel.onViewCreated()

        //when
        dismissAlarmViewModel.onDismissButtonClicked()

        //then
        verify(musicPlayer, times(1)).stop()
    }
}