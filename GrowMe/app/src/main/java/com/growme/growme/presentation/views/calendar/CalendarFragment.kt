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
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.growme.growme.data.LoggerUtils
import com.growme.growme.data.model.MonthExp
import com.growme.growme.data.model.Quest
import com.growme.growme.databinding.DialogAddQuestBinding
import com.growme.growme.databinding.DialogDoneQuestBinding
import com.growme.growme.databinding.DialogModifyQuestBinding
import com.growme.growme.databinding.FragmentCalendarBinding
import com.growme.growme.databinding.FragmentMypageBinding
import com.growme.growme.domain.model.calendar.GetMonthExpInfo
import com.growme.growme.domain.model.calendar.GetMonthExpInfoItem
import com.growme.growme.domain.model.calendar.MonthQuestInfo
import com.growme.growme.domain.model.quest.QuestInfo
import com.growme.growme.presentation.UiState
import com.growme.growme.presentation.views.home.QuestRvAdapter
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.math.round

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
    private var filteredQuests: MutableList<Quest> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
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
            showAddQuestDialog(selectedDate, questExpList)
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
                    monthAdapter.setOnDateSelectedListener(this)

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

                    questRvAdapter = QuestRvAdapter(
                        { position -> showModifyQuestDialog(position) },
                        { position -> showDoneQuestDialog(position) },
                        false,
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

    private fun showAddQuestDialog(selectedDate: String, expList: List<GetMonthExpInfoItem>) {
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

        // Modify 버튼 클릭 리스너
        binding.btnModify.setOnClickListener {
            val newQuestDesc = binding.etQuestDesc.text.toString().trim()

            if (newQuestDesc.isNotEmpty()) {
                filteredQuests[position].desc = newQuestDesc
                updateUI()
            }

            dialog.dismiss()
        }

        binding.btnDelete.setOnClickListener {
            val questToDelete = questList[position]
            questList.remove(questToDelete)

            updateUI()
            dialog.dismiss()
        }

        dialog.show()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun updateUI() {
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.KOREAN).format(Date())
        filteredQuests = filteredQuests.filter { quest -> quest.date == today }.toMutableList()
//        questRvAdapter.setData(filteredQuests)
        questRvAdapter.notifyDataSetChanged()
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}