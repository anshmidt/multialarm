package com.anshmidt.multialarm.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.anshmidt.multialarm.data.AlarmSettings
import com.anshmidt.multialarm.repository.AlarmSettingsRepository

class MainViewModel(
    val repository: AlarmSettingsRepository
) : ViewModel(), IMainViewModel {

    val alarmSwitchState: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }


    override fun onViewCreated() {
        alarmSwitchState.setValue(repository.alarmSwitchState)
    }

    override fun onAlarmSwitchTurnedOn() {
        repository.alarmSwitchState = true
    }

    override fun onAlarmSwitchTurnedOff() {
        repository.alarmSwitchState = false
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