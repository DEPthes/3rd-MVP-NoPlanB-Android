package com.growme.growme.presentation.views.mypage

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.growme.growme.R
import com.growme.growme.data.LoggerUtils
import com.growme.growme.data.model.MyInfo
import com.growme.growme.databinding.FragmentMypageBinding
import com.growme.growme.presentation.UiState
import kotlin.math.roundToInt

class MyPageFragment : Fragment() {
    private lateinit var binding: FragmentMypageBinding
    private val viewModel: MyPageViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMypageBinding.inflate(layoutInflater)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setObserver()
        initListener()
        fetchData()
    }

    private fun fetchData() {
        viewModel.fetchCharacterInfo()
    }

    private fun initListener() {
        binding.btnSetting.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fl_main, SettingFragment())
                .addToBackStack("myPage")
                .commit()
        }
    }


    @SuppressLint("SetTextI18n")
    private fun setObserver() {
        viewModel.fetchInfo.observe(viewLifecycleOwner) {
            when (it) {
                is UiState.Failure -> LoggerUtils.e("Character Data 조회 실패: ${it.error}")
                is UiState.Loading -> {}
                is UiState.Success -> {
                    binding.tvNickname.text = it.data.characterName

                    binding.tvTotalExp.text = "EXP ${it.data.totalExp}"
                    binding.tvTotalQuest.text = "${it.data.totalQuest} 개"
                    binding.tvTotalDay.text = "D+${it.data.growDate}"
                }
            }
        }

    }

    private fun showExpProgress(ratio: Int) {
        val parentLayout = binding.ivExpBar
        parentLayout.removeAllViews() // 이전에 추가된 모든 뷰 제거

        val linearLayout = LinearLayout(requireContext()).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
        }

        for (i in 1..ratio) {
            val imageView = ImageView(requireContext()).apply {
                layoutParams = LinearLayout.LayoutParams(18.dpToPx(), 9.dpToPx()).apply {
                    if (i == 1) {
                        marginStart = 6.dpToPx()
                    }
                }
                setImageResource(R.drawable.img_exp_one)
            }
            linearLayout.addView(imageView)
        }
        parentLayout.addView(linearLayout)
    }

    // dp를 px로 변환하는 함수
    private fun Int.dpToPx(): Int {
        val density = resources.displayMetrics.density
        return (this * density).toInt()
    }
}