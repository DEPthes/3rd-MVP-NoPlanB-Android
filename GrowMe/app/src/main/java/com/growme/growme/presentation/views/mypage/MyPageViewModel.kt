package com.growme.growme.presentation.views.mypage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.growme.growme.data.LoggerUtils
import com.growme.growme.data.repository.CharacterRepositoryImpl
import com.growme.growme.data.repository.DataStoreRepositoryImpl
import com.growme.growme.data.repository.QuestRepositoryImpl
import com.growme.growme.data.repository.UserRepositoryImpl
import com.growme.growme.domain.model.MessageInfo
import com.growme.growme.domain.model.character.MyPageInfo
import com.growme.growme.domain.model.home.HomeExpInfo
import com.growme.growme.presentation.UiState
import kotlinx.coroutines.launch

class MyPageViewModel : ViewModel() {
    private val characterRepositoryImpl = CharacterRepositoryImpl()
    private val questRepositoryImpl = QuestRepositoryImpl()
    private val userRepositoryImpl = UserRepositoryImpl()
    private val dataStoreRepositoryImpl = DataStoreRepositoryImpl()

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

    private val _settingState = MutableLiveData<UiState<MessageInfo>>()
    val settingState: LiveData<UiState<MessageInfo>> get() = _settingState

    fun fetchSettingInfo() {
        _settingState.value = UiState.Loading

        viewModelScope.launch {
            userRepositoryImpl.getUserEmail()
                .onSuccess {
                    _settingState.value = UiState.Success(it)
                }
                .onFailure {
                    _settingState.value = UiState.Failure(it.message)
                }
        }
    }

    private var _expState = MutableLiveData<UiState<HomeExpInfo>>(UiState.Loading)
    val expState get() = _expState

    fun fetchExpInfo() {
        _expState.value = UiState.Loading

        viewModelScope.launch {
            questRepositoryImpl.fetchHomeData()
                .onSuccess {
                    val homeInfo = HomeExpInfo(
                        level = it.level,
                        acquireExp = it.acquireExp,
                        needExp = it.needExp,
                        todayExp = it.todayExp,
                        totQuestExp = it.totQuestExp
                    )
                    _expState.value = UiState.Success(homeInfo)
                }
                .onFailure {
                    _expState.value = UiState.Failure(it.message)
                }
        }
    }

    private var _withdrawState = MutableLiveData<UiState<String>>(UiState.Loading)
    val withdrawState get() = _withdrawState

    fun withdraw() {
        _withdrawState.value = UiState.Loading

        viewModelScope.launch {
            userRepositoryImpl.withdraw()
                .onSuccess {
                    _withdrawState.value = UiState.Success(it.msg)
                }
                .onFailure {
                    _withdrawState.value = UiState.Failure(it.message)
                }
        }
    }

    private val _clearState = MutableLiveData<UiState<Boolean>>(UiState.Loading)
    val clearState: LiveData<UiState<Boolean>> get() = _clearState

    fun clearData() {
        _clearState.value = UiState.Loading

        viewModelScope.launch {
            try {
                dataStoreRepositoryImpl.clearData()
                    .onSuccess {
                        _clearState.value = UiState.Success(it)
                    }
                    .onFailure { e ->
                        LoggerUtils.e("Clear User Data failed: ${e.message}")
                        _clearState.value = UiState.Failure(e.message)
                    }
            } catch (e: Exception) {
                LoggerUtils.e("Clear User Data: ${e.message}")
                _clearState.value = UiState.Failure(e.message)
            }
        }
    }
}