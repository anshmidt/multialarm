package com.anshmidt.multialarm.countdowntimer

/**
 * CountDownTimer timer is implemented via interface in order to improve testability
 */
interface ICountDownTimer {
    fun init(millisInFuture: Long, countDownInterval: Long, doOnCountDownFinish: () -> Unit)
    fun start()
    fun cancel()
}