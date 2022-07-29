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

    fun onViewCreated() {

    }

    fun onViewStarted() {
        repository.subscribeOnChangeListener()
        val disposable = repository.firstAlarmTimeObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ firstAlarmTime ->
                    onFirstAlarmTimeChanged(firstAlarmTime)
                }, Throwable::printStackTrace)
        subscriptions.add(disposable)
    }

    private fun onFirstAlarmTimeChanged(firstAlarmTime: LocalTime) {
        _alarms.value = listOf(firstAlarmTime)
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