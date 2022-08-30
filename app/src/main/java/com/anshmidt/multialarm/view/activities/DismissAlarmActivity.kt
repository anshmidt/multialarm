package com.anshmidt.multialarm.view.activities

import android.app.KeyguardManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.anshmidt.multialarm.R
import com.anshmidt.multialarm.databinding.ActivityDismissAnimatedBinding
import com.anshmidt.multialarm.services.MusicService
import com.anshmidt.multialarm.viewmodel.DismissAlarmViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel








class DismissAlarmActivity : AppCompatActivity() {

    private val dismissAlarmViewModel: DismissAlarmViewModel by viewModel()

    private val binding: ActivityDismissAnimatedBinding by lazy {
        DataBindingUtil.setContentView<ActivityDismissAnimatedBinding>(this, R.layout.activity_dismiss_animated)
    }

    private val countDownFinishReceiver: BroadcastReceiver by lazy {
        object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                dismissAlarmViewModel.onCountDownFinished()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        displayActivityOnLockedScreen()
        hideStatusBar()
        initBinding()
        registerCountDownFinishReceiver(countDownFinishReceiver)
        startButtonAnimation()

        dismissAlarmViewModel.finishView.observe(this, Observer {
            finish()
        })

        dismissAlarmViewModel.stopMusicService.observe(this, Observer {
            stopMusicService()
        })

        dismissAlarmViewModel.onViewCreated()
    }

    private fun registerCountDownFinishReceiver(countDownFinishReceiver: BroadcastReceiver) {
        val intentFilter = IntentFilter(COUNT_DOWN_FINISHED_ACTION)
        registerReceiver(countDownFinishReceiver, intentFilter)
    }

    private fun initBinding() {
        binding.lifecycleOwner = this
        binding.dismissAlarmViewModel = dismissAlarmViewModel
    }

    override fun onPause() {
        super.onPause()
        dismissAlarmViewModel.onViewPaused()
    }

    private fun hideStatusBar() {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        actionBar?.hide()
    }

    private fun displayActivityOnLockedScreen() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true)
            setTurnScreenOn(true)
            val keyguardManager = getSystemService(KEYGUARD_SERVICE) as KeyguardManager
            keyguardManager.requestDismissKeyguard(this, null)
        } else {
            val window: Window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED)
            window.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD)
//            window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            window.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON)
        }


    }

    private fun stopMusicService() {
        val intent = Intent(this, MusicService::class.java)
        stopService(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(countDownFinishReceiver)
    }

    private fun startButtonAnimation() {
        binding.dismissButton.start()
    }


    companion object {
        const val COUNT_DOWN_FINISHED_ACTION = "countDownIntentKey"
    }

}