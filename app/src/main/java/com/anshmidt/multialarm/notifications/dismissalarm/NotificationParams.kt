package com.anshmidt.multialarm.notifications.dismissalarm

import java.util.concurrent.TimeUnit

class NotificationParams {

    val notificationId: Int
        get() {
            //        val currentTimeDate = LocalDateTime.now()
//        val hours = currentTimeDate.hour
//        val minutes = currentTimeDate.minute
//        val notificationId = hours * 60 + minutes  // this way notificationId will be unique for each alarm
            val currentTimeMillis = System.currentTimeMillis()
            val currentTimeMinutes: Int = TimeUnit.MILLISECONDS.toMinutes(currentTimeMillis).toInt()
            return currentTimeMinutes  // this way notificationId will be unique for each alarm
        }

}