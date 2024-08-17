package com.growme.growme.domain.repository

import com.growme.growme.domain.model.character.ItemchangeInfo

interface ItemChangeRepository {
    suspend fun changeItem(): Result<ItemchangeInfo>
}