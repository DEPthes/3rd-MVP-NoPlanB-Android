package com.growme.growme.presentation.views.item

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.growme.growme.data.model.Item
import com.growme.growme.databinding.ItemInventoryBinding

class ItemRvAdapter(private val context: Context) :
    RecyclerView.Adapter<ItemRvAdapter.ActivityViewHolder>() {
    private var dataList = mutableListOf<Item>()

    inner class ActivityViewHolder(private val binding: ItemInventoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: Item) {
            Glide.with(binding.root.context)
                .load(data.face)
                .override(100, 100)
                .skipMemoryCache(true)
                .dontAnimate()
                .into(binding.ivItem)
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
    fun setData(newList: List<Item>) {
        dataList = newList.toMutableList()
        notifyDataSetChanged()
    }

    interface OnItemClickListener {
        fun onClick(isFull: Boolean)
    }

    private lateinit var itemClickListener: OnItemClickListener

    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }
}