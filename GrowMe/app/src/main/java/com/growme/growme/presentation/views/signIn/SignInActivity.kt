package com.growme.growme.presentation.views.signIn

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.growme.growme.R
import com.growme.growme.data.LoggerUtils
import com.growme.growme.data.service.KakaoAuthService
import com.growme.growme.databinding.ActivitySigninBinding
import com.growme.growme.presentation.UiState
import com.growme.growme.presentation.views.MainActivity
import com.growme.growme.presentation.views.characterSetting.CharacterSettingActivity

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
        observer()

        binding.ivKakaoLogin.setOnClickListener {
            kakaoAuthService.signInKakao(viewModel::login)
        }
    }

    private fun observer() {
        viewModel.loginState.observe(this) {
            when (it) {
                is UiState.Failure -> {
                    LoggerUtils.e("로그인 실패: ${it.error}")
                }
                is UiState.Loading -> {}
                is UiState.Success -> {
                    LoggerUtils.d("로그인 성공")
                    showSuccessDialog()
                    viewModel.isUserRegistered()
                    registerObserver()
                }
            }
        }
    }

    private fun registerObserver() {
        viewModel.isUserRegisteredState.observe(this) {
            when (it) {
                is UiState.Failure -> LoggerUtils.e("사용자를 찾을 수 없음: ${it.error}")
                UiState.Loading -> {}
                is UiState.Success -> {
                    LoggerUtils.d("응답 원본: ${it.data}")
                    LoggerUtils.d("${it.data.exist}")
                    if (it.data.exist) {
                        moveActivity(MainActivity())
                    }
                    else {
                        moveActivity(CharacterSettingActivity())
                    }
                }
            }
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

    private var dialog: Dialog? = null

    private fun showSuccessDialog() {
        if (isFinishing || isDestroyed) return

        dialog = Dialog(this)
        dialog?.apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setContentView(LayoutInflater.from(this@SignInActivity).inflate(R.layout.dialog_login_success, null))
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            show()

            binding.ivKakaoLogin.visibility = View.INVISIBLE
            binding.tvStart.visibility = View.VISIBLE

            Handler(Looper.getMainLooper()).postDelayed({
                if (!isFinishing && !isDestroyed) {
                    dismiss()
                }
            }, 2000)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        dialog?.dismiss()
        dialog = null
    }
}