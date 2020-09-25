package com.anshmidt.multialarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent

class AlarmScheduler(val context: Context) {

    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    fun schedule(firstAlarmTimeMillis: Long, intervalBetweenAlarmsMillis: Long) {
        val alarmIntent = getAlarmIntent()
        val alarmType = AlarmManager.RTC_WAKEUP
        alarmManager.setRepeating(
                alarmType,
                firstAlarmTimeMillis,
                intervalBetweenAlarmsMillis,
                alarmIntent
        )
    }

    private fun getAlarmIntent(): PendingIntent {
        val requestCode = 0
        val flag = PendingIntent.FLAG_UPDATE_CURRENT
        return Intent(context, AlarmBroadcastReceiver::class.java).let { intent ->
            PendingIntent.getBroadcast(context, requestCode, intent, flag)
        }
    }

    fun cancel() {
        val pendingIntent = getAlarmIntent()
        alarmManager.cancel(pendingIntent)
    }
}