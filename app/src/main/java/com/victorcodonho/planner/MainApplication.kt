package com.victorcodonho.planner

import android.app.Application
import com.victorcodonho.planner.data.di.MainServiceLocator

class MainApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        MainServiceLocator.initalize(application = this)
    }
}