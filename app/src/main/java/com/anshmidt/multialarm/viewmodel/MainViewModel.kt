package com.anshmidt.multialarm.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.anshmidt.multialarm.data.AlarmSettings
import com.anshmidt.multialarm.repository.AlarmSettingsRepository

class MainViewModel(
    val repository: AlarmSettingsRepository
) : ViewModel(), IMainViewModel {

    val alarmSwitchState: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    override fun onViewCreated() {
        repository.alarmSwitchState
    }

    override fun onAlarmSwitchTurnedOn() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onAlarmSwitchTurnedOff() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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