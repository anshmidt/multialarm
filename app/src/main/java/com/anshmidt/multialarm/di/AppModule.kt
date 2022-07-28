package com.anshmidt.multialarm.di

import com.anshmidt.multialarm.alarmscheduler.AlarmScheduler
import com.anshmidt.multialarm.countdowntimer.DefaultCountDownTimer
import com.anshmidt.multialarm.countdowntimer.ICountDownTimer
import com.anshmidt.multialarm.musicplayer.IMusicPlayer
import com.anshmidt.multialarm.musicplayer.MusicPlayer
import com.anshmidt.multialarm.notifications.dismissalarm.NotificationHelper
import com.anshmidt.multialarm.notifications.dismissalarm.NotificationParams
import com.anshmidt.multialarm.repository.SettingsRepository
import com.anshmidt.multialarm.repository.ISettingsRepository
import com.anshmidt.multialarm.viewmodel.*
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val appModule = module {
    single<ISettingsRepository> { SettingsRepository(androidContext()) }
    single<IMusicPlayer> { MusicPlayer(androidContext()) }
    factory<ICountDownTimer> { DefaultCountDownTimer() }
    factory<AlarmScheduler> { AlarmScheduler(androidContext()) }
    factory { NotificationParams() }
    factory<NotificationHelper> { NotificationHelper(androidContext(), get()) }
    viewModel { MainViewModel(get(), get()) }
    viewModel { MinutesBetweenAlarmsViewModel(get(), get()) }
    viewModel { FirstAlarmTimeViewModel(get(), get()) }
    viewModel { NumberOfAlarmsViewModel(get(), get()) }
    viewModel { DismissAlarmViewModel() }
    viewModel { AlarmsListViewModel() }
}