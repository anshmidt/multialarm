package com.anshmidt.multialarm.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.anshmidt.multialarm.R
import com.anshmidt.multialarm.databinding.ActivityMainBinding
import com.anshmidt.multialarm.viewmodel.MainViewModel
import com.anshmidt.multialarm.viewmodel.MinutesBetweenAlarmsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity(), MainView {

    private val mainViewModel: MainViewModel by viewModel()
    private val minutesBetweenAlarmsViewModel: MinutesBetweenAlarmsViewModel by viewModel()


    private val binding: ActivityMainBinding by lazy {
        DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBinding()

        mainViewModel.openFirstAlarmTimeDialog.observe(this, Observer {
            openFirstAlarmTimeDialog()
        })

        minutesBetweenAlarmsViewModel.openMinutesBetweenAlarmsDialog.observe(this, Observer {
            openMinutesBetweenAlarmsDialog()
        })

        mainViewModel.onViewCreated()
        minutesBetweenAlarmsViewModel.onViewCreated()
    }

    private fun initBinding() {
        binding.lifecycleOwner = this
        binding.mainViewModel = mainViewModel
        binding.minutesBetweenAlarmsViewModel = minutesBetweenAlarmsViewModel
    }

    private fun openFirstAlarmTimeDialog() {
        val dialog = FirstAlarmTimeDialogFragment()
        dialog.show(supportFragmentManager, FirstAlarmTimeDialogFragment.FRAGMENT_TAG)
    }

    private fun openMinutesBetweenAlarmsDialog() {
        val dialog = MinutesBetweenAlarmsDialogFragment()
        dialog.show(supportFragmentManager, MinutesBetweenAlarmsDialogFragment.FRAGMENT_TAG)
    }

    override fun onResume() {
        super.onResume()
        mainViewModel.onViewResumed()
    }

    override fun onPause() {
        super.onPause()
        mainViewModel.onViewPaused()
    }

    override fun onDestroy() {
        super.onDestroy()
        mainViewModel.onViewDestroyed()
    }

}