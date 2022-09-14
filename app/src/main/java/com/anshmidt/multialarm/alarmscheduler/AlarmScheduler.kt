package com.anshmidt.multialarm.alarmscheduler

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.anshmidt.multialarm.logging.Log
import com.anshmidt.multialarm.data.AlarmSettings
import com.anshmidt.multialarm.data.TimeFormatter
import com.anshmidt.multialarm.notifications.dismissalarm.NotificationHelper
import com.anshmidt.multialarm.receivers.AlarmBroadcastReceiver
import com.anshmidt.multialarm.repository.IScheduleSettingsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject
import java.util.concurrent.TimeUnit

class AlarmScheduler(val context: Context) : KoinComponent {

    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    private val notificationHelper: NotificationHelper by inject()
    private val scheduleSettingsRepository: IScheduleSettingsRepository by inject()

    companion object {
        val TAG = AlarmScheduler::class.java.simpleName
    }

    fun schedule(alarmSettings: AlarmSettings) {
        if (!alarmSettings.switchState) {
            Log.d(TAG, "Not scheduling the alarm because it's turned off in settings")
            return
        }
        val firstAlarmTimeMillis = TimeFormatter.getFirstAlarmTimeMillis(alarmSettings.firstAlarmTime)
        val intervalBetweenAlarmsMillis = TimeUnit.MINUTES.toMillis(alarmSettings.minutesBetweenAlarms.toLong())
        schedule(firstAlarmTimeMillis = firstAlarmTimeMillis, intervalBetweenAlarmsMillis = intervalBetweenAlarmsMillis)
        Log.d(TAG, "Alarm scheduled: $alarmSettings")

        CoroutineScope(SupervisorJob()).launch(Dispatchers.IO) {
            scheduleSettingsRepository.saveNumberOfAlreadyRangAlarms(0)
        }

        notificationHelper.createNotificationChannel()
    }

    private fun schedule(firstAlarmTimeMillis: Long, intervalBetweenAlarmsMillis: Long) {
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

    fun reschedule(alarmSettings: AlarmSettings) {
        cancel()
        schedule(alarmSettings)
    }

    fun cancel() {
        val pendingIntent = getAlarmIntent()
        alarmManager.cancel(pendingIntent)
    }
}