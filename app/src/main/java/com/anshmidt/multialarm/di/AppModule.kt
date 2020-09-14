package com.anshmidt.multialarm.di

import com.anshmidt.multialarm.repository.AlarmSettingsRepository
import com.anshmidt.multialarm.repository.IAlarmSettingsRepository
import com.anshmidt.multialarm.viewmodel.*
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val appModule = module {
    single<IAlarmSettingsRepository> { AlarmSettingsRepository(androidContext()) }
    viewModel { MainViewModel(get()) }
    viewModel { MinutesBetweenAlarmsViewModel(get()) }
    viewModel { FirstAlarmTimeViewModel(get()) }
    viewModel { NumberOfAlarmsViewModel(get()) }
    viewModel { DismissAlarmViewModel(get()) }
}