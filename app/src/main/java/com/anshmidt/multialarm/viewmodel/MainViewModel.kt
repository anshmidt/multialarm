package com.anshmidt.multialarm.viewmodel

import androidx.lifecycle.*
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
    private var firstAlarmTimeSelectedOnPickerLiveData = MutableLiveData<LocalTime>()


    lateinit var timeLeftBeforeFirstAlarm: LiveData<Duration>

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
        assignTimeLeft()
    }

    private fun assignTimeLeft() {
        val firstAlarmTimeFromRepository = firstAlarmTime
        val firstAlarmTimeFromPicker = firstAlarmTimeSelectedOnPickerLiveData

        val firstAlarmTimeFromPickerAndRepository = MediatorLiveData<LocalTime>()
        firstAlarmTimeFromPickerAndRepository.addSource(firstAlarmTimeFromRepository, {
            firstAlarmTimeFromPickerAndRepository.value = it
        })
        firstAlarmTimeFromPickerAndRepository.addSource(firstAlarmTimeFromPicker, {
            firstAlarmTimeFromPickerAndRepository.value = it
        })

        timeLeftBeforeFirstAlarm = Transformations.map(firstAlarmTimeFromPickerAndRepository) {
            TimeFormatter.getTimeLeft(it)
        }
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
    }
}