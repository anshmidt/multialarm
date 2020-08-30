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
    private val repository: AlarmSettingsRepository
) : ViewModel(), IMainViewModel {


    companion object {
        private val TAG = MainViewModel::class.java.simpleName

    }

    var alarmSwitchState: Boolean
        get() = repository.alarmSwitchState
        set(value) {
            repository.alarmSwitchState = value
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