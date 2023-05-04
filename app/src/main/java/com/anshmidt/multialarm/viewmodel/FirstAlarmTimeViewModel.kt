package com.anshmidt.multialarm.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anshmidt.multialarm.alarmscheduler.AlarmScheduler
import com.anshmidt.multialarm.data.SingleLiveEvent
import com.anshmidt.multialarm.data.TimeFormatter
import com.anshmidt.multialarm.data.TimeLeft
import com.anshmidt.multialarm.logging.Log
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

    private var _shouldShowTimeLeftOnMainScreen = MutableLiveData<Boolean>()
    val shouldShowTimeLeftOnMainScreen: LiveData<Boolean> = _shouldShowTimeLeftOnMainScreen

    private var _isTimeLeftEnabled = MutableLiveData<Boolean>()
    val isTimeLeftEnabled: LiveData<Boolean> = _isTimeLeftEnabled

    private val firstAlarmTimeSelectedByUserFlow = MutableStateFlow<LocalTime?>(null)

    private val _openFirstAlarmTimeDialog = SingleLiveEvent<Any>()
    val openFirstAlarmTimeDialog: LiveData<Any>
        get() = _openFirstAlarmTimeDialog

    fun onViewResumed() {
        viewModelScope.launch(Dispatchers.IO) {
            scheduleSettingsRepository.getFirstAlarmTime().first { firstAlarmTime ->
                _firstAlarmTime.postValue(firstAlarmTime)
                return@first true
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

        /**
         * Time left to the first alarm doesn't make sense if it's already off. That's why we hide
         * time left in this case.
         */
        viewModelScope.launch(Dispatchers.IO) {
            scheduleSettingsRepository.getNumberOfAlreadyRangAlarms().combine(
                scheduleSettingsRepository.getAlarmSwitchState()
            ) { numberOfAlreadyRangAlarms, switchState ->
                return@combine shouldShowTimeLeftOnMainScreen(numberOfAlreadyRangAlarms, switchState)
            }.collect { shouldShowTimeLeftOnMainScreen ->
                _shouldShowTimeLeftOnMainScreen.postValue(shouldShowTimeLeftOnMainScreen)
            }
        }

        viewModelScope.launch(Dispatchers.IO) {
            scheduleSettingsRepository.getAlarmSwitchState().collect { switchState ->
                _isTimeLeftEnabled.postValue(switchState)
            }
        }
    }

    private fun shouldShowTimeLeftOnMainScreen(numberOfAlreadyRangAlarms: Int, switchState: Boolean): Boolean {
        return if ((numberOfAlreadyRangAlarms > 0) && (switchState == true)) {
            false
        } else {
            true
        }
    }

    fun onFirstAlarmTimeClicked() {
        _openFirstAlarmTimeDialog.call()
    }

    fun onOkButtonClickInFirstAlarmDialog() {
        val firstAlarmTimeSelectedByUser = firstAlarmTimeSelectedByUserFlow.value

        firstAlarmTimeSelectedByUser?.let {
            _firstAlarmTime.postValue(it)

            viewModelScope.launch(Dispatchers.IO) {
                scheduleSettingsRepository.getAlarmSettings().first { alarmSettings ->
                    val newAlarmSettings = alarmSettings.copy(firstAlarmTime = firstAlarmTimeSelectedByUser)
                    Log.d(TAG, "Rescheduling alarm because first alarm time changed by user")
                    alarmScheduler.reschedule(newAlarmSettings)
                    scheduleSettingsRepository.saveFirstAlarmTime(firstAlarmTimeSelectedByUser)

                    return@first true
                }
            }
        }

    }

    fun onCancelButtonClickInFirstAlarmDialog() {
        // Clicking Cancel effectively means that we ignore any value user set, and use a value saved
        // in repository instead.
        viewModelScope.launch(Dispatchers.IO) {
            scheduleSettingsRepository.getFirstAlarmTime().first { firstAlarmTimeFromRepository ->
                firstAlarmTimeSelectedByUserFlow.emit(firstAlarmTimeFromRepository)
                return@first true
            }
        }
    }

    fun onFirstAlarmTimeSelectedOnPicker(hour: Int, minute: Int) {
        viewModelScope.launch(Dispatchers.Default) {
            firstAlarmTimeSelectedByUserFlow.emit(LocalTime.of(hour, minute))
        }
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
            TimeFormatter.getDisplayableTimeLeft(
                    alarmTime = firstAlarmTime,
                    currentTime = LocalTime.now()
            )
        }
    }

    companion object {
        private val TAG = FirstAlarmTimeViewModel::class.java.simpleName
    }

}