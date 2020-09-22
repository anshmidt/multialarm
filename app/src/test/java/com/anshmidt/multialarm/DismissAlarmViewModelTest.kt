package com.anshmidt.multialarm

import android.media.RingtoneManager
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.anshmidt.multialarm.repository.IAlarmSettingsRepository
import com.anshmidt.multialarm.viewmodel.DismissAlarmViewModel
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.anyOrNull

class DismissAlarmViewModelTest {

    @Mock
    private lateinit var alarmSettingsRepository: IAlarmSettingsRepository

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

        val songDurationSeconds = 1
        `when`(alarmSettingsRepository.songDurationSeconds).thenReturn(songDurationSeconds)

        dismissAlarmViewModel = DismissAlarmViewModel(alarmSettingsRepository, musicPlayer, countDownTimer)
    }

    @Test
    fun playerStartsOnViewCreated() {
        //when
        dismissAlarmViewModel.onViewCreated()

        //then
        verify(musicPlayer).play(anyOrNull())
    }

    @Test
    fun playerStopsOnViewDestroyed() {
        //given
        dismissAlarmViewModel.onViewCreated()

        //when
        dismissAlarmViewModel.onViewPaused()

        //then
        verify(musicPlayer).stop()
    }

    @Test
    fun playerStopsOnButtonClicked() {
        //given
        dismissAlarmViewModel.onViewCreated()

        //when
        dismissAlarmViewModel.onDismissButtonClicked()

        //then
        verify(musicPlayer).stop()
    }

    @Test
    fun playerStopsWhenCountDownTimerFinishes() {
        //given
        dismissAlarmViewModel = DismissAlarmViewModel(alarmSettingsRepository, musicPlayer, countDownTimerThatFinishesImmediately)

        //when
        dismissAlarmViewModel.onViewCreated()

        //then
        verify(musicPlayer).stop()
    }
}