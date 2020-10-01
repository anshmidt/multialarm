package com.anshmidt.multialarm.countdowntimer

import android.os.CountDownTimer

class DefaultCountDownTimer : ICountDownTimer {

    private lateinit var countDownTimer: CountDownTimer

    override fun init(millisInFuture: Long, countDownInterval: Long, doOnCountDownFinish: () -> Unit) {
        countDownTimer = object : CountDownTimer(millisInFuture, countDownInterval) {
            override fun onFinish() {
                doOnCountDownFinish()
            }

            override fun onTick(millisUntilFinished: Long) {
            }
        }
    }

    override fun start() {
        countDownTimer.start()
    }

    override fun cancel() {
        countDownTimer.cancel()
    }


}