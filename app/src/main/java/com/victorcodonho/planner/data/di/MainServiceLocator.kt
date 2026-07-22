package com.victorcodonho.planner.data.di

import android.app.Application
import com.victorcodonho.planner.data.datasource.UserRegistrationLocalDataSource
import com.victorcodonho.planner.data.datasource.UserRegistrationLocalDataSourceImpl

object MainServiceLocator {

    private lateinit var application: Application

    val userRegistrationLocalDataSource: UserRegistrationLocalDataSource by lazy {
        UserRegistrationLocalDataSourceImpl(applicationContext = application.applicationContext)
    }

    fun initalize(application: Application) {
        this.application = application
    }
}