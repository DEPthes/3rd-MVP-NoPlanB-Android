package com.growme.growme.presentation.views.characterSetting

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.growme.growme.data.repository.CharacterRepositoryImpl
import com.growme.growme.domain.model.MessageInfo
import com.growme.growme.presentation.UiState
import kotlinx.coroutines.launch

class CharacterSettingViewModel: ViewModel() {
    private val characterRepositoryImpl = CharacterRepositoryImpl()

    private var _makeInitCharacterState = MutableLiveData<UiState<MessageInfo>>(UiState.Loading)
    val makeInitCharacterState: LiveData<UiState<MessageInfo>> get() = _makeInitCharacterState

    fun makeInitCharacter(characterName: String, itemIdList: List<Int>) {
        _makeInitCharacterState.value = UiState.Loading

        viewModelScope.launch {
            characterRepositoryImpl.makeInitCharacter(characterName, itemIdList).onSuccess {
                _makeInitCharacterState.value = UiState.Success(it)
            }.onFailure {
                _makeInitCharacterState.value = UiState.Failure(it.message)
            }
        }
    }
}