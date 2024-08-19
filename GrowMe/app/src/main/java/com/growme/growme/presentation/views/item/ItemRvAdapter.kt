package com.growme.growme.presentation.views.item

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.growme.growme.R
import com.growme.growme.data.model.item.CategoryItem
import com.growme.growme.databinding.ItemInventoryBinding

class ItemRvAdapter(private val context: Context) :
    RecyclerView.Adapter<ItemRvAdapter.ActivityViewHolder>() {

    private var dataList = mutableListOf<CategoryItem>()
    private var selectedPosition = RecyclerView.NO_POSITION  // 선택된 아이템의 위치를 추적하기 위한 변수

    inner class ActivityViewHolder(private val binding: ItemInventoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("NotifyDataSetChanged", "SetTextI18n")
        fun bind(data: CategoryItem, isSelected: Boolean) {
            binding.tvItemName.text = data.itemName
            // 해금 관련
            if (data.ableToEquip) {
                binding.clItemLockBg.visibility = View.GONE
                Glide.with(binding.root.context)
                    .load(data.itemImage)
                    .override(100, 100)
                    .skipMemoryCache(true)
                    .dontAnimate()
                    .into(binding.ivItem)
            } else {
                binding.clItemLockBg.visibility = View.VISIBLE
                binding.tvItemUnlockLv.text = "Lv${data.requiredLevel} 해금"
                Glide.with(binding.root.context)
                    .load(data.itemImage)
                    .override(100, 100)
                    .skipMemoryCache(true)
                    .dontAnimate()
                    .into(binding.ivItem)
            }

            // 아이템이 선택되었는지 여부에 따라 테두리를 설정
            if (isSelected) {
                binding.ivItemBackground.setBackgroundResource(R.drawable.btn_mini_selected)
            } else {
                binding.ivItemBackground.setBackgroundResource(R.drawable.btn_mini_default) // 기본 테두리
            }

            itemView.setOnClickListener {
                if (data.ableToEquip) {
                    itemClickListener.onClick(data.itemId, data.itemImage, data.itemType)
                    // 선택된 아이템 위치를 업데이트
                    selectedPosition = adapterPosition
                    notifyDataSetChanged()  // RecyclerView를 갱신하여 테두리 상태를 반영
                }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ItemRvAdapter.ActivityViewHolder {
        val binding =
            ItemInventoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ActivityViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemRvAdapter.ActivityViewHolder, position: Int) {
        holder.bind(dataList[position], position == selectedPosition)
    }

    override fun getItemCount(): Int = dataList.size

    @SuppressLint("NotifyDataSetChanged")
    fun setData(newList: List<CategoryItem>) {
        dataList = newList.toMutableList()
        notifyDataSetChanged()
        selectedPosition = RecyclerView.NO_POSITION  // 데이터가 변경되면 선택된 아이템 초기화
    }

    interface OnItemClickListener {
        fun onClick(itemId: Int, itemImage: String, itemType: String)
    }

    private lateinit var itemClickListener: OnItemClickListener

    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }
}
