package com.growme.growme.domain.repository

import com.growme.growme.data.model.character.MyCharacterEquipItemDetailReq
import com.growme.growme.domain.model.MessageInfo

interface ItemChangeRepository {
    suspend fun changeItem(
        mycharacterEquipItemDetailReqList: List<MyCharacterEquipItemDetailReq>
    ): Result<MessageInfo>
}