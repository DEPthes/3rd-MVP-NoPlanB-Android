package com.growme.growme.presentation.views.splash

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.growme.growme.R
import com.growme.growme.data.LoggerUtils
import com.growme.growme.presentation.base.UiState
import com.growme.growme.presentation.views.MainActivity
import com.growme.growme.presentation.views.signIn.SignInActivity

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    private val viewModel: SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        moveActivity(SignInActivity())
//        moveActivity(MainActivity())
    }

    private fun observer() {
        viewModel.loginState.observe(this) {
            when (it) {
                is UiState.Failure -> {
                    LoggerUtils.e("자동 로그인 실패: ${it.error}")
                    moveActivity(SignInActivity())
                }

                is UiState.Loading -> {}
                is UiState.Success -> {
                    LoggerUtils.d("자동 로그인 성공")

                    // 자동 로그인 성공
                    moveActivity(MainActivity())
                }
            }
        }
    }

    private fun moveActivity(p: Activity) {
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, p::class.java)
            startActivity(intent)
            finish()
        }, 1000)
    }
}