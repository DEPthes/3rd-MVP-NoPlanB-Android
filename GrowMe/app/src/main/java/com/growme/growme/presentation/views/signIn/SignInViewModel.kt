package com.growme.growme.presentation.views.signIn

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.growme.growme.data.LoggerUtils
import com.growme.growme.data.repository.AuthRepositoryImpl
import com.growme.growme.presentation.UiState
import com.growme.growme.presentation.base.GlobalApplication.Companion.app
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class SignInViewModel : ViewModel() {
    private val authRepositoryImpl = AuthRepositoryImpl()

    private val _loginState = MutableLiveData<UiState<Unit>>(UiState.Loading)
    val loginState: LiveData<UiState<Unit>> get() = _loginState

    fun login(accessToken: String, email: String) {
        _loginState.value = UiState.Loading

        viewModelScope.launch {
            try {
                authRepositoryImpl.login(accessToken, email).onSuccess {
                    runBlocking(Dispatchers.IO) {
                        app.userPreferences.setAccessToken(it.accessToken)
                        app.userPreferences.setRefreshToken(it.refreshToken)
                    }
                    _loginState.value = UiState.Success(Unit)
                }.onFailure {
                    _loginState.value = UiState.Failure(it.message)
                }
            } catch (e: Exception) {
                _loginState.value = UiState.Failure(e.message)
            }
        }
    }
}