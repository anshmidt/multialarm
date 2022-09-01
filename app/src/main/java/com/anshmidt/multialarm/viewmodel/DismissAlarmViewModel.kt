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

    /**
     * When MusicService is destroyed, it doesn't finish the view.
     */
    fun onDismissButtonClicked() {
        _stopMusicService.call()
        _finishView.call()
    }

    /**
     * If something appears on top of DismissAlarm screen (for example incoming call screen or another app),
     * then we stop the music and close the DismissAlarm screen.
     * Remember that this is also called after user clicks Dismiss, or count down finishes.
     */
    fun onViewPaused() {
    }

    /**
     * On count down finish, MusicService informs the view by sending intent to DismissAlarmActivity
     * (and after that, the service stops itself). When the intent is received in activity,
     * this method is triggered.
     */
    fun onCountDownFinished() {
        _finishView.call()
    }


}