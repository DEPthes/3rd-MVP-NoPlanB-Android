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
import androidx.recyclerview.widget.LinearLayoutManager
import com.growme.growme.data.model.MonthExp
import com.growme.growme.data.model.Quest
import com.growme.growme.databinding.DialogAddQuestBinding
import com.growme.growme.databinding.DialogDoneQuestBinding
import com.growme.growme.databinding.DialogModifyQuestBinding
import com.growme.growme.databinding.FragmentCalendarBinding
import com.growme.growme.presentation.views.home.QuestRvAdapter
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class CalendarFragment : Fragment(), MonthAdapter.OnDateSelectedListener {
    private lateinit var binding: FragmentCalendarBinding
    private lateinit var monthAdapter: MonthAdapter
    private lateinit var monthListManager: LinearLayoutManager
    private lateinit var questRvAdpater: QuestRvAdapter
    private val calendar = Calendar.getInstance()
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

    private val monthExpList = mutableListOf(
        MonthExp("2024-08-01", 10),
        MonthExp("2024-08-02", 8),
        MonthExp("2024-08-03", 5),
        MonthExp("2024-08-04", 2),
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCalendarBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setTodayQuestRv()

        binding.ivPrevMonth.setOnClickListener {
            updateMonth(-1)
            monthAdapter.updateCurrentMonth(-1)

        }
        binding.ivNextMonth.setOnClickListener {
            updateMonth(1)
            monthAdapter.updateCurrentMonth(1)
        }

        initListener()
        setTodayDate()
        updateMonthDisplay()

        binding.ivAddQuest.setOnClickListener {
            showAddQuestDialog()
        }
    }

    private fun initListener() {
        val today = Calendar.getInstance().time
        monthAdapter = MonthAdapter(0, today, monthExpList)
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

    private fun setTodayQuestRv() {
        questRvAdpater = QuestRvAdapter(
            { position -> showModifyQuestDialog(position) },
            { position -> showDoneQuestDialog(position) },
            true
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

    private fun updateMonth(monthChange: Int) {
        calendar.add(Calendar.MONTH, monthChange)
        updateMonthDisplay()
    }

    private fun updateMonthDisplay() {
        val dateFormat = SimpleDateFormat("yyyy년 M월", Locale.KOREAN)
        val formattedDate = dateFormat.format(calendar.time)
        binding.tvSelectMonth.text = formattedDate
    }

    private fun setTodayDate() {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("yyyy년 M월", Locale.KOREAN)
        val formattedDate = dateFormat.format(calendar.time)

        binding.tvSelectMonth.text = formattedDate
    }

    override fun onDateSelected(date: Date) {
        monthAdapter.updateSelectedDate(date)
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.KOREAN)
        val selectedDate = dateFormat.format(date)
        val currentDate = dateFormat.format(Date())

        val filteredQuests = questList.filter { quest ->
            quest.date == selectedDate
        }

        questRvAdpater.setData(filteredQuests)
        binding.ivAddQuest.visibility = if (selectedDate < currentDate) View.GONE else View.VISIBLE
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
            val questToDelete = filteredQuests[position]
            filteredQuests.removeAt(position)
            questList.remove(questToDelete)

            updateUI()
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun updateUI() {
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.KOREAN).format(Date())
        filteredQuests = filteredQuests.filter { quest -> quest.date == today }.toMutableList()
        questRvAdpater.setData(filteredQuests)
        questRvAdpater.notifyDataSetChanged()
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
}