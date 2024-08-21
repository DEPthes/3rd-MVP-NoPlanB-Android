package com.growme.growme.presentation.views.calendar

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.growme.growme.data.repository.CalendarRepositoryImpl
import com.growme.growme.data.repository.QuestRepositoryImpl
import com.growme.growme.domain.model.MessageInfo
import com.growme.growme.domain.model.calendar.GetMonthExpInfoItem
import com.growme.growme.domain.model.quest.CompleteQuestInfo
import com.growme.growme.domain.model.quest.QuestInfo
import com.growme.growme.presentation.UiState
import kotlinx.coroutines.launch

class CalendarViewModel : ViewModel() {
    private val calendarRepositoryImpl = CalendarRepositoryImpl()
    private val questRepositoryImpl = QuestRepositoryImpl()

    private val _addFutureState = MutableLiveData<UiState<String>>()
    val addFutureState: LiveData<UiState<String>> get() = _addFutureState

    fun addFutureQuest(date: String, contents: String, exp: Int) {
        _addFutureState.value = UiState.Loading

        viewModelScope.launch {
            calendarRepositoryImpl.addFutureQuest(date, contents, exp)
                .onSuccess {
                    _addFutureState.value = UiState.Success(it)
                }
                .onFailure {
                    _addFutureState.value = UiState.Failure(it.message)
                }
        }
    }

    private val _addState = MutableLiveData<UiState<MessageInfo>>()
    val addState: LiveData<UiState<MessageInfo>> get() = _addState

    fun addQuest(contents: String, exp: Int) {
        _addState.value = UiState.Loading

        viewModelScope.launch {
            questRepositoryImpl.addQuest(contents, exp)
                .onSuccess {
                    _addState.value = UiState.Success(it)
                }
                .onFailure {
                    _addState.value = UiState.Failure(it.message)
                }
        }
    }

    private val _updateState = MutableLiveData<UiState<MessageInfo>>()
    val updateState: LiveData<UiState<MessageInfo>> get() = _updateState

    fun updateQuest(id: Int, contents: String) {
        _updateState.value = UiState.Loading

        viewModelScope.launch {
            questRepositoryImpl.updateQuest(id, contents)
                .onSuccess {
                    _updateState.value = UiState.Success(it)
                }
                .onFailure {
                    _updateState.value = UiState.Failure(it.message)
                }
        }
    }

    private val _monthExpState = MutableLiveData<UiState<List<GetMonthExpInfoItem>>>()
    val monthExpState: LiveData<UiState<List<GetMonthExpInfoItem>>> get() = _monthExpState

    fun getMonthExp(month: String) {
        _monthExpState.value = UiState.Loading

        viewModelScope.launch {
            calendarRepositoryImpl.getCalendarExp(month)
                .onSuccess {
                    _monthExpState.value = UiState.Success(it)
                }
                .onFailure {
                    _monthExpState.value = UiState.Failure(it.message)
                }
        }
    }

    private val _questState = MutableLiveData<UiState<List<QuestInfo>>>()
    val questState: LiveData<UiState<List<QuestInfo>>> get() = _questState

    fun getQuestInfo(date: String) {
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

    private val _deleteState = MutableLiveData<UiState<MessageInfo>>()
    val deleteState: LiveData<UiState<MessageInfo>> get() = _deleteState

    fun deleteQuest(id: Int) {
        _deleteState.value = UiState.Loading

        viewModelScope.launch {
            questRepositoryImpl.deleteQuest(id)
                .onSuccess {
                    _deleteState.value = UiState.Success(it)
                }
                .onFailure {
                    _deleteState.value = UiState.Failure(it.message)
                }
        }
    }

    private val _completeState = MutableLiveData<UiState<CompleteQuestInfo>>()
    val completeState: LiveData<UiState<CompleteQuestInfo>> get() = _completeState

    fun completeQuest(id: Int) {
        _completeState.value = UiState.Loading

        viewModelScope.launch {
            questRepositoryImpl.completeQuest(id)
                .onSuccess {
                    _completeState.value = UiState.Success(it)
                }
                .onFailure {
                    _completeState.value = UiState.Failure(it.message)
                }
        }
    }
}