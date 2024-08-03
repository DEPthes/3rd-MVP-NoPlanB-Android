package com.growme.growme.data.model

data class Quest(
    var desc: String,
    var exp: Int,
    var finished: Boolean,
    val date: String
)