package com.anshmidt.multialarm.notifications.dismissalarm

import java.util.concurrent.TimeUnit

class NotificationParams {

    val notificationId: Int
        get() {
            val currentTimeMillis = System.currentTimeMillis()
            val currentTimeMinutes: Int = TimeUnit.MILLISECONDS.toMinutes(currentTimeMillis).toInt()
            return currentTimeMinutes  // this way notificationId will be unique for each alarm
        }

    companion object {
        const val CHANNEL_ID = "alarm"
    }

}