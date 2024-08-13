package com.growme.growme.domain.repository

import com.growme.growme.domain.model.MessageInfo
import com.growme.growme.domain.model.character.MyCharacterDetailInfo
import com.growme.growme.domain.model.character.MyPageInfo

interface CharacterRepository {
    suspend fun getCharacterInfo(): Result<MyPageInfo>

    suspend fun changeNickname(newName: String):Result<MessageInfo>

    suspend fun getCharacterItem(): Result<MyCharacterDetailInfo>
}