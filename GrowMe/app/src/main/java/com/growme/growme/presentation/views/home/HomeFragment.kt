package com.growme.growme.presentation.views.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.view.marginStart
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.growme.growme.R
import com.growme.growme.data.model.MyInfo
import com.growme.growme.data.model.TodayQuest
import com.growme.growme.databinding.FragmentHomeBinding
import com.growme.growme.databinding.FragmentItemBinding
import kotlin.math.roundToInt

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var todayQuestAdapter: TodayQuestAdapter
    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setTodayQuestRv()

        viewModel.myInfo.observe(viewLifecycleOwner) { myInfo ->
            val totalExpForLevel = myInfo.level * 10
            val expRatio = myInfo.exp.toDouble() / totalExpForLevel.toDouble()
            val roundedExpRatio = (expRatio * 10).roundToInt()

            binding.tvMyLevel.text = "LV ${myInfo.level}"
            binding.tvExp.text = "${myInfo.exp}/${totalExpForLevel}"
            binding.tvNickname.text = myInfo.nickname

            showExpProgress(roundedExpRatio)
        }

        viewModel.fetchData(MyInfo("닉네임 예시입니다", 12, 80))
    }

    private fun setTodayQuestRv() {
        val questList = listOf(
            TodayQuest("퀘스트 내용입니다1", 10, false),
            TodayQuest("퀘스트 내용입니다2", 8, false),
            TodayQuest("퀘스트 내용입니다3", 1, true),
            TodayQuest("퀘스트 내용입니다12", 3, true)
        )
        todayQuestAdapter = TodayQuestAdapter()
        todayQuestAdapter.setData(questList)

        binding.rvTodayQuest.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = todayQuestAdapter
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