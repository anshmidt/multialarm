package com.anshmidt.multialarm.alarmscheduler

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.anshmidt.multialarm.data.AlarmSettings
import com.anshmidt.multialarm.data.getNextAlarmTimeMillis
import com.anshmidt.multialarm.data.isThereNextAlarm
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

class AlarmScheduler(val context: Context) : KoinComponent {

    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    private val notificationHelper: NotificationHelper by inject()
    private val scheduleSettingsRepository: IScheduleSettingsRepository by inject()

    companion object {
        val TAG = AlarmScheduler::class.java.simpleName
    }

    /**
     * Is used when user changes alarm settings
     */
    fun rescheduleAlarms(alarmSettings: AlarmSettings) {
        val newAlarmSettings = alarmSettings.copy(numberOfAlreadyRangAlarms = 0)
        CoroutineScope(SupervisorJob()).launch(Dispatchers.IO) {
            scheduleSettingsRepository.saveAlarmSettings(newAlarmSettings)
        }

        if (alarmSettings.areOn) {
            scheduleNextAlarm(alarmSettings)
        } else {
            cancel()
        }
    }

    /**
     * Is used when alarm is triggered, and app need to schedule next alarm
     */
    fun scheduleNext(alarmSettings: AlarmSettings) {
        if (alarmSettings.areOn) {
            scheduleNextAlarm(alarmSettings)
        } else {
            cancel()
        }
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
        } else {
            // TODO handle error message
            Log.d(TAG, "scheduleOneAlarm: Don't have permissions to schedule alarms")
            throw Exception("Don't have permissions to schedule alarms")
        }

        notificationHelper.createNotificationChannel()
    }

//    private fun schedule(firstAlarmTimeMillis: Long, intervalBetweenAlarmsMillis: Long) {
//        val alarmIntent = getAlarmIntent()
//        val alarmType = AlarmManager.RTC_WAKEUP
//        /**
//         * setExactAndAllowWhileIdle() limitations:
//         * - Can only trigger at least 15 minutes apart while the device is in Doze mode.
//         * If you try to schedule alarms closer together, they will not be triggered during Doze.
//         * However, when the device is not in Doze mode, there’s no such interval limitation,
//         * and you can schedule alarms with shorter intervals.
//         *
//         * setAlarmClock() limitations:
//         * - only works with the RTC_WAKEUP timebase
//         *
//         * Starting with Android 12, I need SCHEDULE_EXACT_ALARM permission.
//         */
//
//        val calendar = Calendar.getInstance().apply {
//            timeInMillis = System.currentTimeMillis()
//            set(Calendar.HOUR_OF_DAY, 12)
//            set(Calendar.MINUTE, 5)
//            set(Calendar.SECOND, 0)
//        }
//
//        val alarmClockInfo = AlarmManager.AlarmClockInfo(calendar.timeInMillis, null)
//
//        if (canScheduleAlarms()) {
//            alarmManager.setAlarmClock(
//                alarmClockInfo,
//                alarmIntent
//            )
//        } else {
//            // TODO handle error message
//            throw Exception("Don't have permissions to schedule alarms")
//        }
//
//        alarmManager.setRepeating(
//            alarmType,
//            firstAlarmTimeMillis,
//            intervalBetweenAlarmsMillis,
//            alarmIntent
//        )
//        Log.d(TAG, "Scheduling alarm via setRepeating(firstAlarmTimeMillis=$firstAlarmTimeMillis, intervalBetweenAlarmsMillis=$intervalBetweenAlarmsMillis)")
//    }

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
        return Intent(context, AlarmBroadcastReceiver::class.java).let { intent ->
            PendingIntent.getBroadcast(context, requestCode, intent, flag)
        }
    }

    fun cancel() {
        val pendingIntent = getAlarmIntent()
        alarmManager.cancel(pendingIntent)
    }
}