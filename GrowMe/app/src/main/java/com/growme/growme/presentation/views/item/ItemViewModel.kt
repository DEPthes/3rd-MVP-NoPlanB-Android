package com.growme.growme.presentation.views.item

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.growme.growme.data.model.item.CategoryItem
import com.growme.growme.data.repository.ItemRepositoryImpl
import com.growme.growme.domain.model.ItemInfo
import com.growme.growme.presentation.UiState
import kotlinx.coroutines.launch

class ItemViewModel : ViewModel() {
    private val itemRepositoryImpl = ItemRepositoryImpl()

    private var _itemState = MutableLiveData<UiState<ItemInfo<List<CategoryItem>>>>(UiState.Loading)
    val itemState get() = _itemState

    fun getHairListInfo() {
        _itemState.value = UiState.Loading

        viewModelScope.launch {
            itemRepositoryImpl.getHairItems()
                .onSuccess {
                    val itemInfo = ItemInfo(
                        categoryItemList = it.categoryItemList
                    )
                    _itemState.value = UiState.Success(itemInfo)
                }
                .onFailure {
                    _itemState.value = UiState.Failure(it.message)
                }
        }
    }

    fun getFashionListInfo() {
        _itemState.value = UiState.Loading

        viewModelScope.launch {
            itemRepositoryImpl.getFashionItems()
                .onSuccess {
                    val itemInfo = ItemInfo(
                        categoryItemList = it.categoryItemList
                    )
                    _itemState.value = UiState.Success(itemInfo)
                }
                .onFailure {
                    _itemState.value = UiState.Failure(it.message)
                }
        }
    }

    fun getFaceListInfo() {
        _itemState.value = UiState.Loading

        viewModelScope.launch {
            itemRepositoryImpl.getFaceItems()
                .onSuccess {
                    val itemInfo = ItemInfo(
                        categoryItemList = it.categoryItemList
                    )
                    _itemState.value = UiState.Success(itemInfo)
                }
                .onFailure {
                    _itemState.value = UiState.Failure(it.message)
                }
        }
    }

    fun getBackgroundListInfo() {
        _itemState.value = UiState.Loading

        viewModelScope.launch {
            itemRepositoryImpl.getBackgroundItems()
                .onSuccess {
                    val itemInfo = ItemInfo(
                        categoryItemList = it.categoryItemList
                    )
                    _itemState.value = UiState.Success(itemInfo)
                }
                .onFailure {
                    _itemState.value = UiState.Failure(it.message)
                }
        }
    }
}