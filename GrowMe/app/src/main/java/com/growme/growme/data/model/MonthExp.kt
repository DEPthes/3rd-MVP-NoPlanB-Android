package com.growme.growme.data.model

// 각 달마다 날짜와, 그 날짜에 얻은 경험치 정보 (캘린더 페이지에서 사용)
data class MonthExp(
    val date: String,
    val exp: Int
)
