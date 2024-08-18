package com.growme.growme.data.model.character


import com.google.gson.annotations.SerializedName

data class ItemChangeResponseDTO(
    @SerializedName("myCharacterEquipItemDetailReqList")
    val myCharacterEquipItemDetailReqList: List<MyCharacterEquipItemDetailReq>
)