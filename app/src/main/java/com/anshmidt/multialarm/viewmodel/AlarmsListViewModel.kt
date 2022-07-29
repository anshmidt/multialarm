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

    private var subscriptions = CompositeDisposable()

    fun onViewStarted() {
        repository.subscribeOnChangeListener()
        val disposable = repository.alarmsListObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ alarmsList ->
                       onAlarmsListChanged(alarmsList)
                }, Throwable::printStackTrace)
        subscriptions.add(disposable)
    }

    private fun onAlarmsListChanged(alarmsList: List<LocalTime>) {
        _alarms.value = alarmsList
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