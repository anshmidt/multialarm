package com.anshmidt.multialarm.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.anshmidt.multialarm.countdowntimer.ICountDownTimer
import com.anshmidt.multialarm.musicplayer.IMusicPlayer
import com.anshmidt.multialarm.data.SingleLiveEvent
import com.anshmidt.multialarm.repository.ISettingsRepository
import java.util.concurrent.TimeUnit

class DismissAlarmViewModel : ViewModel() {

    private val _finishView = SingleLiveEvent<Any>()
    val finishView: LiveData<Any>
        get() = _finishView

    private val _stopMusicService = SingleLiveEvent<Any>()
    val stopMusicService: LiveData<Any>
        get() = _stopMusicService

    fun onViewCreated() {

    }

    fun onDismissButtonClicked() {
        _stopMusicService.call()
        _finishView.call()
    }


    fun onViewPaused() {
        _stopMusicService.call()
        _finishView.call()
    }

    fun onCountDownFinished() {
        _finishView.call()
    }

}