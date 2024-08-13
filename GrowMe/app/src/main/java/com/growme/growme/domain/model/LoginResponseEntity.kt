package com.growme.growme.domain.model

data class LoginResponseEntity(
    val accessToken: String,
    val refreshToken: String
)