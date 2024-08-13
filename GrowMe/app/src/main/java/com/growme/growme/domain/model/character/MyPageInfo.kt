package com.growme.growme.domain.model.character

import com.growme.growme.data.model.character.MyCharaterDetailRes

data class MyPageInfo(
    val characterName: String,
    val growDate: Int,
    val myCharaterDetailResList: List<MyCharacterDetailInfo>,
    val startDate: String,
    val totalExp: Int,
    val totalQuest: Int
)
