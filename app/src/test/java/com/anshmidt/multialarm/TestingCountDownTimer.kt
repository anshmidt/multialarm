package com.anshmidt.multialarm

class TestingCountDownTimer : ICountDownTimer {
    override fun init(millisInFuture: Long, countDownInterval: Long, doOnCountDownFinish: () -> Unit) {
        doOnCountDownFinish()
    }

    override fun start() {

    }

    override fun cancel() {

    }
}