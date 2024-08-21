package com.growme.growme.presentation.views.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.growme.growme.presentation.base.UiState
import com.kakao.sdk.auth.AuthApiClient
import com.kakao.sdk.user.UserApiClient

class SplashViewModel : ViewModel() {
    private val _loginState = MutableLiveData<UiState<Unit>>(UiState.Loading)
    val loginState: LiveData<UiState<Unit>> get() = _loginState

    private fun validationKakao() {
        if (AuthApiClient.instance.hasToken()) {
            UserApiClient.instance.accessTokenInfo { _, error ->
                if (error != null) {
                    _loginState.value = UiState.Failure(error.message)
                } else {
                    _loginState.value = UiState.Success(Unit)
                }
            }
        } else {
            _loginState.value = UiState.Failure("카카오 로그인 필요")
        }
    }
}