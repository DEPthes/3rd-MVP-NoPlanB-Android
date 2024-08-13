package com.growme.growme.data

import com.google.gson.GsonBuilder
import com.growme.growme.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "http://52.78.221.240:8080/"
    private var instance: Retrofit? = null
    private val gson = GsonBuilder().setLenient().create()

    private val loggingInterceptor = if (BuildConfig.DEBUG) {
        HttpLoggingInterceptor(PrettyJsonLogger()).setLevel(
            HttpLoggingInterceptor.Level.BODY)
    } else {
        HttpLoggingInterceptor(PrettyJsonLogger()).setLevel(
            HttpLoggingInterceptor.Level.BODY)
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    fun getInstance(): Retrofit {
        if (instance == null) {
            instance = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build()
        }
        return instance!!
    }
}