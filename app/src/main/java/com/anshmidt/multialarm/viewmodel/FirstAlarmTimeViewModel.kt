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

    private var _firstAlarmTime = MutableLiveData<LocalTime>()
    val firstAlarmTime: LiveData<LocalTime> = _firstAlarmTime

    private var _timeLeft = MutableLiveData<TimeLeft>()
    val timeLeft: LiveData<TimeLeft> = _timeLeft

    private val firstAlarmTimeSelectedByUserFlow = MutableStateFlow<LocalTime?>(null)

    private val _openFirstAlarmTimeDialog = SingleLiveEvent<Any>()
    val openFirstAlarmTimeDialog: LiveData<Any>
        get() = _openFirstAlarmTimeDialog

    fun onViewCreated() {

    }

    fun onViewResumed() {
        viewModelScope.launch(Dispatchers.IO) {
            scheduleSettingsRepository.getFirstAlarmTime().collect { firstAlarmTime ->
                _firstAlarmTime.postValue(firstAlarmTime)
            }
        }

        viewModelScope.launch(Dispatchers.IO) {
            val firstAlarmTimeFlow = merge(
                    scheduleSettingsRepository.getFirstAlarmTime(),
                    firstAlarmTimeSelectedByUserFlow.filterNotNull()
            )
            timeLeftFlow(
                    firstAlarmTimeFlow = firstAlarmTimeFlow,
                    tickerFlow = tickerEverySecondFlow()
            ).collect { timeLeft ->
                _timeLeft.postValue(timeLeft)
            }
        }
    }

    fun onFirstAlarmTimeClicked() {
        _openFirstAlarmTimeDialog.call()
    }

    fun onOkButtonClickInFirstAlarmDialog() {
        val firstAlarmTimeSelectedByUser = firstAlarmTimeSelectedByUserFlow.value

        if (firstAlarmTimeSelectedByUser == null) return

        viewModelScope.launch(Dispatchers.IO) {
            scheduleSettingsRepository.getAlarmSettings().collect { alarmSettings ->
                firstAlarmTimeSelectedByUser.let { newFirstAlarmTime ->
                    val newAlarmSettings = alarmSettings.copy(firstAlarmTime = newFirstAlarmTime)
                    alarmScheduler.reschedule(newAlarmSettings)
                    scheduleSettingsRepository.saveFirstAlarmTime(newFirstAlarmTime)
                }
            }
        }

    }

    fun onCancelButtonClickInFirstAlarmDialog() {
        //close dialog without saving selected value
        //TODO implement
    }

    fun onFirstAlarmTimeSelectedOnPicker(hour: Int, minute: Int) {
        viewModelScope.launch(Dispatchers.Default) {
            firstAlarmTimeSelectedByUserFlow.emit(LocalTime.of(hour, minute))
        }
    }

    fun onViewPaused() {

    }

    fun onViewDestroyed() {

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