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
import com.bumptech.glide.Glide
import com.growme.growme.R
import com.growme.growme.data.LoggerUtils
import com.growme.growme.databinding.FragmentMypageBinding
import com.growme.growme.domain.model.home.ItemData
import com.growme.growme.presentation.UiState
import com.growme.growme.presentation.views.MainActivity

class MyPageFragment : Fragment() {
    private var _binding: FragmentMypageBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MyPageViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentMypageBinding.inflate(layoutInflater)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fetchData()
        setObserver()
        initListener()
    }

    private fun fetchData() {
        viewModel.fetchCharacterInfo()
        viewModel.fetchExpInfo()
    }

    private fun initListener() {
        (activity as? MainActivity)?.let { activity ->
            activity.binding.ivTbSetting.setOnClickListener {
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fl_main, SettingFragment())
                    .addToBackStack("myPage")
                    .commit()
            }
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

                    val dateParts = it.data.startDate.split("-")
                    val year = dateParts[0]
                    val month = dateParts[1]
                    binding.tvStartDate.text = "${year}년 ${month}월부터 성장"

                    binding.tvTotalExp.text = "EXP ${it.data.totalExp}"
                    binding.tvTotalQuest.text = "${it.data.totalQuest} 개"
                    binding.tvTotalDay.text = "D+${it.data.growDate}"

                    // 장착된 캐릭터 아이템 로드
                    val itemList = it.data.myCharaterDetailResList
                    itemList.forEach { item ->
                        handleItem(ItemData(item.itemType, item.itemImage))
                    }
                }
            }
        }

        viewModel.expState.observe(viewLifecycleOwner) {
            when (it) {
                is UiState.Failure -> LoggerUtils.e("Exp Data 조회 실패: ${it.error}")
                is UiState.Loading -> {}
                is UiState.Success -> {
                    val acquireExp = it.data.acquireExp
                    val needExp = it.data.needExp
                    val result = ((acquireExp.toDouble() / needExp.toDouble()) * 10).toInt()
                    showExpProgress(result)
                    binding.tvMyLevel.text = "LV ${it.data.level}"
                    binding.tvExp.text = "${acquireExp}/${needExp}"
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

    private fun loadImage(view: ImageView, itemImage: String, widthDp: Int, heightDp: Int) {
        Glide.with(view.context)
            .load(itemImage)
            .override(widthDp.dpToPx(), heightDp.dpToPx())
            .skipMemoryCache(true)
            .dontAnimate()
            .into(view)
    }

    private fun handleItem(item: ItemData) {
        val (view, widthDp, heightDp) = when (item.itemType) {
            "FACECOLOR" -> Triple(binding.ivCharacter, 105, 216)
            "EYE" -> Triple(binding.ivEye, 75, 33)
            "CLOTHES" -> Triple(binding.ivCloth, 69, 117)
            "HAIR" -> Triple(binding.ivHair, 123, 159)
            "BACKGROUND" -> Triple(binding.ivBackground, 300, 300)
            "GLASSES" -> Triple(binding.ivGlasses, 75, 39)
            "HEAD" -> Triple(binding.ivHat, 123, 81)
            // 다른 itemType 추가 가능
            else -> return
        }

        loadImage(view, item.itemImage, widthDp, heightDp)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}