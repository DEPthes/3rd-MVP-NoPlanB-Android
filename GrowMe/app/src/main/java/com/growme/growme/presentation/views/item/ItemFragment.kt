package com.growme.growme.presentation.views.item

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.tabs.TabLayout
import com.growme.growme.R
import com.growme.growme.data.LoggerUtils
import com.growme.growme.data.model.Item
import com.growme.growme.databinding.FragmentItemBinding
import com.growme.growme.presentation.UiState

class ItemFragment : Fragment() {
    private lateinit var binding: FragmentItemBinding
    private lateinit var itemRvAdapter: ItemRvAdapter

    private val itemViewModel : ItemViewModel by viewModels()

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
        selectFirstTab()
        setItemRv()

        setObservers()
        itemViewModel.getHairInfo()
//        itemViewModel.getFashionInfo()
//        itemViewModel.getFaceInfo()
//        itemViewModel.getBackgroundInfo()
    }

    @SuppressLint("SetTextI18n")
    private fun setObservers() {
        itemViewModel.itemState.observe(viewLifecycleOwner) {
            when (it) {
                is UiState.Failure -> LoggerUtils.e("Item Data 조회 실패: ${it.error}")
                is UiState.Loading -> {}
                is UiState.Success -> {
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
        val itemFaceList = listOf(
            Item(R.drawable.face_1),
            Item(R.drawable.face_2),
            Item(R.drawable.face_3),
            Item(R.drawable.face_1),
            Item(R.drawable.face_2),
            Item(R.drawable.face_3),
            Item(R.drawable.face_1),
            Item(R.drawable.face_2),
            Item(R.drawable.face_3),
            Item(R.drawable.face_1)
        )

        val itemHairList = listOf(
            Item(R.drawable.hair_mini1),
            Item(R.drawable.hair_mini2),
            Item(R.drawable.hair_mini3),
            Item(R.drawable.hair_mini1),
            Item(R.drawable.hair_mini2),
            Item(R.drawable.hair_mini3),
            Item(R.drawable.hair_mini1),
            Item(R.drawable.hair_mini2),
            Item(R.drawable.hair_mini3),
            Item(R.drawable.hair_mini1)
        )

        val itemClothesList = listOf(
            Item(R.drawable.clothes_mini_1),
            Item(R.drawable.clothes_mini_2),
            Item(R.drawable.clothes_mini_1),
            Item(R.drawable.clothes_mini_2),
            Item(R.drawable.clothes_mini_1),
            Item(R.drawable.clothes_mini_2),
            Item(R.drawable.clothes_mini_1),
            Item(R.drawable.clothes_mini_2),
            Item(R.drawable.clothes_mini_1),
            Item(R.drawable.clothes_mini_2)
        )

        val itemBackgroundList = listOf(
            Item(R.drawable.background_1),
            Item(R.drawable.background_1),
            Item(R.drawable.background_1),
            Item(R.drawable.background_1),
            Item(R.drawable.background_1),
            Item(R.drawable.background_1),
            Item(R.drawable.background_1),
            Item(R.drawable.background_1),
            Item(R.drawable.background_1),
            Item(R.drawable.background_1)
        )

        when (tabIndex) {
            0 -> itemRvAdapter.setData(itemFaceList)
            1 -> itemRvAdapter.setData(itemHairList)
            2 -> itemRvAdapter.setData(itemClothesList)
            3 -> itemRvAdapter.setData(itemBackgroundList)
        }
    }
}