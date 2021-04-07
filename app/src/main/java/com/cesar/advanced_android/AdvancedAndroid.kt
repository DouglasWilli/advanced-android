package com.cesar.advanced_android

import android.app.Application
import com.cesar.advanced_android.di.androidModule
import org.koin.android.ext.android.startKoin
import org.koin.standalone.StandAloneContext.stopKoin

class AdvancedAndroid : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin(this, listOf(androidModule))
    }
    override fun onTerminate() {
        super.onTerminate()
        stopKoin()
    }
}