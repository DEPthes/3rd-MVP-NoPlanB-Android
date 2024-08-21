package com.growme.growme.domain.repository

import com.growme.growme.domain.model.IsUserRegisteredInfo
import com.growme.growme.domain.model.MessageInfo

interface UserRepository {
    suspend fun getUserEmail(): Result<MessageInfo>

    suspend fun isUserRegistered(): Result<IsUserRegisteredInfo>

    suspend fun withdraw(): Result<MessageInfo>
}