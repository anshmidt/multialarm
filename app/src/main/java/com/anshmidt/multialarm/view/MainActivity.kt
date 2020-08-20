package com.anshmidt.multialarm.view

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.anshmidt.multialarm.R
import com.anshmidt.multialarm.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), MainView {

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        switch_main.setOnCheckedChangeListener { _, isChecked ->
            when (isChecked) {
                true -> viewModel.onAlarmSwitchTurnedOn()
                false -> viewModel.onAlarmSwitchTurnedOff()
            }
        }

        layout_first_alarm.setOnClickListener {
            viewModel.onFirstAlarmTimeClicked()
        }

        layout_interval.setOnClickListener {
            viewModel.onIntervalBetweenAlarmsClicked()
        }

        layout_numberofalarms.setOnClickListener {
            viewModel.onNumberOfAlarmsClicked()
        }

    }

    override fun displayAlarmSwitchState(switchState: Boolean) {
        switch_main.isChecked = switchState
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