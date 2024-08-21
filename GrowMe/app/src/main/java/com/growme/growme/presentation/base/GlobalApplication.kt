package com.growme.growme.presentation.base

import android.app.Application
import android.provider.ContactsContract.Data
import com.growme.growme.BuildConfig
import com.growme.growme.data.repository.DataStoreRepositoryImpl
import com.kakao.sdk.common.KakaoSdk

class GlobalApplication : Application() {

    companion object {
        lateinit var app: GlobalApplication
        var userLevel: Int = 0
        lateinit var nickname: String
    }

    lateinit var userPreferences: DataStoreRepositoryImpl

    override fun onCreate() {
        super.onCreate()

        KakaoSdk.init(this, BuildConfig.KAKAO_NATIVE_KEY)
        app = this
        userPreferences = DataStoreRepositoryImpl()
    }
}