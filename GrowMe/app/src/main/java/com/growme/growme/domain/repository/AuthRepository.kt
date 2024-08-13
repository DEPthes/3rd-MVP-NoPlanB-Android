package com.growme.growme.domain.repository

import com.growme.growme.domain.model.LoginResponseEntity

interface AuthRepository {
    suspend fun login(
        accessToken: String,
        email: String
    ): Result<LoginResponseEntity>
}