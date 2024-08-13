package com.growme.growme.data.model.character


import com.google.gson.annotations.SerializedName

data class GetCharacterItemResponseDTO(
    @SerializedName("myCharaterDetailResList")
    val myCharaterDetailResList: List<MyCharaterDetailRes>
)