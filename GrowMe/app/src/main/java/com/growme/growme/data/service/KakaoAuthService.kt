package com.growme.growme.data.service

import android.content.Context
import android.util.Log
import com.growme.growme.data.LoggerUtils
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient

class KakaoAuthService(private val context: Context) {

    private val client = UserApiClient.instance

    private val isKakaoTalkLoginAvailable: Boolean
        get() = client.isKakaoTalkLoginAvailable(context)

    fun signInKakao(
        loginListener: ((String, String) -> Unit)
    ) {
        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                signInError(error)
            } else if (token != null) signInSuccess(token, loginListener)
        }

        if (isKakaoTalkLoginAvailable) {
            client.loginWithKakaoTalk(context, callback = callback)
        } else {
            client.loginWithKakaoAccount(context, callback = callback)
        }
    }

    private fun signInError(throwable: Throwable) {
        val kakaoType = if (isKakaoTalkLoginAvailable) "카카오톡" else "카카오계정"
        Log.e("LOGIN", "{$kakaoType}으로 로그인 실패 ${throwable.message}")
    }

    private fun signInSuccess(
        oAuthToken: OAuthToken,
        loginListener: ((String, String) -> Unit)
    ) {
        client.me { user, error ->
            LoggerUtils.d("kakao access token: ${oAuthToken.accessToken}, ${user!!.kakaoAccount!!.email!!}")
            loginListener(oAuthToken.accessToken, user.kakaoAccount!!.email!!)
            if (error != null) {
                Log.e("LOGIN", "사용자 정보 요청 실패 $error")
            }
        }
    }

    fun signOutKakao() {
        // 연결 끊기
        UserApiClient.instance.unlink { error ->
            if (error != null) {
                Log.e("TAG", "연결 끊기 실패", error)
            } else {
                Log.i("TAG", "연결 끊기 성공. SDK에서 토큰 삭제 됨")
            }
        }
    }
}
