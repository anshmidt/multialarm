package com.anshmidt.multialarm.view

import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.anshmidt.multialarm.R
import com.anshmidt.multialarm.databinding.ActivityDismissBinding
import com.anshmidt.multialarm.viewmodel.DismissAlarmViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class DismissAlarmActivity : AppCompatActivity() {

    private val dismissAlarmViewModel: DismissAlarmViewModel by viewModel()

    private val binding: ActivityDismissBinding by lazy {
        DataBindingUtil.setContentView<ActivityDismissBinding>(this, R.layout.activity_dismiss)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        displayActivityOnLockedScreen()
        initBinding()

        dismissAlarmViewModel.finishView.observe(this, Observer {
            finish()
        })

        dismissAlarmViewModel.onViewCreated()
    }

    private fun initBinding() {
        binding.lifecycleOwner = this
        binding.dismissAlarmViewModel = dismissAlarmViewModel
    }

    override fun onPause() {
        super.onPause()
        dismissAlarmViewModel.onViewPaused()
    }

    private fun displayActivityOnLockedScreen() {
        val window: Window = window
        window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED)
        window.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        window.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON)
    }


}