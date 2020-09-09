package com.anshmidt.multialarm.viewmodel

import androidx.lifecycle.*
import com.anshmidt.multialarm.SingleLiveEvent
import com.anshmidt.multialarm.data.LiveDataUtil
import com.anshmidt.multialarm.data.TimeFormatter
import com.anshmidt.multialarm.repository.IAlarmSettingsRepository
import org.threeten.bp.Duration
import org.threeten.bp.LocalTime
import java.util.concurrent.TimeUnit
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable

open class MainViewModel(
    private val repository: IAlarmSettingsRepository
) : ViewModel() {


    companion object {
        private val TAG = MainViewModel::class.java.simpleName
        val TIME_LEFT_REFRESH_INTERVAL_SECONDS = 10
    }

    var subscriptions = CompositeDisposable()

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
    private var firstAlarmTimeChangedByUser = MutableLiveData<LocalTime>()

    lateinit var timeLeftBeforeFirstAlarm: LiveData<Duration>

    private var currentTime = MutableLiveData<LocalTime>()

    lateinit var timeLeftHours: LiveData<Int>
    lateinit var timeLeftMinutesPart: LiveData<Int>

    private val _openFirstAlarmTimeDialog = SingleLiveEvent<Any>()
    val openFirstAlarmTimeDialog: LiveData<Any>
        get() = _openFirstAlarmTimeDialog


    var numberOfAlarms: Int
        get() = repository.numberOfAlarms
        set(value) {
            repository.numberOfAlarms = value
        }



    fun onViewCreated() {
        _firstAlarmTime.value = repository.firstAlarmTime
        assignTimeLeft()
    }

    fun onViewResumed() {
        startToObserveCurrentTime(TIME_LEFT_REFRESH_INTERVAL_SECONDS)
    }

    private fun assignTimeLeft() {
        val firstAlarmTimeFromRepository = firstAlarmTime
        val firstAlarmTimeFromPicker = firstAlarmTimeChangedByUser

        val firstAlarmTimeFromPickerAndRepository = LiveDataUtil.combineLiveDataFromDifferentSources(
                firstAlarmTimeFromRepository,
                firstAlarmTimeFromPicker
        )

        timeLeftBeforeFirstAlarm = Transformations.switchMap(currentTime) {
            TimeFormatter.getTimeLeft(firstAlarmTimeFromPickerAndRepository, it)
        }

        timeLeftHours = TimeFormatter.getHours(timeLeftBeforeFirstAlarm)
        timeLeftMinutesPart = TimeFormatter.getMinutesPart(timeLeftBeforeFirstAlarm)
    }


    fun onFirstAlarmTimeClicked() {
        _openFirstAlarmTimeDialog.call()
    }


    fun onNumberOfAlarmsClicked() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun onOkButtonClickInFirstAlarmDialog() {
        _firstAlarmTime.value = firstAlarmTimeChangedByUser.value
        repository.firstAlarmTime = firstAlarmTimeChangedByUser.value!!
    }

    fun onCancelButtonClickInFirstAlarmDialog() {
        //close dialog without saving selected value
    }


    fun onFirstAlarmTimeSelectedOnPicker(hour: Int, minute: Int) {
        firstAlarmTimeChangedByUser.value = LocalTime.of(hour, minute)
    }

    private fun startToObserveCurrentTime(intervalSeconds: Int) {
        val disposable = Observable.interval(0, intervalSeconds.toLong(), TimeUnit.SECONDS)
                .subscribe({
                    currentTime.postValue(LocalTime.now())
                }, Throwable::printStackTrace)
        subscriptions.add(disposable)
    }
    

    fun onViewPaused() {
        if (!subscriptions.isDisposed) {
            subscriptions.clear()
        }
    }

    fun onViewDestroyed() {
        if (!subscriptions.isDisposed) {
            subscriptions.dispose()
        }
    }

}