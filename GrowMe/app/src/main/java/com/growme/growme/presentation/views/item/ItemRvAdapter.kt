package com.growme.growme.presentation.views.item

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.growme.growme.R
import com.growme.growme.data.model.Item
import com.growme.growme.data.model.item.CategoryItem
import com.growme.growme.databinding.ItemInventoryBinding

class ItemRvAdapter(private val context: Context) :
    RecyclerView.Adapter<ItemRvAdapter.ActivityViewHolder>() {
    private var dataList = mutableListOf<CategoryItem>()

    inner class ActivityViewHolder(private val binding: ItemInventoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: CategoryItem) {
            // 해금 관련
            if (data.ableToEquip) {
                binding.clItemLockBg.visibility = View.GONE
                Glide.with(binding.root.context)
                    .load(data.itemImage)
                    .override(100, 100)
                    .skipMemoryCache(true)
                    .dontAnimate()
                    .into(binding.ivItem)
            }
            else {
                binding.clItemLockBg.visibility = View.VISIBLE
                binding.tvItemUnlockLv.text = "Lv ${data.requiredLevel} 해금"
                Glide.with(binding.root.context)
                    .load(data.itemImage)
                    .override(100, 100)
                    .skipMemoryCache(true)
                    .dontAnimate()
                    .into(binding.ivItem)
            }

            itemView.setOnClickListener {
                itemClickListener.onClick(data.itemImage, data.itemType)
                binding.ivItemBackground.setBackgroundResource(R.drawable.btn_mini_selected)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ItemRvAdapter.ActivityViewHolder {
        val binding =
            ItemInventoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ActivityViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemRvAdapter.ActivityViewHolder, position: Int) {
        holder.bind(dataList[position])
    }

    override fun getItemCount(): Int = dataList.size

    @SuppressLint("NotifyDataSetChanged")
    fun setData(newList: List<CategoryItem>) {
        dataList = newList.toMutableList()
        notifyDataSetChanged()
    }

    interface OnItemClickListener {
        fun onClick(itemImage: String, itemType: String)
    }

    private lateinit var itemClickListener: OnItemClickListener

    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }
}