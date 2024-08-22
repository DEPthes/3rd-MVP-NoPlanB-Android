package com.growme.growme.presentation.views.characterSetting

import android.content.Intent
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

    private var _makeInitCharacterState = MutableLiveData<UiState<Intent>>(UiState.Loading)
    val makeInitCharacterState: LiveData<UiState<Intent>> get() = _makeInitCharacterState

    fun makeInitCharacter(characterName: String, intent: Intent, itemIdList: List<Int>) {
        _makeInitCharacterState.value = UiState.Loading

        viewModelScope.launch {
            characterRepositoryImpl.makeInitCharacter(characterName, itemIdList).onSuccess {
                _makeInitCharacterState.value = UiState.Success(intent)
            }.onFailure {
                _makeInitCharacterState.value = UiState.Failure(it.message)
            }
        }
    }
}