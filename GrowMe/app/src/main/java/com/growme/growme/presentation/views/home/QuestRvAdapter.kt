package com.growme.growme.presentation.views.home

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.growme.growme.R
import com.growme.growme.databinding.ItemQuestBinding
import com.growme.growme.domain.model.quest.QuestInfo
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class QuestRvAdapter(
    private val onModifyClick: (position: Int) -> Unit,
    private val onDoneQuestClick: (position: Int) -> Unit,
    private val isCalendarFragment: Boolean,
    private val selectedDate: String,
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var questList = mutableListOf<QuestInfo>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ItemQuestBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return QuestHolder(binding)
    }

    override fun getItemCount(): Int {
        return questList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is QuestHolder) {
            holder.bind(questList[position])
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: List<QuestInfo>) {
        questList.clear()
        questList.addAll(data)
        notifyDataSetChanged()
    }

    @Suppress("DEPRECATION")
    inner class QuestHolder(val binding: ItemQuestBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(item: QuestInfo) {
            binding.tvQuestDesc.text = item.contents
            binding.tvExp.text = "EXP ${item.exp}"

            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val questDate = dateFormat.parse(selectedDate)
            val currentDate = dateFormat.parse(dateFormat.format(Date()))

            // 오늘 이전 날짜 퀘스트는 체크, 수정 삭제 안됨
            if (questDate!!.before(currentDate)) {
                binding.ivRadio.visibility = View.GONE
                binding.ivModify.visibility = View.GONE
            } else if (questDate.after(currentDate)) {
                // 오늘 이후 날짜 퀘스트는 체크만 안됨
                binding.ivRadio.visibility = View.GONE
                binding.ivModify.visibility = View.VISIBLE
            } else {
                binding.ivRadio.visibility = View.VISIBLE
                binding.ivModify.visibility = View.VISIBLE
            }

            if (item.isComplete) {
                binding.ivRadio.alpha = 0.0f

                if (isCalendarFragment) {
                    binding.root.setBackgroundResource(R.drawable.shape_rectangle_gray_calendar)
                } else {
                    binding.root.setBackgroundResource(R.drawable.shape_rectangle_gray)
                }
                binding.ivRadio.setImageResource(R.drawable.ic_radio_check)
                binding.tvQuestDesc.alpha = 0.5f
                binding.ivModify.alpha = 0.5f
                binding.ivModify.isEnabled = false
            } else {
                if (isCalendarFragment) {
                    binding.root.setBackgroundResource(R.drawable.shape_rectangle_black_calendar)
                } else {
                    binding.root.setBackgroundResource(R.drawable.shape_rectangle_black)
                }
                binding.ivRadio.setImageResource(R.drawable.ic_circle)
                binding.ivModify.isEnabled = true
                binding.ivModify.alpha = 1.0f
            }

            // 퀘스트 완료 했을 때
            binding.ivRadio.setOnClickListener {
                onDoneQuestClick(position)

//                if (item.isComplete) {
//                    if (isCalendarFragment) {
//                        binding.root.setBackgroundResource(R.drawable.shape_rectangle_gray_calendar)
//                    } else {
//                        binding.root.setBackgroundResource(R.drawable.shape_rectangle_gray)
//                    }
//                    binding.ivRadio.setImageResource(R.drawable.ic_radio_check)
//                    binding.ivModify.isEnabled = false
//                    binding.tvQuestDesc.alpha = 0.5f
//                    binding.ivModify.alpha = 0.5f
//                } else {
//                    if (isCalendarFragment) {
//                        binding.root.setBackgroundResource(R.drawable.shape_rectangle_black_calendar)
//                    } else {
//                        binding.root.setBackgroundResource(R.drawable.shape_rectangle_black)
//                    }
//                    binding.ivRadio.setImageResource(R.drawable.ic_circle)
//                    binding.tvQuestDesc.alpha = 1.0f
//                    binding.ivModify.isEnabled = true
//                    binding.ivModify.alpha = 1.0f
//                }
            }

            binding.ivModify.setOnClickListener {
                // modify 클릭 시 위치 전달
                onModifyClick(position)
            }
        }
    }
}