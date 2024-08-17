package com.growme.growme.presentation.views.item

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.growme.growme.R
import com.growme.growme.data.LoggerUtils
import com.growme.growme.data.model.character.MyCharacterEquipItemDetailReq
import com.growme.growme.databinding.FragmentItemBinding
import com.growme.growme.domain.model.home.ItemData
import com.growme.growme.presentation.UiState
import com.growme.growme.presentation.views.character.CharacterViewModel
import com.growme.growme.presentation.views.mypage.MyPageViewModel
import java.util.logging.Logger

class ItemFragment : Fragment() {
    private lateinit var binding: FragmentItemBinding
    private lateinit var itemRvAdapter: ItemRvAdapter

    private val itemViewModel : ItemViewModel by viewModels()
    private val characterViewModel : CharacterViewModel by viewModels()
    private val myPageViewModel: MyPageViewModel by viewModels()

    private var tabNum = 0
    private var itemList = ArrayList<MyCharacterEquipItemDetailReq>()

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

        myPageViewModel.fetchCharacterInfo()
        setTabLayout()
        // 첫번째 탭으로 초기화
        selectFirstTab()
        // 탭 누를 때마다 변경
        setItemRv()

        itemRvAdapter.apply {
            setItemClickListener(object : ItemRvAdapter.OnItemClickListener {
                override fun onClick(itemId: Int, itemImage: String, itemType: String) {
                    val (targetView, width, height) = when (tabNum) {
                        0 -> Triple(binding.ivHair, 123.dpToPx(), 159.dpToPx())
                        1 -> when (itemType) {
                            "EYE" -> Triple(binding.ivFace, 75.dpToPx(), 33.dpToPx())
                            else -> Triple(binding.ivCharacter, 105.dpToPx(), 216.dpToPx())
                        }
                        2 -> when (itemType) {
                            "CLOTHES" -> Triple(binding.ivClothes, 69.dpToPx(), 117.dpToPx())
                            "GLASSES" -> Triple(binding.ivGlasses, 75.dpToPx(), 39.dpToPx())
                            else -> Triple(binding.ivHat, 123.dpToPx(), 81.dpToPx())
                        }
                        3 -> Triple(binding.ivBackground, 300.dpToPx(), 300.dpToPx())
                        else -> return
                    }
                    itemList.add(MyCharacterEquipItemDetailReq(itemType, itemId))
                    loadImage(itemImage, targetView, width, height)
                }
            })
        }

        binding.btnSave.setOnClickListener {
            characterViewModel.changeItemInfo(itemList)
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
        characterViewModel.itemChangeState.observe(viewLifecycleOwner) {
            when (it) {
                is UiState.Failure -> LoggerUtils.e("Item Change 저장 실패 : ${it.error}")
                is UiState.Loading -> {}
                is UiState.Success -> {
                    LoggerUtils.d("Item Change 저장 성공")
                }
            }
        }
        myPageViewModel.fetchInfo.observe(viewLifecycleOwner) {
            when (it) {
                is UiState.Failure -> LoggerUtils.e("Character Data 조회 실패: ${it.error}")
                is UiState.Loading -> {}
                is UiState.Success -> {
                    // 장착된 캐릭터 아이템 로드
                    val itemList = it.data.myCharaterDetailResList
                    itemList.forEach { item ->
                        handleItem(ItemData(item.itemType, item.itemImage))
                    }
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

    private fun loadImage(url: String, targetView: ImageView, width: Int, height: Int) {
        Glide.with(targetView.context)
            .load(url)
            .override(width, height)
            .skipMemoryCache(true)
            .dontAnimate()
            .into(targetView)
    }

    private fun handleItem(item: ItemData) {
        val (view, widthDp, heightDp) = when (item.itemType) {
            "FACECOLOR" -> Triple(binding.ivCharacter, 105.dpToPx(), 216.dpToPx())
            "EYE" -> Triple(binding.ivFace, 75.dpToPx(), 33.dpToPx())
            "CLOTHES" -> Triple(binding.ivClothes, 69.dpToPx(), 117.dpToPx())
            "HAIR" -> Triple(binding.ivHair, 123.dpToPx(), 159.dpToPx())
            "BACKGROUND" -> Triple(binding.ivBackground, 300.dpToPx(), 300.dpToPx())
            // 다른 itemType 추가 가능
            else -> return
        }

        loadImage(item.itemImage, view, widthDp, heightDp)
    }

    private fun Int.dpToPx(): Int {
        val density = resources.displayMetrics.density
        return (this * density).toInt()
    }
}