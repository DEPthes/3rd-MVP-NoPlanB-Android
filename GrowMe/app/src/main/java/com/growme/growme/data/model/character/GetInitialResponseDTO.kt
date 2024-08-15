package com.growme.growme.data.model.character


import com.google.gson.annotations.SerializedName

data class GetInitialResponseDTO(
    @SerializedName("characterName")
    val characterName: String,
    @SerializedName("myCharaterDetailResList")
    val myCharaterDetailResList: List<MyCharaterDetailRes>
)