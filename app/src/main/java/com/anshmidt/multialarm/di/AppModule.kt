package com.anshmidt.multialarm.di

import com.anshmidt.multialarm.alarmscheduler.AlarmScheduler
import com.anshmidt.multialarm.countdowntimer.DefaultCountDownTimer
import com.anshmidt.multialarm.countdowntimer.ICountDownTimer
import com.anshmidt.multialarm.datasources.DataStoreStorage
import com.anshmidt.multialarm.datasources.FileStorage
import com.anshmidt.multialarm.datasources.SharedPreferencesStorage
import com.anshmidt.multialarm.musicplayer.IMusicPlayer
import com.anshmidt.multialarm.musicplayer.MusicPlayer
import com.anshmidt.multialarm.notifications.dismissalarm.NotificationHelper
import com.anshmidt.multialarm.repository.*
import com.anshmidt.multialarm.view.helpers.AppThemeSelector
import com.anshmidt.multialarm.viewmodel.*
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val appModule = module {
    single<IScheduleSettingsRepository> { ScheduleSettingsRepository(get()) }
    single<IRingtoneSettingRepository> { RingtoneSettingRepository(get(), get()) }
    single<IAppSettingRepository> { AppSettingRepository(get()) }
    single<IMusicPlayer> { MusicPlayer(androidContext()) }
    single<AppThemeSelector> { AppThemeSelector(get()) }
    single<SharedPreferencesStorage> { SharedPreferencesStorage(androidContext()) }
    single<FileStorage> { FileStorage(androidContext()) }
    single<DataStoreStorage> { DataStoreStorage(androidContext()) }
    factory<ICountDownTimer> { DefaultCountDownTimer() }
    factory<AlarmScheduler> { AlarmScheduler(androidContext()) }
    factory<NotificationHelper> { NotificationHelper(androidContext()) }
    viewModel { MainViewModel(get(), get(), get()) }
    viewModel { MinutesBetweenAlarmsViewModel(get(), get()) }
    viewModel { FirstAlarmTimeViewModel(get(), get()) }
    viewModel { NumberOfAlarmsViewModel(get(), get()) }
    viewModel { DismissAlarmViewModel() }
    viewModel { AlarmsListViewModel(get()) }
    viewModel { SettingsViewModel(get(), get(), get()) }
}