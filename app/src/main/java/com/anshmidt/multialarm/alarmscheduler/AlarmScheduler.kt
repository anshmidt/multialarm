package com.anshmidt.multialarm.alarmscheduler

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.anshmidt.multialarm.data.AlarmSettings
import com.anshmidt.multialarm.data.TimeFormatter
import com.anshmidt.multialarm.data.getNextAlarmTimeMillis
import com.anshmidt.multialarm.data.isThereNextAlarm
import com.anshmidt.multialarm.di.DpsContext
import com.anshmidt.multialarm.logging.Log
import com.anshmidt.multialarm.notifications.dismissalarm.NotificationHelper
import com.anshmidt.multialarm.receivers.AlarmBroadcastReceiver
import com.anshmidt.multialarm.repository.IScheduleSettingsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class AlarmScheduler(val dpsContext: DpsContext) : KoinComponent {

    private val alarmManager = dpsContext.context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    private val notificationHelper: NotificationHelper by inject()
    private val scheduleSettingsRepository: IScheduleSettingsRepository by inject()

    companion object {
        val TAG = AlarmScheduler::class.java.simpleName
    }

    /**
     * Is used when user changes alarm settings
     */
    fun rescheduleAlarms(alarmSettings: AlarmSettings) {
        // Making sure alarm is within 24h in the future each time user reschedules
        val newFirstAlarmMillis = TimeFormatter.getAlarmMillisWithin24Hours(
            alarmSettings.firstAlarmTimeMillis
        )
        // Alarms are rescheduled by user, that means no alarms have rang yet
        val newNumberOfAlreadyRangAlarms = 0

        val newAlarmSettings = alarmSettings.copy(
            firstAlarmTimeMillis = newFirstAlarmMillis,
            numberOfAlreadyRangAlarms = newNumberOfAlreadyRangAlarms
        )

        CoroutineScope(SupervisorJob()).launch(Dispatchers.IO) {
            scheduleSettingsRepository.saveAlarmSettings(newAlarmSettings)
        }

        if (newAlarmSettings.areOn) {
            Log.d(TAG, "Rescheduling alarms: $newAlarmSettings")
            scheduleNextAlarm(newAlarmSettings)
        } else {
            cancel()
        }
    }

    /**
     * Is used when alarm is triggered, and app need to schedule next alarm
     */
    fun scheduleNext(alarmSettings: AlarmSettings) {
        if (alarmSettings.areOn) {
            Log.d(TAG, "Scheduling next alarm: $alarmSettings")
            scheduleNextAlarm(alarmSettings)
        } else {
            cancel()
        }
    }

    fun cancel() {
        Log.d(TAG, "Canceling alarms")
        val pendingIntent = getAlarmIntent()
        alarmManager.cancel(pendingIntent)
    }

    private fun scheduleNextAlarm(alarmSettings: AlarmSettings) {
        if (alarmSettings.isThereNextAlarm().not()) {
            Log.d(TAG, "scheduleNextAlarm: nothing to schedule. AlarmSettings: $alarmSettings")
            return
        }

        val nextAlarmTimeMillis = alarmSettings.getNextAlarmTimeMillis()
        nextAlarmTimeMillis?.let {
            scheduleOneAlarm(it)
        } ?: Log.d(TAG, "scheduleNextAlarm: no next alarm found")
    }

    private fun scheduleOneAlarm(timeMillis: Long) {
        val alarmIntent = getAlarmIntent()

        val alarmClockInfo = AlarmManager.AlarmClockInfo(timeMillis, null)

        if (canScheduleAlarms()) {
            alarmManager.setAlarmClock(
                alarmClockInfo,
                alarmIntent
            )
            Log.d(TAG, "Alarm scheduled: "+
                "${TimeFormatter.getLocalDateTime(timeMillis)} ($timeMillis)")
        } else {
            // TODO handle error message
            Log.d(TAG, "scheduleOneAlarm: Don't have permissions to schedule alarms")
            throw Exception("Don't have permissions to schedule alarms")
        }

        notificationHelper.createNotificationChannel()
    }

    private fun canScheduleAlarms(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            alarmManager.canScheduleExactAlarms()
        } else {
            true
        }
    }

    private fun getAlarmIntent(): PendingIntent {
        val requestCode = 0
        val flag = PendingIntent.FLAG_UPDATE_CURRENT
        return Intent(dpsContext.context, AlarmBroadcastReceiver::class.java).let { intent ->
            PendingIntent.getBroadcast(dpsContext.context, requestCode, intent, flag)
        }
    }
}