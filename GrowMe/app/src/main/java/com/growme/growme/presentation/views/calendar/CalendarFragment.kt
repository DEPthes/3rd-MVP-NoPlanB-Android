package com.growme.growme.presentation.views.calendar

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
import com.growme.growme.data.model.TodayQuest
import com.growme.growme.databinding.DialogModifyQuestBinding
import com.growme.growme.databinding.FragmentCalendarBinding
import com.growme.growme.presentation.views.home.TodayQuestAdapter
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class CalendarFragment : Fragment(), MonthAdapter.OnDateSelectedListener {
    private lateinit var binding: FragmentCalendarBinding
    private lateinit var monthAdapter: MonthAdapter
    private lateinit var monthListManager: LinearLayoutManager
    private lateinit var todayQuestAdapter: TodayQuestAdapter
    private val calendar = Calendar.getInstance()

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
    }

    private fun initListener() {
        val today = Calendar.getInstance().time
        monthAdapter = MonthAdapter(0, today)
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
        val questList = listOf(
            TodayQuest("퀘스트 내용입니다1", 10, false),
            TodayQuest("퀘스트 내용입니다2", 8, false),
            TodayQuest("퀘스트 내용입니다3", 1, true),
            TodayQuest("퀘스트 내용입니다12", 3, true)
        )
        todayQuestAdapter = TodayQuestAdapter {
            showModifyQuestDialog()
        }
        todayQuestAdapter.setData(questList)

        binding.rvTodayQuest.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = todayQuestAdapter
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
    }

    private fun showModifyQuestDialog() {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        val binding = DialogModifyQuestBinding.inflate(LayoutInflater.from(requireContext()))
        dialog.setContentView(binding.root)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }
}