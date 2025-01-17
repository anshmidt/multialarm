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
import com.anshmidt.multialarm.repository.AppSettingRepository
import com.anshmidt.multialarm.repository.IAppSettingRepository
import com.anshmidt.multialarm.repository.IRingtoneSettingRepository
import com.anshmidt.multialarm.repository.IScheduleSettingsRepository
import com.anshmidt.multialarm.repository.LogRepository
import com.anshmidt.multialarm.repository.RingtoneSettingRepository
import com.anshmidt.multialarm.repository.ScheduleSettingsRepository
import com.anshmidt.multialarm.view.helpers.AppThemeSelector
import com.anshmidt.multialarm.viewmodel.AlarmsListViewModel
import com.anshmidt.multialarm.viewmodel.DismissAlarmViewModel
import com.anshmidt.multialarm.viewmodel.FirstAlarmTimeViewModel
import com.anshmidt.multialarm.viewmodel.LogViewModel
import com.anshmidt.multialarm.viewmodel.MainViewModel
import com.anshmidt.multialarm.viewmodel.MinutesBetweenAlarmsViewModel
import com.anshmidt.multialarm.viewmodel.NumberOfAlarmsViewModel
import com.anshmidt.multialarm.viewmodel.SettingsViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single<DpsContext> { DpsContext(androidContext()) }
    single<IScheduleSettingsRepository> { ScheduleSettingsRepository(get()) }
    single<IRingtoneSettingRepository> { RingtoneSettingRepository(get(), get()) }
    single<IAppSettingRepository> { AppSettingRepository(get()) }
    single<LogRepository> { LogRepository(get()) }
    single<IMusicPlayer> { MusicPlayer(get()) }
    single<AppThemeSelector> { AppThemeSelector(get()) }
    single<FileStorage> { FileStorage(get()) }
    single<DataStoreStorage> { DataStoreStorage(get()) }
    single<SharedPreferencesStorage> { SharedPreferencesStorage(get()) }
    factory<ICountDownTimer> { DefaultCountDownTimer() }
    factory<AlarmScheduler> { AlarmScheduler(get()) }
    factory<NotificationHelper> { NotificationHelper(get()) }
    viewModel { MainViewModel(get(), get(), get()) }
    viewModel { MinutesBetweenAlarmsViewModel(get(), get()) }
    viewModel { FirstAlarmTimeViewModel(get(), get()) }
    viewModel { NumberOfAlarmsViewModel(get(), get()) }
    viewModel { DismissAlarmViewModel() }
    viewModel { AlarmsListViewModel(get()) }
    viewModel { SettingsViewModel(get(), get(), get()) }
    viewModel { LogViewModel(get()) }
}