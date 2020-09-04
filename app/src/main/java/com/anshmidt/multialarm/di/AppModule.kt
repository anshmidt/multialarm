package com.anshmidt.multialarm.di

import com.anshmidt.multialarm.repository.AlarmSettingsRepository
import com.anshmidt.multialarm.repository.IAlarmSettingsRepository
import com.anshmidt.multialarm.viewmodel.MainViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val appModule = module {

    single<IAlarmSettingsRepository> { AlarmSettingsRepository(androidContext()) }

    viewModel { MainViewModel(get()) }

}