package com.growme.growme.presentation.views.calendar

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.growme.growme.data.repository.CalendarRepositoryImpl
import com.growme.growme.data.repository.QuestRepositoryImpl
import com.growme.growme.data.repository.UserRepositoryImpl
import com.growme.growme.domain.model.calendar.GetMonthExpInfoItem
import com.growme.growme.domain.model.character.MyPageInfo
import com.growme.growme.domain.model.quest.QuestInfo
import com.growme.growme.presentation.UiState
import kotlinx.coroutines.launch

class CalendarViewModel : ViewModel() {
    private val calendarRepositoryImpl = CalendarRepositoryImpl()
    private val questRepositoryImpl = QuestRepositoryImpl()

    private val _addState = MutableLiveData<UiState<String>>()
    val addState: LiveData<UiState<String>> get() = _addState

    fun addFutureQuest(date: String, contents: String, exp: Int) {
        _addState.value = UiState.Loading

        viewModelScope.launch {
            calendarRepositoryImpl.addFutureQuest(date, contents, exp)
                .onSuccess {
                    _addState.value = UiState.Success(it)
                }
                .onFailure {
                    _addState.value = UiState.Failure(it.message)
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
}