package com.anshmidt.multialarm

import com.anshmidt.multialarm.countdowntimer.ICountDownTimer

class CountDownTimerThatFinishesImmediately : ICountDownTimer {
    override fun init(millisInFuture: Long, countDownInterval: Long, doOnCountDownFinish: () -> Unit) {
        doOnCountDownFinish()
    }

    override fun start() {

    }

    override fun cancel() {

    }
}