package com.anshmidt.multialarm.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.anshmidt.multialarm.SingleLiveEvent
import com.anshmidt.multialarm.data.TimeFormatter
import com.anshmidt.multialarm.repository.AlarmSettingsRepository
import com.anshmidt.multialarm.repository.IAlarmSettingsRepository
import org.threeten.bp.Duration
import org.threeten.bp.LocalTime

open class MainViewModel(
    private val repository: IAlarmSettingsRepository
) : ViewModel(), IMainViewModel {


    companion object {
        private val TAG = MainViewModel::class.java.simpleName
    }

    var alarmSwitchState: Boolean
        get() = repository.alarmSwitchState
        set(value) {
            repository.alarmSwitchState = value
        }


    var _firstAlarmTime = MutableLiveData<LocalTime>()
    val firstAlarmTime: LiveData<LocalTime> = _firstAlarmTime
    var firstAlarmTimeDisplayable = Transformations.map(firstAlarmTime) { localTime ->
        TimeFormatter.getDisplayableTime(localTime)
    }
    private lateinit var firstAlarmTimeSelectedOnPicker: LocalTime
    var firstAlarmTimeSelectedOnPickerLiveData = MutableLiveData<LocalTime>()
    var timeLeftBeforeFirstAlarm = Transformations.map(firstAlarmTime) { alarmTime ->
        alarmTime.toString()
    }

    private val _openFirstAlarmTimeDialog = SingleLiveEvent<Any>()
    val openFirstAlarmTimeDialog: LiveData<Any>
        get() = _openFirstAlarmTimeDialog




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



    fun onActivityCreated() {
        _firstAlarmTime.value = repository.firstAlarmTime
    }

    override fun getFirstAlarmTimeDisplayable(): String {
        return TimeFormatter.getDisplayableTime(firstAlarmTime.value!!)
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
        _firstAlarmTime.value = firstAlarmTimeSelectedOnPickerLiveData.value
        repository.firstAlarmTime = firstAlarmTimeSelectedOnPickerLiveData.value!!
    }

    override fun onCancelButtonClickInFirstAlarmDialog() {
        //close dialog without saving selected time
    }

    override fun onFirstAlarmTimeSelectedOnPicker(hour: Int, minute: Int) {
        firstAlarmTimeSelectedOnPickerLiveData.value = LocalTime.of(hour, minute)
        val temp = firstAlarmTimeSelectedOnPickerLiveData.value
    }
}