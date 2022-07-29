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

    var subscriptions = CompositeDisposable()

    fun onViewCreated() {
        repository.subscribeOnChangeListener()

        val disposable = repository.firstAlarmTimeObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ firstAlarmTime ->
                    _alarms.value = listOf(firstAlarmTime)
                }, Throwable::printStackTrace)
        subscriptions.add(disposable)
//        _alarms.value = listOf(repository.firstAlarmTime)
//        _alarms.value = listOf(
//                LocalTime.of(1, 9),
//                LocalTime.of(2, 11),
//                LocalTime.of(3, 11),
//                LocalTime.of(4, 11),
//                LocalTime.of(5, 11),
//                LocalTime.of(6, 11)
//        )
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