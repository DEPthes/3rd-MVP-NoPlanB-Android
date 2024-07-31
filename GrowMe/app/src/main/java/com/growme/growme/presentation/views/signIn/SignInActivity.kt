package com.growme.growme.presentation.views.signIn

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.growme.growme.R
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
//        Log.d("KeyHash", "keyhash : ${Utility.getKeyHash(this)}")

        addOnBackPressedCallback()

        kakaoAuthService = KakaoAuthService(this)

        binding.ivKakaoLogin.setOnClickListener {
            kakaoAuthService.signInKakao(
                onSuccess = { userName, userId, accessToken ->
                    Log.i("SignInActivity", "로그인 성공: $userName, ID: $userId, Token: $accessToken")

                    showSuccessDialog()

                    binding.tvStart.setOnClickListener {
                        // MainActivity로 이동
                        moveActivity(MainActivity())
                    }
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

    private fun showSuccessDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(
            LayoutInflater.from(this).inflate(R.layout.dialog_login_success, null)
        )
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()

        // 로그인 성공 후 로그인 버튼 가리기
        binding.ivKakaoLogin.visibility = View.INVISIBLE
        binding.tvStart.visibility = View.VISIBLE

        // 로그인 성공 다이얼로그 2초 표시
        Handler(Looper.getMainLooper()).postDelayed({
            dialog.dismiss()
        }, 2000)
    }
}