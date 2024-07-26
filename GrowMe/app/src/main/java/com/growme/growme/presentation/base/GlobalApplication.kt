package com.growme.growme.presentation.base

import android.app.Application
import com.growme.growme.BuildConfig
import com.kakao.sdk.common.KakaoSdk

class GlobalApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // 다른 초기화 코드들

        // Kakao SDK 초기화
        KakaoSdk.init(this, BuildConfig.KAKAO_NATIVE_KEY)

//        val keyHash = Utility.getKeyHash(this)
//        LoggerUtils.debug("해시 키 값 : $keyHash")
    }
}