package com.anshmidt.multialarm

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.anshmidt.multialarm.countdowntimer.ICountDownTimer
import com.anshmidt.multialarm.musicplayer.IMusicPlayer
import com.anshmidt.multialarm.repository.IScheduleSettingsRepository
import com.anshmidt.multialarm.viewmodel.DismissAlarmViewModel
import org.junit.Before
import org.junit.Rule
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

class DismissAlarmViewModelTest {

    @Mock
    private lateinit var settingsRepository: IScheduleSettingsRepository

    @Mock
    private lateinit var musicPlayer: IMusicPlayer

    @Mock
    private lateinit var countDownTimer: ICountDownTimer

    private val countDownTimerThatFinishesImmediately = CountDownTimerThatFinishesImmediately()

    private lateinit var dismissAlarmViewModel: DismissAlarmViewModel


    // Allows to set livedata
    @get:Rule
    var instantExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

//        val ringtoneDurationSeconds = 1
//        `when`(settingsRepository.ringtoneDurationSeconds).thenReturn(ringtoneDurationSeconds)

        dismissAlarmViewModel = DismissAlarmViewModel()
    }


//    @Test
//    fun playerStartsOnViewCreated() {
//        //when
//        dismissAlarmViewModel.onViewCreated()
//
//        //then
//        verify(musicPlayer).play(anyOrNull())
//    }
//
//    @Test
//    fun playerStopsOnViewDestroyed() {
//        //given
//        dismissAlarmViewModel.onViewCreated()
//
//        //when
//        dismissAlarmViewModel.onViewPaused()
//
//        //then
//        verify(musicPlayer).stop()
//    }
//
//    @Test
//    fun playerStopsOnButtonClicked() {
//        //given
//        dismissAlarmViewModel.onViewCreated()
//
//        //when
//        dismissAlarmViewModel.onDismissButtonClicked()
//
//        //then
//        verify(musicPlayer).stop()
//    }
//
//    @Test
//    fun playerStopsWhenCountDownTimerFinishes() {
//        //given
//        dismissAlarmViewModel = DismissAlarmViewModel()
//
//        //when
//        dismissAlarmViewModel.onViewCreated()
//
//        //then
//        verify(musicPlayer).stop()
//    }
}