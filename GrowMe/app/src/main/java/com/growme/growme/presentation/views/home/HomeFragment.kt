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
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.growme.growme.R
import com.growme.growme.data.LoggerUtils
import com.growme.growme.data.model.MyInfo
import com.growme.growme.data.model.Quest
import com.growme.growme.databinding.DialogAddQuestBinding
import com.growme.growme.databinding.DialogDoneAllQuestBinding
import com.growme.growme.databinding.DialogDoneQuestBinding
import com.growme.growme.databinding.DialogLevelupBinding
import com.growme.growme.databinding.DialogLevelupUnlockBinding
import com.growme.growme.databinding.DialogModifyQuestBinding
import com.growme.growme.databinding.FragmentHomeBinding
import com.growme.growme.domain.model.QuestInfo
import com.growme.growme.presentation.UiState
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.roundToInt

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var questRvAdpater: QuestRvAdapter
    private val viewModel: HomeViewModel by viewModels()
//    private var filteredQuests: MutableList<QuestInfo> = mutableListOf()
    private var questList = mutableListOf<QuestInfo>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//
//        characterSetting()
        setObservers()
        initListener()
        fetchData()
    }

    private fun fetchData() {
        viewModel.fetchCharacterInfo()
        viewModel.fetchExpInfo()

        val today = SimpleDateFormat("yyyy-MM-dd", Locale.KOREAN).format(Date())
        viewModel.fetchQuestInfo(today)
    }

    private fun initListener() {
        binding.ivAddQuest.setOnClickListener {
            showAddQuestDialog()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setObservers() {
        viewModel.expState.observe(viewLifecycleOwner) {
            when (it) {
                is UiState.Failure -> LoggerUtils.e("Home Data 조회 실패: ${it.error}")
                is UiState.Loading -> {}
                is UiState.Success -> {
                    binding.tvMyLevel.text = "LV ${it.data.level}"
                    binding.tvTodayExp.text = "${it.data.todayExp}/10"
                }
            }
        }

        viewModel.characterState.observe(viewLifecycleOwner) {
            when (it) {
                is UiState.Failure -> LoggerUtils.e("Character Data 조회 실패: ${it.error}")
                is UiState.Loading -> {}
                is UiState.Success -> {
                    binding.tvNickname.text = it.data.characterName
                }
            }
        }

        viewModel.questState.observe(viewLifecycleOwner) {
            when (it) {
                is UiState.Failure -> LoggerUtils.e("Quest Data 조회 실패: ${it.error}")
                is UiState.Loading -> {}
                is UiState.Success -> {
                    questList = it.data.toMutableList()

                    questRvAdpater = QuestRvAdapter(
                        { position -> showModifyQuestDialog(position) },
                        { position -> showDoneQuestDialog(position) },
                        false
                    )
                    questRvAdpater.setData(questList)
                    binding.rvTodayQuest.apply {
                        setHasFixedSize(true)
                        layoutManager = LinearLayoutManager(requireContext())
                        adapter = questRvAdpater
                    }
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

    private fun showAddQuestDialog() {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        val binding = DialogAddQuestBinding.inflate(LayoutInflater.from(requireContext()))
        dialog.setContentView(binding.root)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        var newExp = 0
        binding.tvExp.text = newExp.toString()

        binding.ivExpUp.setOnClickListener {
            newExp += 1
            binding.tvExp.text = newExp.toString()
        }

        binding.ivExpDown.setOnClickListener {
            if (newExp > 0) {
                newExp -= 1
                binding.tvExp.text = newExp.toString()
            }
        }

        binding.btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        binding.btnOk.setOnClickListener {
            val newQuestDesc = binding.etQuestDesc.text.toString().trim()

            if (newQuestDesc.isNotEmpty()) {
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val currentDate = dateFormat.format(Date())

                // 새로운 Quest 객체 생성
                val newQuest = Quest(
                    desc = newQuestDesc,
                    exp = newExp,
                    finished = false,
                    date = currentDate
                )

//                filteredQuests.add(newQuest)
//                updateUI()
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

        // 기존 퀘스트 정보 설정
        binding.etQuestDesc.setText(questList[position].contents)
        binding.tvExpText.text = "EXP ${questList[position].exp}"

        binding.btnModify.setOnClickListener {
            val newQuestDesc = binding.etQuestDesc.text.toString().trim()

            if (newQuestDesc.isNotEmpty()) {
//                filteredQuests[position].contents = newQuestDesc
//                updateUI()
            }

            dialog.dismiss()
        }

        binding.btnDelete.setOnClickListener {
            val questToDelete = questList[position]
            questList.removeAt(position)
            questList.remove(questToDelete)

//            updateUI()
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
            dialog.dismiss()
            // 경험치 추가 로직 구현
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
//    private fun updateUI() {
//        val today = SimpleDateFormat("yyyy-MM-dd", Locale.KOREAN).format(Date())
//        filteredQuests = filteredQuests.filter { quest -> quest.date == today }.toMutableList()
//        questRvAdpater.setData(filteredQuests)
//        questRvAdpater.notifyDataSetChanged()
//    }

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
}