package com.growme.growme.presentation.views.mypage

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.growme.growme.data.LoggerUtils
import com.growme.growme.data.service.KakaoAuthService
import com.growme.growme.databinding.FragmentSettingBinding
import com.growme.growme.presentation.base.UiState
import com.growme.growme.presentation.views.MainActivity
import com.growme.growme.presentation.views.signIn.SignInActivity

class SettingFragment : Fragment() {
    private val viewModel: MyPageViewModel by viewModels()

    private lateinit var binding: FragmentSettingBinding
    private lateinit var kakaoAuthService: KakaoAuthService

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentSettingBinding.inflate(layoutInflater)
        kakaoAuthService = KakaoAuthService(requireContext())

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListener()
        setObserver()
        fetchData()
    }

    override fun onResume() {
        super.onResume()
        // 툴바 숨기기
        (activity as? MainActivity)?.supportActionBar?.hide()
    }

    override fun onPause() {
        super.onPause()
        // 툴바 다시 보이기
        (activity as? MainActivity)?.supportActionBar?.show()
    }

    private fun initListener() {

        binding.ibBack.setOnClickListener {
            (requireActivity() as MainActivity).supportFragmentManager.popBackStack()
        }

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
            viewModel.withdraw()
        }
    }

    private fun setObserver() {
        viewModel.settingState.observe(viewLifecycleOwner) {
            when (it) {
                is UiState.Failure -> LoggerUtils.e("Email 조회 실패: ${it.error}")
                is UiState.Loading -> {}
                is UiState.Success -> {
                    binding.tvEmail.text = it.data.msg
                }
            }
        }

        viewModel.withdrawState.observe(viewLifecycleOwner) {
            when (it) {
                is UiState.Loading -> {}
                is UiState.Failure -> {
                    Toast.makeText(requireContext(), "회원 탈퇴 실패", Toast.LENGTH_SHORT).show()
                }

                is UiState.Success -> {
                    viewModel.clearData()
                }
            }
        }

        viewModel.clearState.observe(viewLifecycleOwner) {
            when (it) {
                is UiState.Loading -> {}
                is UiState.Failure -> {
                    Toast.makeText(requireContext(), "회원 정보 삭제 실패", Toast.LENGTH_SHORT).show()
                }

                is UiState.Success -> {
                    Toast.makeText(requireContext(), "회원 탈퇴, 회원 정보 삭제 성공", Toast.LENGTH_SHORT).show()
                    requireActivity().apply {
                        startActivity(Intent(this, SignInActivity::class.java))
                        finish()
                    }
                }
            }
        }
    }

    private fun fetchData() {
        viewModel.fetchSettingInfo()
    }

    private fun moveActivityToSignIn() {
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(requireContext(), SignInActivity::class.java)
            startActivity(intent)
            activity?.finish() // MainActivity 종료
        }, 1000)
    }
}