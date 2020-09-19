package com.anshmidt.multialarm

class CountDownTimerThatFinishesImmediately : ICountDownTimer {
    override fun init(millisInFuture: Long, countDownInterval: Long, doOnCountDownFinish: () -> Unit) {
        doOnCountDownFinish()
    }

    override fun start() {

    }

    override fun cancel() {

    }
}