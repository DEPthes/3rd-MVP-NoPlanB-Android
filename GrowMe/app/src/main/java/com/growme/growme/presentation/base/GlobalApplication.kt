package com.growme.growme.presentation.base

import android.app.Application
import com.growme.growme.BuildConfig
import com.growme.growme.data.repository.UserPreferencesRepositoryImpl
import com.kakao.sdk.common.KakaoSdk

class GlobalApplication : Application() {

    companion object {
        lateinit var app: GlobalApplication
        var userId: Int = -1
    }

    lateinit var userPreferences: UserPreferencesRepositoryImpl

    override fun onCreate() {
        super.onCreate()

        KakaoSdk.init(this, BuildConfig.KAKAO_NATIVE_KEY)
        app = this
        userPreferences = UserPreferencesRepositoryImpl()
    }
}