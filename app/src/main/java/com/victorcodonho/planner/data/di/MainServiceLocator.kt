package com.victorcodonho.planner.data.di

import android.app.Application
import com.victorcodonho.planner.data.datasource.AuthenticationLocalDataSource
import com.victorcodonho.planner.data.datasource.AuthenticationLocalDataSourceImpl
import com.victorcodonho.planner.data.datasource.UserRegistrationLocalDataSource
import com.victorcodonho.planner.data.datasource.UserRegistrationLocalDataSourceImpl

object MainServiceLocator {

    private var _application: Application? = null
    private val application: Application get() = _application!!

    val userRegistrationLocalDataSource: UserRegistrationLocalDataSource by lazy {
        UserRegistrationLocalDataSourceImpl(applicationContext = application.applicationContext)
    }

    val authenticationLocalDataSource: AuthenticationLocalDataSource by lazy {
        AuthenticationLocalDataSourceImpl(applicationContext = application.applicationContext)
    }

    fun initalize(application: Application) {
        _application = application
    }

    fun clear() {
        _application = null
    }
}