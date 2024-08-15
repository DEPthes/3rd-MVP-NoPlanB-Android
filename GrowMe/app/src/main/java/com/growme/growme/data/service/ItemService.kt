package com.growme.growme.data.service

import com.growme.growme.data.model.BaseResponse
import com.growme.growme.data.model.item.CategoryItem
import com.growme.growme.data.model.item.ItemDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface ItemService {
    @GET("api/v1/items/hair")
    suspend fun getHairItems(
        @Header("Authorization") accessToken: String
    ) : Response<BaseResponse<ItemDTO<List<CategoryItem>>>>

    @GET("api/v1/items/fashion")
    suspend fun getFashionItems(
        @Header("Authorization") accessToken: String
    ) : Response<BaseResponse<ItemDTO<List<CategoryItem>>>>

    @GET("api/v1/items/face")
    suspend fun getFaceItems(
        @Header("Authorization") accessToken: String
    ) : Response<BaseResponse<ItemDTO<List<CategoryItem>>>>

    @GET("api/v1/items/background")
    suspend fun getBackgroundItems(
        @Header("Authorization") accessToken: String
    ) : Response<BaseResponse<ItemDTO<List<CategoryItem>>>>
}