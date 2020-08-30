package com.anshmidt.multialarm.viewmodel

import android.util.Log
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.anshmidt.multialarm.data.AlarmSettings
import com.anshmidt.multialarm.repository.AlarmSettingsRepository

open class MainViewModel(
    val repository: AlarmSettingsRepository
) : ViewModel(), IMainViewModel {


    companion object {
        private val TAG = MainViewModel::class.java.simpleName

    }

    val DEFAULT_ALARM_SWITCH_STATE = repository.alarmSwitchState

    var alarmSwitchState: Boolean = DEFAULT_ALARM_SWITCH_STATE
        get() {
            field = repository.alarmSwitchState
            return field
        }
        set(value) {
            repository.alarmSwitchState = value
            field = value
        }



    override fun onViewCreated() {

    }


    override fun onFirstAlarmTimeClicked() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onIntervalBetweenAlarmsClicked() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onNumberOfAlarmsClicked() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getAlarmSettingsFromModel(): AlarmSettings {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}