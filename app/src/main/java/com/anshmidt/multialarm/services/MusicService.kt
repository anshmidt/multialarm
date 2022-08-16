package com.anshmidt.multialarm.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.anshmidt.multialarm.countdowntimer.ICountDownTimer
import com.anshmidt.multialarm.musicplayer.MusicPlayer
import com.anshmidt.multialarm.notifications.dismissalarm.NotificationHelper
import com.anshmidt.multialarm.repository.IRingtoneSettingRepository
import com.anshmidt.multialarm.repository.ISettingsRepository
import com.anshmidt.multialarm.view.activities.DismissAlarmActivity
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject
import java.util.concurrent.TimeUnit

/**
 * Service for playing and stopping music when alarm notification or DismissAlarmActivity appears
 */
class MusicService : Service(), KoinComponent {

    private val musicPlayer: MusicPlayer by inject()
    private val ringtoneSettingRepository: IRingtoneSettingRepository by inject()
    private val countDownTimer: ICountDownTimer by inject()
    private val notificationHelper: NotificationHelper by inject()


    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val songUri = ringtoneSettingRepository.songUri
        musicPlayer.play(songUri)
        val songDurationSeconds = ringtoneSettingRepository.songDurationSeconds
        startCountDownTimer(durationSeconds = songDurationSeconds, doOnCountDownFinish = this::doOnCountDownFinish)

        return START_NOT_STICKY
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
        musicPlayer.stop()
        super.onDestroy()
    }

    private fun doOnCountDownFinish() {
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




}