package com.growme.growme.presentation.views.item

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import com.growme.growme.R
import com.growme.growme.data.model.Item
import com.growme.growme.databinding.FragmentItemBinding

class ItemFragment : Fragment() {
    private lateinit var binding: FragmentItemBinding
    private lateinit var itemRvAdapter: ItemRvAdapter

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
    }

    private fun setTabLayout() {
        binding.tlInventory.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {

            override fun onTabSelected(tab: TabLayout.Tab) {
                tab.view.setBackgroundResource(R.drawable.tab_item_selected)
                Log.d("TAG", "2")
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                tab.view.setBackgroundResource(R.drawable.tab_item_unselected)
                Log.d("TAG", "1")
            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })
    }

    private fun updateUnselectedTabs(selectedTab: TabLayout.Tab) {
        for (i in 0 until binding.tlInventory.tabCount) {
            val tab = binding.tlInventory.getTabAt(i)
            if (tab != null && tab != selectedTab) {
                Log.d("TAG", tab.toString())
                Log.d("TAG", selectedTab.toString())
                Log.d("TAG", binding.tlInventory.tabCount.toString())
                tab.view.setBackgroundResource(R.drawable.tab_item_unselected)
            }
        }
    }

    private fun selectFirstTab() {
        val firstTab = binding.tlInventory.getTabAt(0)
        firstTab?.let {
            it.select()
            it.view.setBackgroundResource(R.drawable.tab_item_selected)
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

        val itemList = listOf(
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
        itemRvAdapter.setData(itemList)
    }
}