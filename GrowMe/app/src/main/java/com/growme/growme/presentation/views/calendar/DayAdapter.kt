package com.growme.growme.presentation.views.calendar

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.growme.growme.R
import com.growme.growme.databinding.ItemDayBinding
import com.growme.growme.domain.model.calendar.GetMonthExpInfoItem
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class DayAdapter(
    private val currentMonth: Int,
    private val dayList: MutableList<Date>,
    private val onDateSelected: (Date) -> Unit,
    private val monthExpList: List<GetMonthExpInfoItem>,
) :
    RecyclerView.Adapter<DayAdapter.DayView>() {
    private val ROW = 6
    private var diaryDates: Set<String> = emptySet()
    private var selectedDate: Date? = null
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.KOREAN)

    inner class DayView(val binding: ItemDayBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayView {
        val binding = ItemDayBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DayView(binding)
    }

    @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
    override fun onBindViewHolder(holder: DayView, position: Int) {
        with(holder.binding) {
            itemDayLayout.setOnClickListener {
                val selectedDate = dayList[position]
                onDateSelected(selectedDate)
            }

            val calendar = Calendar.getInstance()
            calendar.time = dayList[position]
            val dayMonth = calendar.get(Calendar.MONTH) + 1
            tvDay.text = calendar.get(Calendar.DAY_OF_MONTH).toString()

            val isSelectedDate = isSameDate(dayList[position], selectedDate)
            val isToday = isSameDate(dayList[position], Date())

            if (currentMonth != dayMonth) {
                // 다른 달의 날짜 안 보이게 표시
                tvDay.alpha = 0.0f
                ivSelectDate.alpha = 0.0f
                ivPotion.visibility = View.GONE
                tvDateExp.visibility = View.GONE
            } else {
                tvDay.alpha = 1.0f

                // 선택된 날짜 칠하기 (케이스 4개 나눔 -> 선택된 날짜인지 아닌지 / 오늘 날짜인지 아닌지)
                if (isToday && isSelectedDate) {
                    tvDay.setTextColor(Color.WHITE)
                    ivSelectDate.setBackgroundResource(R.drawable.shape_black_circle)
                } else if (isToday) {
                    ivSelectDate.setBackgroundResource(R.drawable.shape_gray_circle)
                } else if (isSelectedDate) {
                    tvDay.setTextColor(Color.WHITE)
                    ivSelectDate.setBackgroundResource(R.drawable.shape_black_circle)
                } else {
                    ivSelectDate.setBackgroundResource(R.drawable.shape_white_circle)
                    tvDay.setTextColor(Color.BLACK)
                }

                ivPotion.visibility = View.VISIBLE
                tvDateExp.visibility = View.VISIBLE

                itemDayLayout.setOnClickListener {
                    selectedDate = calendar.time
                    notifyDataSetChanged() // 모든 항목 다시 그리기
                    onDateSelected(calendar.time)
                }

                val dateString = dateFormat.format(calendar.time)
                val expForDate = monthExpList.find { it.date == dateString }?.exp ?: ""

                ivPotion.setImageResource(
                    when (expForDate) {
                        in 1..3 -> R.drawable.ic_potion_blue
                        in 4..6 -> R.drawable.ic_potion_green
                        in 7..9 -> R.drawable.ic_potion_orange
                        10 -> R.drawable.ic_potion_red
                        else -> R.drawable.ic_potion
                    }
                )
                tvDateExp.text = "$expForDate"

            }
        }
    }

    override fun getItemCount(): Int {
        return ROW * 7
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setDiaryDates(diaryDates: Set<String>) {
        this.diaryDates = diaryDates
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setSelectedDate(date: Date?) {
        selectedDate = date
        notifyDataSetChanged()
    }

    private fun isSameDate(date1: Date?, date2: Date?): Boolean {
        if (date1 == null || date2 == null) return false
        val cal1 = Calendar.getInstance().apply { time = date1 }
        val cal2 = Calendar.getInstance().apply { time = date2 }
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH) &&
                cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH)
    }
}
