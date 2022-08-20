package com.anshmidt.multialarm.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.anshmidt.multialarm.alarmscheduler.AlarmScheduler
import com.anshmidt.multialarm.repository.IScheduleSettingsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject

class DeviceBootReceiver : BroadcastReceiver(), KoinComponent {

    private val alarmScheduler: AlarmScheduler by inject()
    private val scheduleSettingsRepository: IScheduleSettingsRepository by inject()
    private val scope = CoroutineScope(SupervisorJob())

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == "android.intent.action.BOOT_COMPLETED") {
            rescheduleAlarm()
        }
    }

    private fun rescheduleAlarm() {
        scope.launch(Dispatchers.IO) {
            scheduleSettingsRepository.getAlarmSettings().collect { alarmSettings ->
                alarmScheduler.reschedule(alarmSettings)
            }
        }
    }
}