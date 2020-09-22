package com.anshmidt.multialarm.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.anshmidt.multialarm.ICountDownTimer
import com.anshmidt.multialarm.IMusicPlayer
import com.anshmidt.multialarm.SingleLiveEvent
import com.anshmidt.multialarm.repository.IAlarmSettingsRepository
import java.util.concurrent.TimeUnit

class DismissAlarmViewModel (
        private val repository: IAlarmSettingsRepository,
        private val musicPlayer: IMusicPlayer,
        private val countDownTimer: ICountDownTimer
) : ViewModel() {

    private val _finishView = SingleLiveEvent<Any>()
    val finishView: LiveData<Any>
        get() = _finishView

    fun onViewCreated() {
        val songUri = repository.songUri
        musicPlayer.play(songUri)
        val songDurationSeconds = repository.songDurationSeconds
        startCountDownTimer(durationSeconds = songDurationSeconds, doOnCountDownFinish = this::doOnCountDownFinish)
    }

    fun onDismissButtonClicked() {
        musicPlayer.stop()
        _finishView.call()
    }

    private fun doOnCountDownFinish() {
        musicPlayer.stop()
        _finishView.call()
    }

    private fun startCountDownTimer(durationSeconds: Int, doOnCountDownFinish: () -> Unit) {
        val millisInFuture = TimeUnit.SECONDS.toMillis(durationSeconds.toLong())
        val countDownInterval = TimeUnit.SECONDS.toMillis(1)  // is not really used

        countDownTimer.init(millisInFuture, countDownInterval, doOnCountDownFinish)
        countDownTimer.start()
    }

    fun onViewPaused() {
        musicPlayer.stop()
    }

}