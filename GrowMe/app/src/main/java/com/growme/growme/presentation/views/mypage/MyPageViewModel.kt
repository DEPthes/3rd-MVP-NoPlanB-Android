package com.growme.growme.presentation.views.mypage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.growme.growme.data.model.MyInfo
import com.growme.growme.data.repository.CharacterRepositoryImpl
import com.growme.growme.domain.model.MessageInfo
import com.growme.growme.domain.model.character.MyPageInfo
import com.growme.growme.presentation.UiState
import kotlinx.coroutines.launch

class MyPageViewModel : ViewModel() {
    private val characterRepositoryImpl = CharacterRepositoryImpl()

    private val _fetchInfo = MutableLiveData<UiState<MyPageInfo>>()
    val fetchInfo: LiveData<UiState<MyPageInfo>> get() = _fetchInfo

    fun fetchCharacterInfo() {
        _fetchInfo.value = UiState.Loading

        viewModelScope.launch {
            characterRepositoryImpl.getCharacterInfo()
                .onSuccess {
                    _fetchInfo.value = UiState.Success(it)
                }
                .onFailure {
                    _fetchInfo.value = UiState.Failure(it.message)
                }
        }
    }

}