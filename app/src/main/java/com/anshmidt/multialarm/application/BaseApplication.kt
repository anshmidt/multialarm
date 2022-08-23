package com.anshmidt.multialarm.application

import android.app.Application
import com.anshmidt.multialarm.di.appModule
import com.jakewharton.threetenabp.AndroidThreeTen
import org.koin.android.ext.android.startKoin
import org.koin.core.Koin
import org.koin.log.EmptyLogger

class BaseApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin(this@BaseApplication, listOf(appModule))
        Koin.logger = EmptyLogger()
        AndroidThreeTen.init(this)
    }
}