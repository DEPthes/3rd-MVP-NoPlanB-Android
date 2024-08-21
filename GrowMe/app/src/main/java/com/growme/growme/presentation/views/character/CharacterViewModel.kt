package com.growme.growme.presentation.views.character

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.growme.growme.data.model.character.MyCharacterEquipItemDetailReq
import com.growme.growme.data.repository.ItemChangeRepositoryImpl
import com.growme.growme.domain.model.MessageInfo
import com.growme.growme.presentation.base.UiState
import kotlinx.coroutines.launch

class CharacterViewModel : ViewModel() {
    private val itemChangeRepositoryImpl = ItemChangeRepositoryImpl()

    private var _itemChangeState = MutableLiveData<UiState<MessageInfo>>(UiState.Loading)
    val itemChangeState : LiveData<UiState<MessageInfo>> get() = _itemChangeState

    fun changeItemInfo(list: List<MyCharacterEquipItemDetailReq>) {
        _itemChangeState.value = UiState.Loading

        viewModelScope.launch {
            itemChangeRepositoryImpl.changeItem(list).onSuccess {
                _itemChangeState.value = UiState.Success(it)
            }.onFailure {
                _itemChangeState.value = UiState.Failure(it.message)
            }
        }
    }
}