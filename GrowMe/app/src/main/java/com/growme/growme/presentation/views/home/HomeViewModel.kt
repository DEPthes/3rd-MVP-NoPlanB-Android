package com.growme.growme.presentation.views.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.growme.growme.data.model.MyInfo
import com.growme.growme.data.model.Quest
import com.growme.growme.data.repository.CharacterRepositoryImpl
import com.growme.growme.data.repository.QuestRepositoryImpl
import com.growme.growme.domain.model.HomeExpInfo
import com.growme.growme.domain.model.QuestInfo
import com.growme.growme.domain.model.character.MyPageInfo
import com.growme.growme.presentation.UiState
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val characterRepositoryImpl = CharacterRepositoryImpl()
    private val questRepositoryImpl = QuestRepositoryImpl()

    private var _expState = MutableLiveData<UiState<HomeExpInfo>>(UiState.Loading)
    val expState get() = _expState

    private val _characterState = MutableLiveData<UiState<MyPageInfo>>()
    val characterState: LiveData<UiState<MyPageInfo>> get() = _characterState



    fun fetchExpInfo() {
        _expState.value = UiState.Loading

        viewModelScope.launch {
            questRepositoryImpl.fetchHomeData()
                .onSuccess {
                    val homeInfo = HomeExpInfo(
                        level = it.level,
                        acquireExp = it.acquireExp,
                        needExp = it.needExp,
                        todayExp = it.todayExp
                    )
                    _expState.value = UiState.Success(homeInfo)
                }
                .onFailure {
                    _expState.value = UiState.Failure(it.message)
                }
        }
    }

    fun fetchCharacterInfo() {
        _characterState.value = UiState.Loading

        viewModelScope.launch {
            characterRepositoryImpl.getCharacterInfo()
                .onSuccess {
                    _characterState.value = UiState.Success(it)
                }
                .onFailure {
                    _characterState.value = UiState.Failure(it.message)
                }
        }
    }

    private val _questState = MutableLiveData<UiState<List<QuestInfo>>>()
    val questState: LiveData<UiState<List<QuestInfo>>> get() = _questState

    fun fetchQuestInfo(date: String) {
        _questState.value = UiState.Loading

        viewModelScope.launch {
            questRepositoryImpl.getQuest(date)
                .onSuccess {
                    _questState.value = UiState.Success(it)
                }
                .onFailure {
                    _questState.value = UiState.Failure(it.message)
                }
        }
    }

}