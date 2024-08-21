package com.growme.growme.data.repository

import com.growme.growme.data.RetrofitClient
import com.growme.growme.data.model.character.ItemChangeResponseDTO
import com.growme.growme.data.model.character.MyCharacterEquipItemDetailReq
import com.growme.growme.data.service.ItemChangeService
import com.growme.growme.domain.model.MessageInfo
import com.growme.growme.domain.repository.ItemChangeRepository

class ItemChangeRepositoryImpl : ItemChangeRepository {
    private val service = RetrofitClient.getInstance().create(ItemChangeService::class.java)
    private val dataStoreRepositoryImpl = DataStoreRepositoryImpl()
    override suspend fun changeItem(
        mycharacterEquipItemDetailReqList: List<MyCharacterEquipItemDetailReq>,
    ): Result<MessageInfo> {
        val accessToken = dataStoreRepositoryImpl.getAccessToken().getOrNull()
        val response = service.changeItem(
            "Bearer $accessToken",
            ItemChangeResponseDTO(mycharacterEquipItemDetailReqList)
        )

        return if (response.isSuccessful) {
            Result.success(MessageInfo(response.body()!!.information!!.message))
        } else {
            Result.failure(Exception("response failure"))
        }
    }
}