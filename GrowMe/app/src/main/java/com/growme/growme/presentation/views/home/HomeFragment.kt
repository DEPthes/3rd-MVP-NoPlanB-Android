package com.growme.growme.presentation.views.home

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.growme.growme.R
import com.growme.growme.data.LoggerUtils
import com.growme.growme.databinding.DialogAddQuestBinding
import com.growme.growme.databinding.DialogDoneAllQuestBinding
import com.growme.growme.databinding.DialogDoneQuestBinding
import com.growme.growme.databinding.DialogLevelupBinding
import com.growme.growme.databinding.DialogLevelupUnlockBinding
import com.growme.growme.databinding.DialogModifyQuestBinding
import com.growme.growme.databinding.FragmentHomeBinding
import com.growme.growme.domain.model.home.ItemData
import com.growme.growme.domain.model.quest.QuestInfo
import com.growme.growme.presentation.UiState
import com.growme.growme.presentation.base.GlobalApplication
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.round

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var questRvAdapter: QuestRvAdapter
    private val viewModel: HomeViewModel by viewModels()

    private var questList = mutableListOf<QuestInfo>()
    private val today = SimpleDateFormat("yyyy-MM-dd", Locale.KOREAN).format(Date())
    private var todayExp = 0

    // 퀘스트 완료 시 다이얼로그에 표시할 Id 저장
    private var currentPosition: Int? = -1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        characterSetting()
        setObservers()
        initListener()
        fetchData()
    }

    private fun fetchData() {
        viewModel.fetchCharacterInfo()
        viewModel.fetchExpInfo()
        viewModel.fetchQuestInfo(today)
    }

    private fun initListener() {
        binding.ivAddQuest.setOnClickListener {
            showAddQuestDialog(todayExp)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setObservers() {
        viewModel.expState.observe(viewLifecycleOwner) {
            when (it) {
                is UiState.Failure -> LoggerUtils.e("Home Data 조회 실패: ${it.error}")
                is UiState.Loading -> {}
                is UiState.Success -> {
                    todayExp = it.data.todayExp
                    val acquireExp = it.data.acquireExp
                    val needExp = it.data.needExp
                    val result = round((acquireExp.toDouble() / needExp.toDouble()) * 10).toInt()
                    showExpProgress(result)
                    binding.tvMyLevel.text = "LV ${it.data.level}"
                    binding.tvExp.text = "${acquireExp}/${needExp}"
                }
            }
        }

        viewModel.characterState.observe(viewLifecycleOwner) {
            when (it) {
                is UiState.Failure -> LoggerUtils.e("Character Data 조회 실패: ${it.error}")
                is UiState.Loading -> {}
                is UiState.Success -> {
                    val nickname = it.data.characterName
                    GlobalApplication.nickname = nickname
                    binding.tvNickname.text = nickname

                    // 장착된 캐릭터 로드
                    val itemList = it.data.myCharaterDetailResList
                    itemList.forEach { item ->
                        handleItem(ItemData(item.itemType, item.itemImage))
                    }
                }
            }
        }

        viewModel.questState.observe(viewLifecycleOwner) {
            when (it) {
                is UiState.Failure -> LoggerUtils.e("Quest Data 조회 실패: ${it.error}")
                is UiState.Loading -> {}
                is UiState.Success -> {
                    questList = it.data.toMutableList()
                    questList.sortBy { data -> data.isComplete }

                    questRvAdapter = QuestRvAdapter(
                        { position -> showModifyQuestDialog(position) },
                        { position -> showDoneQuestDialog(position) },
                        false,
                        today
                    )
                    questRvAdapter.setData(questList)
                    binding.rvTodayQuest.apply {
                        setHasFixedSize(true)
                        layoutManager = LinearLayoutManager(requireContext())
                        adapter = questRvAdapter
                    }
                }
            }
        }

        viewModel.addState.observe(viewLifecycleOwner) {
            when (it) {
                is UiState.Failure -> LoggerUtils.e("Add Quest 실패: ${it.error}")
                is UiState.Loading -> {}
                is UiState.Success -> {
                    LoggerUtils.d(it.data.msg)
                    updateUI()
                }
            }
        }

        viewModel.updateState.observe(viewLifecycleOwner) {
            when (it) {
                is UiState.Failure -> LoggerUtils.e("Quest 수정 실패: ${it.error}")
                is UiState.Loading -> {}
                is UiState.Success -> {
                    LoggerUtils.d(it.data.msg)
                    updateUI()
                }
            }
        }

        viewModel.completeState.observe(viewLifecycleOwner) {
            when (it) {
                is UiState.Failure -> LoggerUtils.e("Complete Quest 실패: ${it.error}")
                is UiState.Loading -> {}
                is UiState.Success -> {
                    val status = it.data.questType
                    if (status == "해금") {
                    } else if (status == "레벨업") {
                    } else {
                        currentPosition?.let { position ->
                            showDoneQuestDialog(position)
                        }
                    }

                    updateUI()
                }
            }
        }

        viewModel.deleteState.observe(viewLifecycleOwner) {
            when (it) {
                is UiState.Failure -> LoggerUtils.e("Delete Quest 실패: ${it.error}")
                is UiState.Loading -> {}
                is UiState.Success -> {
                    LoggerUtils.d(it.data.msg)
                    updateUI()
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

    private fun showAddQuestDialog(todayExp: Int) {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        val binding = DialogAddQuestBinding.inflate(LayoutInflater.from(requireContext()))
        dialog.setContentView(binding.root)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        var newExp = 1
        binding.tvExp.text = newExp.toString()

        binding.ivExpUp.setOnClickListener {
            if (newExp + todayExp > 9) {
                Toast.makeText(requireContext(), "하루에 얻을 수 있는 경험치는 최대 10입니다", Toast.LENGTH_SHORT)
                    .show()
            } else {
                newExp += 1
                binding.tvExp.text = newExp.toString()
            }
        }

        binding.ivExpDown.setOnClickListener {
            if (newExp > 1) {
                newExp -= 1
                binding.tvExp.text = newExp.toString()
            }
        }

        binding.btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        binding.btnOk.setOnClickListener {
            val newQuestDesc = binding.etQuestDesc.text.toString().trim()

            if (newQuestDesc.isEmpty()) {
                Toast.makeText(requireContext(), "퀘스트를 입력해주세요", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.addQuest(newQuestDesc, newExp)
            }
            dialog.dismiss()
        }

        dialog.show()
    }

    @SuppressLint("SetTextI18n")
    private fun showModifyQuestDialog(position: Int) {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        val binding = DialogModifyQuestBinding.inflate(LayoutInflater.from(requireContext()))
        dialog.setContentView(binding.root)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding.etQuestDesc.setText(questList[position].contents)
        binding.tvExpText.text = "EXP ${questList[position].exp}"

        binding.btnModify.setOnClickListener {
            val newQuestDesc = binding.etQuestDesc.text.toString().trim()

            if (newQuestDesc.isEmpty()) {
                Toast.makeText(requireContext(), "퀘스트를 입력해주세요", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.updateQuest(questList[position].id, newQuestDesc)
            }
            dialog.dismiss()
        }

        binding.btnDelete.setOnClickListener {
            viewModel.deleteQuest(questList[position].id)
            dialog.dismiss()
        }

        dialog.show()
    }

    @SuppressLint("SetTextI18n")
    private fun showDoneQuestDialog(position: Int) {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        val binding = DialogDoneQuestBinding.inflate(LayoutInflater.from(requireContext()))
        dialog.setContentView(binding.root)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding.tvDoneExp.text = "EXP ${questList[position].exp}"
        binding.btnGet.setOnClickListener {
            currentPosition = position
            viewModel.completeQuest(questList[position].id)
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun showDoneAllQuestDialog() {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        val binding = DialogDoneAllQuestBinding.inflate(LayoutInflater.from(requireContext()))
        dialog.setContentView(binding.root)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }

    private fun showLevelUpDialog() {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        val binding = DialogLevelupBinding.inflate(LayoutInflater.from(requireContext()))
        dialog.setContentView(binding.root)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }

    private fun showLevelUpUnlockDialog() {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        val binding = DialogLevelupUnlockBinding.inflate(LayoutInflater.from(requireContext()))
        dialog.setContentView(binding.root)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun updateUI() {
        viewModel.fetchQuestInfo(today)
        questRvAdapter.notifyDataSetChanged()
    }

    private fun characterSetting() {
        val skinValue = arguments?.getInt("skin", 1)
        val faceValue = arguments?.getInt("face", 1)
        val hairValue = arguments?.getInt("hair", 1)
        val clothesValue = arguments?.getInt("clothes", 1)
        val nameValue = arguments?.getString("name", "grow me")

        binding.tvNickname.text = nameValue

        LoggerUtils.i("$skinValue, $faceValue, $hairValue, $clothesValue, $nameValue")

        when (skinValue) {
            1 -> binding.ivCharacter.setBackgroundResource(R.drawable.character_1)
            2 -> binding.ivCharacter.setBackgroundResource(R.drawable.character_2)
            3 -> binding.ivCharacter.setBackgroundResource(R.drawable.character_3)
        }

        when (faceValue) {
            1 -> binding.ivFace.setBackgroundResource(R.drawable.face_1)
            2 -> binding.ivFace.setBackgroundResource(R.drawable.face_2)
            3 -> binding.ivFace.setBackgroundResource(R.drawable.face_3)
        }

        when (hairValue) {
            1 -> binding.ivHair.setBackgroundResource(R.drawable.hair1_for_total_character)
            2 -> binding.ivHair.setBackgroundResource(R.drawable.hair2_for_total_character)
            3 -> binding.ivHair.setBackgroundResource(R.drawable.hair3_for_total_character)
        }

        when (clothesValue) {
            1 -> binding.ivCloth.setBackgroundResource(R.drawable.clothes_1)
            2 -> binding.ivCloth.setBackgroundResource(R.drawable.clothes_2)
        }
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
            "EYE" -> Triple(binding.ivFace, 75, 33)
            "CLOTHES" -> Triple(binding.ivCloth, 69, 117)
            "HAIR" -> Triple(binding.ivHair, 123, 159)
            "BACKGROUND" -> Triple(binding.ivBackground, 300, 300)
            // 다른 itemType 추가 가능
            else -> return
        }

        loadImage(view, item.itemImage, widthDp, heightDp)
    }
}