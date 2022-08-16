package com.anshmidt.multialarm.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.anshmidt.multialarm.alarmscheduler.AlarmScheduler
import com.anshmidt.multialarm.repository.IScheduleSettingsRepository
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject

class DeviceBootReceiver : BroadcastReceiver(), KoinComponent {

    private val alarmScheduler: AlarmScheduler by inject()
    private val scheduleSettingsRepository: IScheduleSettingsRepository by inject()

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == "android.intent.action.BOOT_COMPLETED") {
            scheduleAlarm()
        }
    }

    private fun scheduleAlarm() {
        alarmScheduler.cancel()
        val alarmSettings = scheduleSettingsRepository.getSettings()
        alarmScheduler.schedule(alarmSettings)
    }
}