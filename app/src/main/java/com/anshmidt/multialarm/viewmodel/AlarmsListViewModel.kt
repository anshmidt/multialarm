package com.anshmidt.multialarm.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.anshmidt.multialarm.repository.ISettingsRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.threeten.bp.LocalTime

class AlarmsListViewModel(
        private val repository: ISettingsRepository
) : ViewModel() {
    private var _alarms = MutableLiveData<List<LocalTime>>()
    val alarms: LiveData<List<LocalTime>> = _alarms

    private var _alarmTurnedOn = MutableLiveData<Boolean>()
    val alarmTurnedOn: LiveData<Boolean> = _alarmTurnedOn

    private var subscriptions = CompositeDisposable()

    fun onViewStarted() {
        repository.subscribeOnChangeListener()

        repository.alarmsListObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ alarmsList ->
                       onAlarmsListChanged(alarmsList)
                }, Throwable::printStackTrace)
                .let { subscriptions.add(it) }

        repository.alarmTurnedOnObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ alarmTurnedOn ->
                    onAlarmTurnedOn(alarmTurnedOn)
                }, Throwable::printStackTrace)
                .let { subscriptions.add(it) }
    }

    private fun onAlarmsListChanged(alarmsList: List<LocalTime>) {
        _alarms.value = alarmsList
    }

    private fun onAlarmTurnedOn(alarmTurnedOn: Boolean) {
        _alarmTurnedOn.value = alarmTurnedOn
    }

    fun onViewStopped() {
        repository.unsubscribeOnChangeListener()
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