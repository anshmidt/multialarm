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

    private lateinit var binding: ActivityMainBinding

//    private val binding: ActivityMainBinding by lazy {
//        DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        binding.mainViewModel = mainViewModel

//        setContentView(R.layout.activity_main)


//        switch_main.setOnCheckedChangeListener { _, isChecked ->
//            // ignore cases when isChecked value is changed programmatically
//            if (!switch_main.isPressed) {
//                return@setOnCheckedChangeListener
//            }
//
//            when (isChecked) {
//                true -> mainViewModel.onAlarmSwitchTurnedOn()
//                false -> mainViewModel.onAlarmSwitchTurnedOff()
//            }
//        }
//
//        layout_first_alarm.setOnClickListener {
//            mainViewModel.onFirstAlarmTimeClicked()
//        }
//
//        layout_interval.setOnClickListener {
//            mainViewModel.onIntervalBetweenAlarmsClicked()
//        }
//
//        layout_numberofalarms.setOnClickListener {
//            mainViewModel.onNumberOfAlarmsClicked()
//        }
//
//        val alarmSwitchStateObserver = Observer<Boolean> { alarmSwitchState ->
//            displayAlarmSwitchState(alarmSwitchState)
//        }
//
//        mainViewModel.alarmSwitchState.observe(this, alarmSwitchStateObserver)

        mainViewModel.onViewCreated()
    }

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
//        binding.mainViewModel = mainViewModel
        return super.onCreateView(name, context, attrs)
    }



    override fun displayAlarmSwitchState(switchState: Boolean) {
//        switch_main.isChecked = switchState
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