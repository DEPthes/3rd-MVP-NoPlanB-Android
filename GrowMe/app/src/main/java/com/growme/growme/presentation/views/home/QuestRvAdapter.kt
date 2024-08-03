package com.growme.growme.presentation.views.home

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.growme.growme.R
import com.growme.growme.data.model.Quest
import com.growme.growme.databinding.ItemQuestBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class QuestRvAdapter(
    private val onModifyClick: (position: Int) -> Unit,
    private val onDoneQuestClick: (position: Int) -> Unit,
    private val isCalendarFragment: Boolean
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var questList = mutableListOf<Quest>()

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

    fun setData(data: List<Quest>) {
        questList.clear()
        questList.addAll(data)
        notifyDataSetChanged()
    }

    @Suppress("DEPRECATION")
    inner class QuestHolder(val binding: ItemQuestBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(item: Quest) {
            binding.tvQuestDesc.text = item.desc
            binding.tvExp.text = "EXP ${item.exp}"

            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            // Quest 날짜와 오늘 날짜를 비교하기 위해 Calendar 사용
            val questDate = Calendar.getInstance().apply { time = dateFormat.parse(item.date)!! }
            val currentDate = Calendar.getInstance()

            // 시간 부분을 무시하고 날짜만 비교
            questDate.set(Calendar.HOUR_OF_DAY, 0)
            questDate.set(Calendar.MINUTE, 0)
            questDate.set(Calendar.SECOND, 0)
            questDate.set(Calendar.MILLISECOND, 0)

            currentDate.set(Calendar.HOUR_OF_DAY, 0)
            currentDate.set(Calendar.MINUTE, 0)
            currentDate.set(Calendar.SECOND, 0)
            currentDate.set(Calendar.MILLISECOND, 0)

            // 이전 날짜 퀘스트는 체크, 수정 삭제 안됨
            if (questDate.before(currentDate)) {
                binding.ivRadio.visibility = View.GONE
                binding.ivModify.visibility = View.GONE
            } else {
                binding.ivRadio.visibility = View.VISIBLE
                binding.ivModify.visibility = View.VISIBLE
            }

            if (item.finished) {
                binding.ivRadio.setImageResource(R.drawable.ic_radio_check)
                binding.root.setBackgroundResource(R.drawable.shape_rectangle_gray)
                binding.tvQuestDesc.alpha = 0.5f
                binding.ivModify.alpha = 0.5f
                binding.ivModify.isEnabled = false
            } else {
                binding.ivRadio.setImageResource(R.drawable.ic_circle)
                binding.ivModify.isEnabled = true
                binding.ivModify.alpha = 1.0f
            }

            // 퀘스트 완료 했을 때
            binding.ivRadio.setOnClickListener {
                if (!item.finished) {
                    onDoneQuestClick(position)
                }
                item.finished = !item.finished

                if (item.finished) {
                    binding.ivRadio.setImageResource(R.drawable.ic_radio_check)
                    binding.root.setBackgroundResource(R.drawable.shape_rectangle_gray)
                    binding.ivModify.isEnabled = false
                    binding.tvQuestDesc.alpha = 0.5f
                    binding.ivModify.alpha = 0.5f
                } else {
                    binding.ivRadio.setImageResource(R.drawable.ic_circle)
                    binding.tvQuestDesc.alpha = 1.0f
                    binding.ivModify.isEnabled = true
                    binding.ivModify.alpha = 1.0f
                }
            }

            if (isCalendarFragment) {
                binding.root.setBackgroundResource(R.drawable.shape_rectangle_black_calendar)
            } else {
                binding.root.setBackgroundResource(R.drawable.shape_rectangle_black)
            }

            binding.ivModify.setOnClickListener {
                // modify 클릭 시 위치 전달
                onModifyClick(adapterPosition)
            }
        }
    }
}