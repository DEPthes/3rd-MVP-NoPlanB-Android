package com.growme.growme.presentation.views.item

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.growme.growme.R
import com.growme.growme.data.LoggerUtils
import com.growme.growme.databinding.FragmentItemBinding
import com.growme.growme.presentation.UiState

class ItemFragment : Fragment() {
    private lateinit var binding: FragmentItemBinding
    private lateinit var itemRvAdapter: ItemRvAdapter

    private val itemViewModel : ItemViewModel by viewModels()

    private var tabNum = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentItemBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setTabLayout()
        // 첫번째 탭으로 초기화
        selectFirstTab()
        // 탭 누를 때마다 변경
        setItemRv()

        itemRvAdapter.apply {
            setItemClickListener(object : ItemRvAdapter.OnItemClickListener{
                override fun onClick(itemImage: String, itemType: String) {
                    when (tabNum) {
                        0 -> Glide.with(binding.root.context)
                            .load(itemImage)
                            .override(123.dpToPx(), 159.dpToPx())
                            .skipMemoryCache(true)
                            .dontAnimate()
                            .into(binding.ivHair)
                        1 -> {
                            if (itemType == "EYE")
                                Glide.with(binding.root.context)
                                    .load(itemImage)
                                    .override(75.dpToPx(), 33.dpToPx())
                                    .skipMemoryCache(true)
                                    .dontAnimate()
                                    .into(binding.ivFace)
                            else
                                Glide.with(binding.root.context)
                                    .load(itemImage)
                                    .override(105.dpToPx(), 216.dpToPx())
                                    .skipMemoryCache(true)
                                    .dontAnimate()
                                    .into(binding.ivCharacter)
                        }
                        2 -> {
                            if (itemType == "CLOTHES")
                                Glide.with(binding.root.context)
                                .load(itemImage)
                                .override(69.dpToPx(), 117.dpToPx())
                                .skipMemoryCache(true)
                                .dontAnimate()
                                .into(binding.ivClothes)
                            else if (itemType == "GLASSES")
                                Glide.with(binding.root.context)
                                .load(itemImage)
                                .override(75.dpToPx(), 39.dpToPx())
                                .skipMemoryCache(true)
                                .dontAnimate()
                                .into(binding.ivGlasses)
                            else
                                Glide.with(binding.root.context)
                                    .load(itemImage)
                                    .override(123.dpToPx(), 81.dpToPx())
                                    .skipMemoryCache(true)
                                    .dontAnimate()
                                    .into(binding.ivHat)
                        }
                        3 -> Glide.with(binding.root.context)
                            .load(itemImage)
                            .override(300.dpToPx(), 300.dpToPx())
                            .skipMemoryCache(true)
                            .dontAnimate()
                            .into(binding.ivBackground)
                    }
                }
            })
        }
        setObservers()
    }

    @SuppressLint("SetTextI18n")
    private fun setObservers() {
        itemViewModel.itemState.observe(viewLifecycleOwner) {
            when (it) {
                is UiState.Failure -> LoggerUtils.e("Item Data 조회 실패: ${it.error}")
                is UiState.Loading -> {}
                is UiState.Success -> {
                    itemRvAdapter.setData(it.data.categoryItemList)
                    LoggerUtils.d("Item Data 조회 성공")
                }
            }
        }
    }

    private fun setTabLayout() {
        binding.tlInventory.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {

            override fun onTabSelected(tab: TabLayout.Tab) {
                setTabView(tab.position)
                changeItemRv(tab.position)
                tabNum = tab.position
                Log.d("TAG", "2")
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                Log.d("TAG", "1")
            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })
    }

    private fun selectFirstTab() {
        val firstTab = binding.tlInventory.getTabAt(0)
        firstTab?.let {
            it.select()
            setTabView(0)
        }
    }

    private fun setTabView(pos: Int) {
        when(pos) {
            0 -> binding.tlInventory.setBackgroundResource(R.drawable.ic_item_navi_hair)
            1 -> binding.tlInventory.setBackgroundResource(R.drawable.ic_item_navi_face)
            2 -> binding.tlInventory.setBackgroundResource(R.drawable.ic_item_navi_fashion)
            3 -> binding.tlInventory.setBackgroundResource(R.drawable.ic_item_navi_background)
        }
    }

    private fun setItemRv() {
        val spacingInPixels = resources.getDimensionPixelSize(R.dimen.grid_spacing)

        itemRvAdapter = ItemRvAdapter(requireContext())
        binding.rvInventory.apply {
            adapter = itemRvAdapter
            addItemDecoration(
                GridSpacingItemDecoration(
                    spanCount = 3,
                    spacing = spacingInPixels,
                    includeEdge = true
                )
            )
        }
        changeItemRv(0)
    }

    private fun changeItemRv(tabIndex : Int) {
        when (tabIndex) {
            0 -> itemViewModel.getHairListInfo()
            1 -> itemViewModel.getFaceListInfo()
            2 -> itemViewModel.getFashionListInfo()
            3 -> itemViewModel.getBackgroundListInfo()
        }
    }

    private fun Int.dpToPx(): Int {
        val density = resources.displayMetrics.density
        return (this * density).toInt()
    }
}