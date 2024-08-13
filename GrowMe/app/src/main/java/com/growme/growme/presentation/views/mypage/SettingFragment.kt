package com.growme.growme.presentation.views.mypage

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.growme.growme.data.service.KakaoAuthService
import com.growme.growme.databinding.FragmentSettingBinding
import com.growme.growme.presentation.views.signIn.SignInActivity
import com.growme.growme.presentation.views.signIn.SignInViewModel

class SettingFragment : Fragment() {
    private lateinit var binding: FragmentSettingBinding
    private lateinit var kakaoAuthService: KakaoAuthService

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingBinding.inflate(layoutInflater)
        kakaoAuthService = KakaoAuthService(requireContext())

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        binding.tvEmail.text = getKakaoAccount.getEmail()

        binding.ivTermsOfUse.setOnClickListener {
            // 이용약관
        }

        binding.ivPolicy.setOnClickListener {
            // 개인정보 정책
        }

        binding.ivSignout.setOnClickListener {
            // 로그아웃
            kakaoAuthService.signOutKakao()
            moveActivityToSignIn()
        }

        binding.ivWithdrawal.setOnClickListener {
            // 서비스 탈퇴
        }
    }

    private fun moveActivityToSignIn() {
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(requireContext(), SignInActivity::class.java)
            startActivity(intent)
            activity?.finish() // MainActivity 종료
        }, 1000)
    }
}