package com.anshmidt.multialarm.view

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.anshmidt.multialarm.R
import com.anshmidt.multialarm.databinding.ActivityMainBinding
import com.anshmidt.multialarm.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity(), MainView {

    val mainViewModel: MainViewModel by viewModel()


    private val binding: ActivityMainBinding by lazy {
        DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.mainViewModel = mainViewModel
        mainViewModel.onViewCreated()
    }



    override fun displayFirstAlarmTime(firstAlarmTime: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun displayIntervalBetweenAlarms(intervalBetweenAlarmsMinutes: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun displayNumberOfAlarms(numberOfAlarms: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}