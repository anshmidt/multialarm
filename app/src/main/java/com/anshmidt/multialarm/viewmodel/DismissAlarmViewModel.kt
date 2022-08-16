package com.anshmidt.multialarm.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.anshmidt.multialarm.data.SingleLiveEvent

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