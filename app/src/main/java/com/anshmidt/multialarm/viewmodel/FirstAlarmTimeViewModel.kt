package com.anshmidt.multialarm.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anshmidt.multialarm.alarmscheduler.AlarmScheduler
import com.anshmidt.multialarm.data.SingleLiveEvent
import com.anshmidt.multialarm.data.TimeFormatter
import com.anshmidt.multialarm.data.TimeLeft
import com.anshmidt.multialarm.repository.IScheduleSettingsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.threeten.bp.LocalTime
import java.util.concurrent.TimeUnit

class FirstAlarmTimeViewModel(
        private val scheduleSettingsRepository: IScheduleSettingsRepository,
        private val alarmScheduler: AlarmScheduler
) : ViewModel() {






//    companion object {
//        const val TIME_LEFT_REFRESH_INTERVAL_SECONDS = 10
//    }
//
//    var subscriptions = CompositeDisposable()
//
    private var _firstAlarmTime = MutableLiveData<LocalTime>()
    val firstAlarmTime: LiveData<LocalTime> = _firstAlarmTime

    private var _timeLeft = MutableLiveData<TimeLeft>()
    val timeLeft: LiveData<TimeLeft> = _timeLeft

//    var firstAlarmTimeDisplayable = Transformations.map(firstAlarmTime) { localTime ->
//        TimeFormatter.getDisplayableTime(localTime)
//    }
    private var firstAlarmTimeSelectedByUser = MutableLiveData<LocalTime>()
    private var timeLeftSelectedByUser = MutableLiveData<TimeLeft>()

    val firstAlarmTimeSelectedByUserFlow = MutableStateFlow<LocalTime>(LocalTime.of(1, 1))

//    private val firstAlarmTimeSelectedByUserFlow = Flow<LocalTime>()
//
//    lateinit var timeLeftBeforeFirstAlarm: LiveData<Duration>
//
//    private var currentTime = MutableLiveData<LocalTime>()
//
//    var timeLeftHours: LiveData<Int> = MutableLiveData<Int>()
//
//    var timeLeftMinutesPart: LiveData<Int> = MutableLiveData<Int>()
//
    private val _openFirstAlarmTimeDialog = SingleLiveEvent<Any>()
    val openFirstAlarmTimeDialog: LiveData<Any>
        get() = _openFirstAlarmTimeDialog
//
//
    fun onViewCreated() {
        viewModelScope.launch(Dispatchers.IO) {
            scheduleSettingsRepository.getFirstAlarmTime().collect { firstAlarmTime ->
                _firstAlarmTime.postValue(firstAlarmTime)
//                assignTimeLeft()
            }
        }



    }
//
    fun onViewResumed() {
        viewModelScope.launch(Dispatchers.IO) {
            val firstAlarmTimeFlow = merge(
                    scheduleSettingsRepository.getFirstAlarmTime(),
                    firstAlarmTimeSelectedByUserFlow
            )
            timeLeftFlow(
                    firstAlarmTimeFlow = firstAlarmTimeFlow,
                    tickerFlow = tickerEverySecondFlow()
            ).collect { timeLeft ->
                _timeLeft.postValue(timeLeft)
            }
        }
    }
//
//    private fun assignTimeLeft() {
//        val firstAlarmTimeFromRepository = firstAlarmTime
//        val firstAlarmTimeFromPicker = firstAlarmTimeChangedByUser
//
//        val firstAlarmTimeFromPickerAndRepository = LiveDataUtil.combineLiveDataFromDifferentSources(
//                firstAlarmTimeFromRepository,
//                firstAlarmTimeFromPicker
//        )
//
//        timeLeftBeforeFirstAlarm = Transformations.switchMap(currentTime) {
//            TimeFormatter.getTimeLeft(firstAlarmTimeFromPickerAndRepository, it)
//        }
//
//        timeLeftHours = TimeFormatter.getHours(timeLeftBeforeFirstAlarm)
//        timeLeftMinutesPart = TimeFormatter.getMinutesPart(timeLeftBeforeFirstAlarm)
//    }
//
//
    fun onFirstAlarmTimeClicked() {
        _openFirstAlarmTimeDialog.call()
    }
//
    fun onOkButtonClickInFirstAlarmDialog() {
//        _firstAlarmTime.value = firstAlarmTimeChangedByUser.value
//
//        viewModelScope.launch(Dispatchers.IO) {
//            scheduleSettingsRepository.getAlarmSettings().collect { alarmSettings ->
//                firstAlarmTimeChangedByUser.value?.let { newFirstAlarmTime ->
//                    val newAlarmSettings = alarmSettings.copy(firstAlarmTime = newFirstAlarmTime)
//                    alarmScheduler.reschedule(newAlarmSettings)
//                    scheduleSettingsRepository.saveFirstAlarmTime(newFirstAlarmTime)
//                }
//            }
//        }
//
    }
//
    fun onCancelButtonClickInFirstAlarmDialog() {
        //close dialog without saving selected value
    }
//
//
    fun onFirstAlarmTimeSelectedOnPicker(hour: Int, minute: Int) {
//        firstAlarmTimeSelectedByUser.value = LocalTime.of(hour, minute)

//        _timeLeft.value = TimeFormatter.getTimeLeft(
//                alarmTime = LocalTime.of(hour, minute),
//                currentTime = LocalTime.now()
//        )
        viewModelScope.launch(Dispatchers.Default) {
            firstAlarmTimeSelectedByUserFlow.emit(LocalTime.of(hour, minute))
        }
    }
//
//    private fun startToObserveCurrentTime(intervalSeconds: Int) {
//        val disposable = Observable.interval(0, intervalSeconds.toLong(), TimeUnit.SECONDS)
//                .subscribe({
//                    currentTime.postValue(LocalTime.now())
//                }, Throwable::printStackTrace)
//        subscriptions.add(disposable)
//    }
//
//
    fun onViewPaused() {

//        if (!subscriptions.isDisposed) {
//            subscriptions.clear()
//        }
    }
//
    fun onViewDestroyed() {
//        if (!subscriptions.isDisposed) {
//            subscriptions.dispose()
//        }
    }

    private fun tickerEverySecondFlow() = flow {
        val initialDelay = 0L
        val period = TimeUnit.SECONDS.toMillis(1)
        delay(initialDelay)
        while (true) {
            emit(Unit)
            delay(period)
        }
    }

    private fun timeLeftFlow(firstAlarmTimeFlow: Flow<LocalTime>, tickerFlow: Flow<Unit>): Flow<TimeLeft> {
        return firstAlarmTimeFlow.combine(tickerFlow) { firstAlarmTime, _ ->
            firstAlarmTime
        }.map { firstAlarmTime ->
            TimeFormatter.getTimeLeft(
                    alarmTime = firstAlarmTime,
                    currentTime = LocalTime.now()
            )
        }
    }





}