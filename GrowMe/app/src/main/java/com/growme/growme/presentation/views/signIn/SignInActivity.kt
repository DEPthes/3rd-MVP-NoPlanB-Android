package com.growme.growme.presentation.views.signIn

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.growme.growme.data.remote.KakaoAuthService
import com.growme.growme.databinding.ActivitySigninBinding
import com.growme.growme.presentation.views.MainActivity

class SignInActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySigninBinding
    private val viewModel: SignInViewModel by viewModels()
    private var backPressedTime: Long = 0
    private lateinit var kakaoAuthService: KakaoAuthService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySigninBinding.inflate(layoutInflater)
        setContentView(binding.root)
        addOnBackPressedCallback()

        kakaoAuthService = KakaoAuthService(this)

        binding.ivKakaoLogin.setOnClickListener {
            kakaoAuthService.signInKakao(
                onSuccess = { userName, userId, accessToken ->
                    Log.i("SignInActivity", "로그인 성공: $userName, ID: $userId, Token: $accessToken")

                    // MainActivity로 이동
                    moveActivity(MainActivity())
                },
                onError = { error ->
                    Log.e("SignInActivity", "로그인 실패", error)
                    Toast.makeText(this, "로그인 실패: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            )
        }
    }

    // 뒤로가기로 스플래시 화면으로 가는 것 방지
    private fun addOnBackPressedCallback() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (System.currentTimeMillis() - backPressedTime >= 2000) {
                    backPressedTime = System.currentTimeMillis()
                    Toast.makeText(this@SignInActivity, "한번 더 누르면 앱을 종료합니다.", Toast.LENGTH_SHORT)
                        .show()
                } else if (System.currentTimeMillis() - backPressedTime < 2000) {
                    finish()
                }
            }
        }
        this.onBackPressedDispatcher.addCallback(this, callback)
    }

    private fun moveActivity(p: Activity) {
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, p::class.java)
            startActivity(intent)
            finish()
        }, 1000)
    }
}