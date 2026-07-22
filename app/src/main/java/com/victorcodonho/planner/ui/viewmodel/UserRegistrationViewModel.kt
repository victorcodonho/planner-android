package com.victorcodonho.planner.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.victorcodonho.planner.data.datasource.UserRegistrationLocalDataSource
import com.victorcodonho.planner.data.di.MainServiceLocator
import com.victorcodonho.planner.data.model.Profile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class UserRegistrationViewModel: ViewModel() {

    private val userRegistrationLocalDataSource: UserRegistrationLocalDataSource by lazy {
        MainServiceLocator.userRegistrationLocalDataSource
    }

    private val _isProfileValid: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isProfileValid: StateFlow<Boolean> = _isProfileValid.asStateFlow()

    private val _profile: MutableStateFlow<Profile> = MutableStateFlow(Profile())
    val profile: StateFlow<Profile> = _profile.asStateFlow()

    init {
        viewModelScope.launch {
            userRegistrationLocalDataSource.profile.collect { profile ->
                _profile.value = profile
            }
        }
    }

    fun getIsUserRegistered(): Boolean {
        return userRegistrationLocalDataSource.getIsUserRegistered()
    }

    fun updateProfile(
        name: String? = null,
        email: String? = null,
        phone: String? = null,
        image: String? = null
    ) {
        if (name == null && email == null && phone == null && image == null) return

        _profile.update { currentProfile ->
            val updateProfile = currentProfile.copy(
                name = name ?: currentProfile.name,
                email = email ?: currentProfile.email,
                phone = phone ?: currentProfile.phone,
                image = image ?: currentProfile.image
            )

            _isProfileValid.update { updateProfile.isValid() }

            updateProfile
        }

        Log.d("UserRegistrationViewModel", "updateProfile: ${_profile.value}")
    }

    fun saveProfile() {
        viewModelScope.launch {
            userRegistrationLocalDataSource.saveProfile(profile = profile.value)
            userRegistrationLocalDataSource.saveIsUserRegistered(isUserRegistered = true)
        }
    }
}