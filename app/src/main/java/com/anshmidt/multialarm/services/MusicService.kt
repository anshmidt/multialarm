package com.anshmidt.multialarm.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.anshmidt.multialarm.alarmscheduler.AlarmScheduler
import com.anshmidt.multialarm.countdowntimer.ICountDownTimer
import com.anshmidt.multialarm.logging.Log
import com.anshmidt.multialarm.musicplayer.IMusicPlayer
import com.anshmidt.multialarm.notifications.dismissalarm.NotificationHelper
import com.anshmidt.multialarm.repository.IRingtoneSettingRepository
import com.anshmidt.multialarm.repository.IScheduleSettingsRepository
import com.anshmidt.multialarm.view.activities.DismissAlarmActivity
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.zip
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject
import org.threeten.bp.LocalTime
import java.util.concurrent.TimeUnit

/**
 * Service for playing and stopping music when alarm notification or DismissAlarmActivity appears
 */
class MusicService : Service(), KoinComponent {

    private val musicPlayer: IMusicPlayer by inject()
    private val ringtoneSettingRepository: IRingtoneSettingRepository by inject()
    private val scheduleSettingsRepository: IScheduleSettingsRepository by inject()
    private val countDownTimer: ICountDownTimer by inject()
    private val notificationHelper: NotificationHelper by inject()
    private val alarmScheduler: AlarmScheduler by inject()
    private val scope = CoroutineScope(SupervisorJob())

    private var shouldShowNotification = SHOULD_SHOW_NOTIFICATION_DEFAULT_VALUE

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "MusicService started")
        intent?.let {
            shouldShowNotification = it.getBooleanExtra(INTENT_KEY_SHOULD_SHOW_NOTIFICATION,
                SHOULD_SHOW_NOTIFICATION_DEFAULT_VALUE)
        }

        if (shouldShowNotification) {
            scope.launch(Dispatchers.IO) {
                scheduleSettingsRepository.getUpcomingAlarmTime().first { upcomingAlarmTime ->
                    showNotification(upcomingAlarmTime)
                    // Alarm is considered as already rang at the moment when it starts ringing
                    checkNumberOfAlreadyRangAlarms()
                    return@first true
                }
            }

        }

        scope.launch(Dispatchers.IO) {
            ringtoneSettingRepository.getRingtoneUri()
                .zip(ringtoneSettingRepository.getMusicVolumePercents()) { ringtoneUri, musicVolumePercents ->
                    musicPlayer.play(ringtoneUri, musicVolumePercents)
                }.first()
        }

        CoroutineScope(Job() + Dispatchers.Main).launch {
            ringtoneSettingRepository.getRingtoneDurationSeconds().first { ringtoneDurationSeconds ->
                Log.d(TAG, "Started counting down. Ringtone duration: $ringtoneDurationSeconds")
                startCountDownTimer(
                        durationSeconds = ringtoneDurationSeconds,
                        doOnCountDownFinish = { doOnCountDownFinish() }
                )
                return@first true
            }
        }

//        return START_NOT_STICKY
        return START_STICKY
    }

    private fun checkNumberOfAlreadyRangAlarms() {
        scope.launch(Dispatchers.IO) {
            scheduleSettingsRepository.getNumberOfAlreadyRangAlarms()
                .zip(scheduleSettingsRepository.getNumberOfAlarms()) { numberOfAlreadyRangAlarms, numberOfAlarms ->
                    val newNumberOfAlreadyRangAlarms = numberOfAlreadyRangAlarms + 1
                    Log.d(TAG, "Saving number of already rang alarms: $newNumberOfAlreadyRangAlarms")
                    scheduleSettingsRepository.saveNumberOfAlreadyRangAlarms(newNumberOfAlreadyRangAlarms)

                    cancelAlarmsIfAllHaveRung(
                        numberOfAlreadyRangAlarms = newNumberOfAlreadyRangAlarms,
                        numberOfAlarms = numberOfAlarms
                    )
                }
                .first()
        }
    }

    private suspend fun cancelAlarmsIfAllHaveRung(numberOfAlreadyRangAlarms: Int, numberOfAlarms: Int) {
        if (numberOfAlreadyRangAlarms >= numberOfAlarms) {
            Log.d(TAG, "Canceling alarms because all of them have rung. numberOfAlreadyRangAlarms=$numberOfAlreadyRangAlarms, numberOfAlarms=$numberOfAlarms")
            alarmScheduler.cancel()
            scheduleSettingsRepository.saveAlarmSwitchState(false)
        }
    }

    private fun showNotification(alarmTime: LocalTime) {
        // Looks like notification should be started from the service according to guidelines,
        // not the other way around
        val notificationId = notificationHelper.getNotificationId(alarmTime)
        val notification = notificationHelper.buildNotification(notificationId, alarmTime)
        startForeground(notificationId, notification)
    }

    private fun startCountDownTimer(durationSeconds: Int, doOnCountDownFinish: () -> Unit) {
        val millisInFuture = TimeUnit.SECONDS.toMillis(durationSeconds.toLong())
        val countDownInterval = TimeUnit.SECONDS.toMillis(1)  // this argument of countdown timer is not used

        countDownTimer.init(
                millisInFuture = millisInFuture,
                countDownInterval = countDownInterval,
                doOnCountDownFinish = doOnCountDownFinish
        )
        countDownTimer.start()
    }

    /**
     * Is called when user dismisses the alarm from notification or from DismissAlarmActivity
     */
    override fun onDestroy() {
        musicPlayer.stop()
        countDownTimer.cancel()
        Log.d(TAG, "MusicService destroyed")
        super.onDestroy()
    }

    private fun doOnCountDownFinish() {
        Log.d(TAG, "Counting down finished")
        musicPlayer.stop()
        finishView()
        stopSelf()
    }

    private fun finishView() {
        // notification or DismissActivity could be present, they need to disappear if count down timer finished
        finishDismissActivity()
        cancelNotification()
    }

    private fun finishDismissActivity() {
        val intent = Intent(DismissAlarmActivity.COUNT_DOWN_FINISHED_ACTION)
        sendBroadcast(intent)
    }

    private fun cancelNotification() {
        notificationHelper.cancelNotification()
    }

    companion object {
        private val TAG = MusicService::class.java.simpleName
        const val INTENT_KEY_SHOULD_SHOW_NOTIFICATION = "intent_key_should_show_notification"
        private const val SHOULD_SHOW_NOTIFICATION_DEFAULT_VALUE = true
    }



}