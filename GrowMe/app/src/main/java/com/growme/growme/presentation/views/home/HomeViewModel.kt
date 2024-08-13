package com.growme.growme.presentation.views.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.growme.growme.data.model.MyInfo
import com.growme.growme.data.repository.QuestRepositoryImpl
import com.growme.growme.domain.model.HomeInfo
import com.growme.growme.presentation.UiState
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private var questRepositoryImpl = QuestRepositoryImpl()

    private var _uiState = MutableLiveData<UiState<HomeInfo>>(UiState.Loading)
    val uiState get() = _uiState

    private val _myInfo = MutableLiveData<MyInfo>()
    val myInfo: LiveData<MyInfo> get() = _myInfo

    fun fetchData(newInfo: MyInfo) {
        _myInfo.value = newInfo

    }

    fun fetchData(accessToken: String) {
        _uiState.value = UiState.Loading

        viewModelScope.launch {
            questRepositoryImpl.fetchHomeDate(
                accessToken
            )
                .onSuccess { it ->
//                    val result =
//
//                    val tmp = it.listIterator()
//                    while (tmp.hasNext()) {
//                        val postItem = tmp.next()
//                        result.addAll(postItem.recommendLinks.map {
//                            RealRecommendPost(
//                                link = it.link,
//                                title = it.title,
//                                categoryName = postItem.name
//                            )
//                        })
//                    }

                    _uiState.value = UiState.Success(it)
                }
                .onFailure {
                    _uiState.value = UiState.Failure(it.message)
                }
        }
    }

}