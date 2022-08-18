package com.anshmidt.multialarm.application

import android.app.Application
import com.anshmidt.multialarm.di.appModule
import com.jakewharton.threetenabp.AndroidThreeTen
import org.koin.android.ext.android.startKoin

class BaseApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin(this@BaseApplication, listOf(appModule))
        AndroidThreeTen.init(this)
    }
}