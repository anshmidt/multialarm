package com.anshmidt.multialarm.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

/**
 * Receiver for finishing DismissAlarmActivity or closing notification
 * when music service is finished because of count down timer.
 */
class CountDownReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        TODO("Not yet implemented")
    }
}