package com.growme.growme.domain.repository

import com.growme.growme.domain.model.MessageInfo

interface UserRepository {
    suspend fun getUserEmail(): Result<MessageInfo>
}