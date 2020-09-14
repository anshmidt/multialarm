package com.anshmidt.multialarm.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.anshmidt.multialarm.R
import com.anshmidt.multialarm.databinding.ActivityDismissBinding
import com.anshmidt.multialarm.databinding.ActivityMainBinding
import com.anshmidt.multialarm.viewmodel.DismissAlarmViewModel
import com.anshmidt.multialarm.viewmodel.MainViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class DismissAlarmActivity : AppCompatActivity() {

    private val dismissAlarmViewModel: DismissAlarmViewModel by viewModel()

    private val binding: ActivityDismissBinding by lazy {
        DataBindingUtil.setContentView<ActivityDismissBinding>(this, R.layout.activity_dismiss)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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


}