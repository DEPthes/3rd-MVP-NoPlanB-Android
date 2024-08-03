package com.growme.growme.presentation.views.home

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.growme.growme.R
import com.growme.growme.data.model.Quest
import com.growme.growme.databinding.ItemTodayQuestBinding

class QuestRvAdpater(
    private val onModifyClick: () -> Unit,
    private val isCalendarFragment: Boolean
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var questList = mutableListOf<Quest>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ItemTodayQuestBinding.inflate(
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
//        questList = list.toMutableList()
        questList.clear()
        questList.addAll(data)
        notifyDataSetChanged()
    }

    inner class QuestHolder(val binding: ItemTodayQuestBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(item: Quest) {
            binding.tvQuestDesc.text = item.desc
            binding.tvExp.text = "EXP ${item.exp}"
            if (item.finished) {
                binding.ivRadio.setImageResource(R.drawable.ic_radio_check)
            } else {
                binding.ivRadio.setImageResource(R.drawable.ic_circle)
            }

            if (isCalendarFragment) {
                binding.root.setBackgroundColor(
                    ContextCompat.getColor(
                        binding.root.context,
                        R.color.cream_ivory
                    )
                )
            } else {
                binding.root.setBackgroundColor(
                    ContextCompat.getColor(
                        binding.root.context,
                        R.color.white
                    )
                )
            }


            binding.ivModify.setOnClickListener {
                // 퀘스트 수정
                onModifyClick()
            }
        }
    }
}