package com.anshmidt.multialarm.viewmodel

import android.util.Log
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.anshmidt.multialarm.SingleLiveEvent
import com.anshmidt.multialarm.data.AlarmSettings
import com.anshmidt.multialarm.data.TimeFormatter
import com.anshmidt.multialarm.repository.AlarmSettingsRepository
import org.threeten.bp.LocalTime

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

    var firstAlarmTime: LocalTime
        get() = repository.firstAlarmTime
        set(value) {
            repository.firstAlarmTime = value
        }

    var firstAlarmTimeLiveData = MutableLiveData<LocalTime>()


    var minutesBetweenAlarms: Int
        get() = repository.minutesBetweenAlarms
        set(value) {
            repository.minutesBetweenAlarms = value
        }

    var numberOfAlarms: Int
        get() = repository.numberOfAlarms
        set(value) {
            repository.numberOfAlarms = value
        }

    private val _openFirstAlarmTimeDialog = SingleLiveEvent<Any>()

    val openFirstAlarmTimeDialog: LiveData<Any>
        get() = _openFirstAlarmTimeDialog

    private lateinit var firstAlarmTimeSelectedOnPicker: LocalTime

    fun onActivityCreated() {
        firstAlarmTimeLiveData.value = repository.firstAlarmTime
    }

    override fun getFirstAlarmTimeDisplayable(): String {
//        return TimeFormatter.getDisplayableTime(firstAlarmTime)
        return TimeFormatter.getDisplayableTime(firstAlarmTimeLiveData.value!!)
    }

    override fun onFirstAlarmTimeClicked() {
        _openFirstAlarmTimeDialog.call()
    }

    override fun onMinutesBetweenAlarmsClicked() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onNumberOfAlarmsClicked() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onOkButtonClickInFirstAlarmDialog() {
        firstAlarmTimeLiveData.value = firstAlarmTimeSelectedOnPicker
        repository.firstAlarmTime = firstAlarmTimeSelectedOnPicker
    }

    override fun onCancelButtonClickInFirstAlarmDialog() {
        //close dialog without saving selected time
    }

    override fun onFirstAlarmTimeSelectedOnPicker(hour: Int, minute: Int) {
        firstAlarmTimeSelectedOnPicker = LocalTime.of(hour, minute)
    }
}