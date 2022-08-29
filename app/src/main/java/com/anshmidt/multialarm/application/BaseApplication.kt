package com.anshmidt.multialarm.application

import android.app.Application
import com.anshmidt.multialarm.di.appModule
import com.anshmidt.multialarm.view.helpers.AppThemeSelector
import com.jakewharton.threetenabp.AndroidThreeTen
import org.koin.android.ext.android.startKoin
import org.koin.core.Koin
import org.koin.log.EmptyLogger
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject

class BaseApplication : Application(), KoinComponent {

    private val appThemeSelector: AppThemeSelector by inject()

    override fun onCreate() {
        super.onCreate()
        startKoin(this@BaseApplication, listOf(appModule))
        Koin.logger = EmptyLogger()
        AndroidThreeTen.init(this)
        appThemeSelector.checkAndShowTheme()
    }
}