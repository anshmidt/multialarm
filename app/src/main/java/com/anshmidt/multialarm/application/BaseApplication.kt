package com.anshmidt.multialarm.application

import android.app.Application
import com.anshmidt.multialarm.di.DpsContext
import com.anshmidt.multialarm.di.appModule
import com.anshmidt.multialarm.logging.Log
import com.anshmidt.multialarm.view.helpers.AppThemeSelector
import com.jakewharton.threetenabp.AndroidThreeTen
import org.koin.android.ext.koin.androidContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.startKoin
import org.koin.core.logger.EmptyLogger

class BaseApplication : Application(), KoinComponent {

    private val appThemeSelector: AppThemeSelector by inject()

    override fun onCreate() {
        super.onCreate()
        startKoin {
            logger(EmptyLogger())
            androidContext(this@BaseApplication)
            modules(listOf(appModule))
        }

        AndroidThreeTen.init(this)
        appThemeSelector.checkAndShowTheme()
        Log.initializeLogging(DpsContext(applicationContext))
    }


}