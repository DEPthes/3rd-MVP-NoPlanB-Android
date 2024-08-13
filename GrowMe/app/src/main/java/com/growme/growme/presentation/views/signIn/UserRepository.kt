package com.growme.growme.presentation.views.signIn

import com.growme.growme.domain.model.LoginResponseEntity

interface UserRepository {
    suspend fun login(accessToken: String, registrationToken: String): Result<LoginResponseEntity>
}