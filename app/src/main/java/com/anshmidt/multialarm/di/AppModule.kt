package com.anshmidt.multialarm.di

import com.anshmidt.multialarm.DefaultCountDownTimer
import com.anshmidt.multialarm.ICountDownTimer
import com.anshmidt.multialarm.IMusicPlayer
import com.anshmidt.multialarm.MusicPlayer
import com.anshmidt.multialarm.repository.AlarmSettingsRepository
import com.anshmidt.multialarm.repository.IAlarmSettingsRepository
import com.anshmidt.multialarm.viewmodel.*
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val appModule = module {
    single<IAlarmSettingsRepository> { AlarmSettingsRepository(androidContext()) }
    single<IMusicPlayer> { MusicPlayer(androidContext()) }
    factory<ICountDownTimer> { DefaultCountDownTimer() }
    viewModel { MainViewModel(get()) }
    viewModel { MinutesBetweenAlarmsViewModel(get()) }
    viewModel { FirstAlarmTimeViewModel(get()) }
    viewModel { NumberOfAlarmsViewModel(get()) }
    viewModel { DismissAlarmViewModel(get(), get(), get()) }
}