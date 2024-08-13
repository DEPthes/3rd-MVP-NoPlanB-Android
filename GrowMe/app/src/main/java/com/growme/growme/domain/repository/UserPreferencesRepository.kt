package com.growme.growme.domain.repository

interface UserPreferencesRepository {
    suspend fun clearData(): Result<Boolean>

    suspend fun setAccessToken(accessToken: String)

    suspend fun getAccessToken(): Result<String>

    suspend fun setRefreshToken(refreshToken: String)

    suspend fun getRefreshToken(): Result<String>
}