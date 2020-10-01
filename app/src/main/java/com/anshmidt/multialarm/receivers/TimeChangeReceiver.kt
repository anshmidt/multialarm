package com.anshmidt.multialarm.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.anshmidt.multialarm.alarmscheduler.AlarmScheduler
import com.anshmidt.multialarm.repository.ISettingsRepository
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject

class TimeChangeReceiver : BroadcastReceiver(), KoinComponent {

    private val alarmScheduler: AlarmScheduler by inject()
    private val settingsRepository: ISettingsRepository by inject()

    override fun onReceive(context: Context?, intent: Intent?) {
        val action = intent?.action

        if (Intent.ACTION_TIME_CHANGED == action || Intent.ACTION_TIMEZONE_CHANGED == action) {
            rescheduleAlarm()
        }
    }

    private fun rescheduleAlarm() {
        alarmScheduler.cancel()
        val alarmSettings = settingsRepository.getSettings()
        alarmScheduler.schedule(alarmSettings)
    }
}