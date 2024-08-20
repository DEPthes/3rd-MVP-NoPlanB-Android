package com.growme.growme.presentation.views.calendar

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.growme.growme.data.LoggerUtils
import com.growme.growme.data.model.Quest
import com.growme.growme.databinding.DialogAddQuestBinding
import com.growme.growme.databinding.DialogDoneQuestBinding
import com.growme.growme.databinding.DialogLevelupBinding
import com.growme.growme.databinding.DialogLevelupUnlockBinding
import com.growme.growme.databinding.DialogModifyQuestBinding
import com.growme.growme.databinding.FragmentCalendarBinding
import com.growme.growme.domain.model.calendar.GetMonthExpInfoItem
import com.growme.growme.domain.model.quest.QuestInfo
import com.growme.growme.presentation.UiState
import com.growme.growme.presentation.base.GlobalApplication
import com.growme.growme.presentation.views.home.QuestRvAdapter
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class CalendarFragment : Fragment(), MonthAdapter.OnDateSelectedListener {
    private val viewModel: CalendarViewModel by viewModels()
    private var _binding: FragmentCalendarBinding? = null
    private val binding get() = _binding!!

    private lateinit var monthAdapter: MonthAdapter
    private lateinit var monthListManager: LinearLayoutManager
    private lateinit var questRvAdapter: QuestRvAdapter

    private var questExpList = listOf<GetMonthExpInfoItem>()
    private var questList = mutableListOf<QuestInfo>()
    private val calendar = Calendar.getInstance()
    private val todayMonth = SimpleDateFormat("yyyy-MM", Locale.KOREAN).format(Date())
    private val today = SimpleDateFormat("yyyy-MM-dd", Locale.KOREAN).format(Date())
    private var selectedDate = today
    private var selectedDateExp = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentCalendarBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fetchData()
        initListener()
        setObserver()
        updateMonthDisplay()
    }

    private fun fetchData() {
        viewModel.getMonthExp(todayMonth)
        viewModel.getQuestInfo(today)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initListener() {
        binding.ivPrevMonth.setOnClickListener {
            updateMonth(-1)
            monthAdapter.updateCurrentMonth(-1)

        }
        binding.ivNextMonth.setOnClickListener {
            updateMonth(1)
            monthAdapter.updateCurrentMonth(1)
        }

        binding.ivAddQuest.setOnClickListener {
            showAddQuestDialog(selectedDate)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setObserver() {
        viewModel.monthExpState.observe(viewLifecycleOwner) {
            when (it) {
                is UiState.Failure -> LoggerUtils.e("해당 달 경험치 정보 조회 실패: ${it.error}")
                is UiState.Loading -> {}
                is UiState.Success -> {
                    questExpList = it.data.toMutableList()
                    val today = Calendar.getInstance().time

                    monthAdapter = MonthAdapter(0, today, questExpList)
                    monthAdapter.setOnDateSelectedListener(object :
                        MonthAdapter.OnDateSelectedListener {
                        @SuppressLint("SetTextI18n")
                        override fun onDateSelected(date: Date) {
                            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.KOREAN)
                            val formattedDate = dateFormat.format(date)
                            val currentDate = dateFormat.format(Date())

                            selectedDate = formattedDate
                            viewModel.getQuestInfo(selectedDate)

                            binding.ivAddQuest.visibility =
                                if (selectedDate < currentDate) View.GONE else View.VISIBLE

                            if (formattedDate == currentDate) {
                                binding.tvTodayQuest.text = "오늘의 퀘스트"
                            } else {
                                val dayPortion =
                                    formattedDate.substring(formattedDate.lastIndexOf('-') + 1)
                                val dayWithoutLeadingZero =
                                    if (dayPortion.toInt() < 10) dayPortion.toInt()
                                        .toString() else dayPortion
                                binding.tvTodayQuest.text = "${dayWithoutLeadingZero}일의 퀘스트"
                            }
                        }
                    })


                    monthListManager =
                        LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                    binding.rvCalendar.apply {
                        layoutManager = monthListManager
                        adapter = monthAdapter
                        scrollToPosition(Int.MAX_VALUE / 2)

                        // 좌우 스크롤 막기
                        setOnTouchListener { _, _ -> true }
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

                    // 선택한 날짜의 경험치 총 합 구하기
                    selectedDateExp = 0
                    for (quest in questList) {
                        selectedDateExp += quest.exp
                    }

                    questRvAdapter = QuestRvAdapter(
                        { position -> showModifyQuestDialog(position) },
                        { position -> showDoneQuestDialog(position) },
                        true,
                        selectedDate
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

        viewModel.addFutureState.observe(viewLifecycleOwner) {
            when (it) {
                is UiState.Failure -> LoggerUtils.e("Add Future Quest 실패: ${it.error}")
                is UiState.Loading -> {}
                is UiState.Success -> {
                    LoggerUtils.d(it.data)
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

        viewModel.deleteState.observe(viewLifecycleOwner) {
            when (it) {
                is UiState.Failure -> LoggerUtils.e("Quest 삭제 실패: ${it.error}")
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
                    updateUI()

                    val status = it.data.questType
                    when (status) {
                        "해금" -> {
                            val itemList = it.data.itemImageUrls
                            LoggerUtils.d(itemList.toString())

                            val dialog = showLevelUpDialog(GlobalApplication.userLevel + 1)
                            dialog.setOnDismissListener {
                                showLevelUpUnlockDialog(GlobalApplication.userLevel + 1, itemList)
                            }
                        }

                        "레벨업" -> {
                            showLevelUpDialog(GlobalApplication.userLevel + 1)
                        }

                        else -> {
                            // 그냥 퀘스트 완료일 때
                        }
                    }
                }
            }
        }
    }

    private fun updateMonth(monthChange: Int) {
        calendar.add(Calendar.MONTH, monthChange)
        updateMonthDisplay()
    }

    private fun updateMonthDisplay() {
        val dateFormat = SimpleDateFormat("yyyy년 M월", Locale.KOREAN)
        val formattedDate = dateFormat.format(calendar.time)
        binding.tvSelectMonth.text = formattedDate
    }

    override fun onDateSelected(date: Date) {
        monthAdapter.updateSelectedDate(date)
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.KOREAN)
        selectedDate = dateFormat.format(date)
        val currentDate = dateFormat.format(Date())

        // 해당 날짜 퀘스트 정보 받아오기
        viewModel.getQuestInfo(selectedDate)
        binding.ivAddQuest.visibility = if (selectedDate < currentDate) View.GONE else View.VISIBLE
    }

    @SuppressLint("SetTextI18n")
    private fun showAddQuestDialog(selectedDate: String) {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        val binding = DialogAddQuestBinding.inflate(LayoutInflater.from(requireContext()))
        dialog.setContentView(binding.root)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        var newExp = 1
        binding.tvExp.text = "EXP 1"

        binding.ivExpUp.setOnClickListener {
            if (newExp + selectedDateExp > 9) {
                Toast.makeText(requireContext(), "하루에 얻을 수 있는 경험치는 최대 10입니다", Toast.LENGTH_SHORT)
                    .show()
            } else {
                newExp += 1
                binding.tvExp.text = "EXP $newExp"
            }
        }

        binding.ivExpDown.setOnClickListener {
            if (newExp > 1) {
                newExp -= 1
                binding.tvExp.text = "EXP $newExp"
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
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val questDate = dateFormat.parse(selectedDate)
                val todayDate = dateFormat.parse(dateFormat.format(Date()))

                if (questDate!!.after(todayDate)) {
                    // 오늘 이후 날짜일 경우
                    viewModel.addFutureQuest(selectedDate, newQuestDesc, newExp)
                } else {
                    // 오늘 날짜일 경우
                    viewModel.addQuest(newQuestDesc, newExp)
                }
                dialog.dismiss()

            }
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

    @SuppressLint("NotifyDataSetChanged")
    private fun updateUI() {
        viewModel.getQuestInfo(selectedDate)
        questRvAdapter.notifyDataSetChanged()
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
            viewModel.completeQuest(questList[position].id)
            dialog.dismiss()
        }

        dialog.show()
    }

    @SuppressLint("SetTextI18n")
    private fun showLevelUpDialog(myLevel: Int): Dialog {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        val binding = DialogLevelupBinding.inflate(LayoutInflater.from(requireContext()))
        binding.tvRequireExpText.text = "LV.${myLevel + 1}까지 필요한 EXP"
        binding.tvRequireExp.text = "${myLevel * 10}"
        dialog.setContentView(binding.root)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding.btnOk.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()

        return dialog
    }

    @SuppressLint("SetTextI18n")
    private fun showLevelUpUnlockDialog(myLevel: Int, itemList: List<String>) {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        val binding = DialogLevelupUnlockBinding.inflate(LayoutInflater.from(requireContext()))
        binding.tvRequireExpText.text = "LV.${myLevel + 1}까지 필요한 EXP"
        binding.tvRequireExp.text = "${myLevel * 10}"
        dialog.setContentView(binding.root)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding.ivUnlockMore.visibility = if (itemList.size > 3) View.GONE else View.VISIBLE

        val imageViews = listOf(binding.ivUnlock1, binding.ivUnlock2, binding.ivUnlock3)

        for (i in imageViews.indices) {
            if (i < itemList.size) {
                Glide.with(binding.root.context)
                    .load(itemList[i])
                    .override(79, 79)
                    .skipMemoryCache(true)
                    .dontAnimate()
                    .into(imageViews[i])
            }
        }

        binding.btnOk.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}