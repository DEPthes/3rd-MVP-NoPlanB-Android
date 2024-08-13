package com.growme.growme.data.service

import com.growme.growme.data.model.item.ItemDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface ItemService {
    @GET("api/v1/items/hair")
    suspend fun getHairItems() : Response<ItemDTO>

    @GET("api/v1/items/fashion")
    suspend fun getFashionItems() : Response<ItemDTO>

    @GET("api/v1/items/face")
    suspend fun getFaceItems() : Response<ItemDTO>

    @GET("api/v1/items/background")
    suspend fun getBackgroundItems() : Response<ItemDTO>
}