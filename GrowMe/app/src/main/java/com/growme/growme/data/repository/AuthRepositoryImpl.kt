package com.growme.growme.data.repository

import com.growme.growme.data.LoggerUtils
import com.growme.growme.data.RetrofitClient
import com.growme.growme.data.model.auth.GetAccessTokenRequestDTO
import com.growme.growme.data.service.AuthService
import com.growme.growme.domain.model.LoginResponseEntity
import com.growme.growme.domain.repository.AuthRepository
import org.json.JSONObject

class AuthRepositoryImpl : AuthRepository {
    private val service = RetrofitClient.getInstance().create(AuthService::class.java)
    override suspend fun login(
        accessToken: String,
        email: String
    ): Result<LoginResponseEntity> {
        val res = service.login(GetAccessTokenRequestDTO(accessToken, email))

        return if (res.isSuccessful) {
            val data = res.body()!!
            Result.success(
                LoginResponseEntity(
                    accessToken = data.information.accessToken,
                    refreshToken = data.information.refreshToken
                )
            )
        } else {
            val errorMsg = JSONObject(res.errorBody()!!.string()).getString("msg")
            Result.failure(Exception(errorMsg))
        }
    }
}