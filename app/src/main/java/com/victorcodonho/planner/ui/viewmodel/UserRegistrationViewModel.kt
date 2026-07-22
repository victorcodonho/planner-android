package com.victorcodonho.planner.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.victorcodonho.planner.data.datasource.UserRegistrationLocalDataSource
import com.victorcodonho.planner.data.di.MainServiceLocator

class UserRegistrationViewModel: ViewModel() {

    private val userRegistrationLocalDataSource: UserRegistrationLocalDataSource by lazy {
        MainServiceLocator.userRegistrationLocalDataSource
    }

    fun getIsUserRegistered(): Boolean {
        return userRegistrationLocalDataSource.getIsUserRegistered()
    }

    fun saveIsUserRegistered(isUserRegistered: Boolean) {
        userRegistrationLocalDataSource.saveIsUserRegistered(isUserRegistered = isUserRegistered)
    }
}