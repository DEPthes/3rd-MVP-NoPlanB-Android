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
import com.growme.growme.data.model.MyInfo
import com.growme.growme.data.model.Quest
import com.growme.growme.databinding.DialogAddQuestBinding
import com.growme.growme.databinding.DialogDoneAllQuestBinding
import com.growme.growme.databinding.DialogDoneQuestBinding
import com.growme.growme.databinding.DialogLevelupBinding
import com.growme.growme.databinding.DialogLevelupUnlockBinding
import com.growme.growme.databinding.DialogModifyQuestBinding
import com.growme.growme.databinding.FragmentHomeBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.roundToInt

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var questRvAdpater: QuestRvAdapter
    private val viewModel: HomeViewModel by viewModels()
    private var filteredQuests: MutableList<Quest> = mutableListOf()

    private val questList = mutableListOf(
        Quest("퀘스트 내용입니다1", 10, false, "2024-08-03"),
        Quest("퀘스트 내용입니다2", 8, false, "2024-08-03"),
        Quest("퀘스트 내용입니다3", 1, true, "2024-08-03"),
        Quest("퀘스트 내용입니다12", 3, true, "2024-08-03"),
        Quest("8월 2일 퀘스트", 5, false, "2024-08-02"),
        Quest("8월 1일 퀘스트", 8, false, "2024-08-01"),
        Quest("8월 1일 퀘스트2", 3, true, "2024-08-01")
    )

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

        binding.ivAddQuest.setOnClickListener {
            showAddQuestDialog()
        }
    }

    private fun setTodayQuestRv() {
        questRvAdpater = QuestRvAdapter(
            { position -> showModifyQuestDialog(position) },
            { position -> showDoneQuestDialog(position) },
            false
        )

        // 오늘 날짜에 맞는 퀘스트 필터링
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.KOREAN).format(Date())
        filteredQuests = questList.filter { quest -> quest.date == today }.toMutableList()
        questRvAdpater.setData(filteredQuests)

        binding.rvTodayQuest.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = questRvAdpater
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

                filteredQuests.add(newQuest)
                updateUI()
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
        binding.etQuestDesc.setText(filteredQuests[position].desc)
        binding.tvExpText.text = "EXP ${filteredQuests[position].exp}"

        binding.btnModify.setOnClickListener {
            val newQuestDesc = binding.etQuestDesc.text.toString().trim()

            if (newQuestDesc.isNotEmpty()) {
                filteredQuests[position].desc = newQuestDesc
                updateUI()
            }

            dialog.dismiss()
        }

        binding.btnDelete.setOnClickListener {
            val questToDelete = filteredQuests[position]
            filteredQuests.removeAt(position)
            questList.remove(questToDelete)

            updateUI()
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

        binding.tvDoneExp.text = "EXP ${filteredQuests[position].exp}"
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

    private fun updateUI() {
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.KOREAN).format(Date())
        filteredQuests = filteredQuests.filter { quest -> quest.date == today }.toMutableList()
        questRvAdpater.setData(filteredQuests)
        questRvAdpater.notifyDataSetChanged()
    }
}