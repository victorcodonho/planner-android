package com.victorcodonho.planner.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.victorcodonho.planner.data.datasource.AuthenticationLocalDataSource
import com.victorcodonho.planner.data.datasource.UserRegistrationLocalDataSource
import com.victorcodonho.planner.data.di.MainServiceLocator
import com.victorcodonho.planner.data.model.Profile
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private val mockToken = """
    eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9
    .eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydWUsImlhdCI6MTUxNjIzOTAyMn0
    .KMUFsIDTnFmyG3nMiGM6H9FNFUROf3wh7SmqJp-QV30
""".trimIndent()

class UserRegistrationViewModel: ViewModel() {

    private val userRegistrationLocalDataSource: UserRegistrationLocalDataSource by lazy {
        MainServiceLocator.userRegistrationLocalDataSource
    }

    private val authenticationLocalDataSource: AuthenticationLocalDataSource by lazy {
        MainServiceLocator.authenticationLocalDataSource
    }

    private val _isProfileValid: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isProfileValid: StateFlow<Boolean> = _isProfileValid.asStateFlow()

    private val _profile: MutableStateFlow<Profile> = MutableStateFlow(Profile())
    val profile: StateFlow<Profile> = _profile.asStateFlow()

    private val _isTokenValid: MutableStateFlow<Boolean?> = MutableStateFlow(null)
    val isTokenValid: StateFlow<Boolean?> = _isTokenValid.asStateFlow()

    init {
        viewModelScope.launch {
            launch {
                userRegistrationLocalDataSource.profile.collect { profile ->
                    _profile.value = profile
                }
            }

            launch {
                while (true) {
                    val tokenExpirationDatetime = authenticationLocalDataSource.expirationDateTime.firstOrNull()

                    tokenExpirationDatetime?.let { tokenExpirationDatetime ->
                        val datetimeNow = System.currentTimeMillis()
                        _isTokenValid.value = tokenExpirationDatetime >= datetimeNow
                    }

                    delay(5_000)
                }
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

    fun obtainNewToken() {
        viewModelScope.launch {
            authenticationLocalDataSource.insertToken(token = mockToken)
        }
    }
}