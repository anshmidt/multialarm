package com.anshmidt.multialarm.services

import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import com.anshmidt.multialarm.countdowntimer.ICountDownTimer
import com.anshmidt.multialarm.musicplayer.IMusicPlayer
import com.anshmidt.multialarm.notifications.dismissalarm.NotificationHelper
import com.anshmidt.multialarm.repository.IRingtoneSettingRepository
import com.anshmidt.multialarm.view.activities.DismissAlarmActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject
import java.util.concurrent.TimeUnit

/**
 * Service for playing and stopping music when alarm notification or DismissAlarmActivity appears
 */
class MusicService : Service(), KoinComponent {

    private val musicPlayer: IMusicPlayer by inject()
    private val ringtoneSettingRepository: IRingtoneSettingRepository by inject()
    private val countDownTimer: ICountDownTimer by inject()
    private val notificationHelper: NotificationHelper by inject()
    private val scope = CoroutineScope(SupervisorJob())


    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        scope.launch(Dispatchers.IO) {
            ringtoneSettingRepository.getRingtoneUri().collect { ringtoneUri ->
                Log.d(TAG, "Music started")
                //        musicPlayer.play(ringtoneUri)
            }
        }

        scope.launch(Dispatchers.IO) {
            ringtoneSettingRepository.getRingtoneDurationSeconds().collect { ringtoneDurationSeconds ->
                startCountDownTimer(
                        durationSeconds = ringtoneDurationSeconds,
                        doOnCountDownFinish = { doOnCountDownFinish() }
                )
            }
        }

        return START_NOT_STICKY
    }

    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Looks like notification should be started from the service according to Android design,
            // not the other way around
            //startForeground(1, notification)

        }
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

    override fun onDestroy() {
        Log.d(TAG, "Music stopped")
//        musicPlayer.stop()
        super.onDestroy()
    }

    private fun doOnCountDownFinish() {
        Log.d(TAG, "Music stopped")
//        musicPlayer.stop()
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
        val TAG = MusicService::class.java.simpleName
    }



}