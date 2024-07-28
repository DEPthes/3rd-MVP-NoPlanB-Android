package com.growme.growme.presentation.views

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.growme.growme.data.remote.KakaoAuthService
import com.growme.growme.databinding.ActivityMainBinding
import com.growme.growme.presentation.views.signIn.SignInActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var kakaoAuthService: KakaoAuthService

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        kakaoAuthService = KakaoAuthService(this)

        binding.btnLogout.setOnClickListener{
            kakaoAuthService.signOutKakao()
            moveActivity(SignInActivity())
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