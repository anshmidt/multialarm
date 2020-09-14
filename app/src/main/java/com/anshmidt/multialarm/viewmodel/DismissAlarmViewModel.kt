package com.anshmidt.multialarm.viewmodel

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.anshmidt.multialarm.IMusicPlayer
import com.anshmidt.multialarm.SingleLiveEvent
import com.anshmidt.multialarm.repository.IAlarmSettingsRepository
import java.util.concurrent.TimeUnit

class DismissAlarmViewModel (
        private val repository: IAlarmSettingsRepository,
        private val musicPlayer: IMusicPlayer
) : ViewModel() {

    private val _finishView = SingleLiveEvent<Any>()
    val finishView: LiveData<Any>
        get() = _finishView

    fun onViewCreated() {
        musicPlayer.play()
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
        val countDownTimer = object : CountDownTimer(millisInFuture, countDownInterval) {
            override fun onFinish() {
                doOnCountDownFinish()
            }

            override fun onTick(millisUntilFinished: Long) {
            }
        }
        countDownTimer.start()
    }

}