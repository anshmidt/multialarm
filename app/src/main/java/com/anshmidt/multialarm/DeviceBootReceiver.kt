package com.anshmidt.multialarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.anshmidt.multialarm.repository.IAlarmSettingsRepository
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject

class DeviceBootReceiver : BroadcastReceiver(), KoinComponent {

    val alarmScheduler: AlarmScheduler by inject()
    val alarmSettingsRepository: IAlarmSettingsRepository by inject()

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == "android.intent.action.BOOT_COMPLETED") {
            scheduleAlarm()
        }
    }

    private fun scheduleAlarm() {
        val alarmSettings = alarmSettingsRepository.getSettings()
        alarmScheduler.schedule(alarmSettings)
    }
}