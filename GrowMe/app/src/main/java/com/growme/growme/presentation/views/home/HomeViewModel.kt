package com.growme.growme.presentation.views.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.growme.growme.data.model.MyInfo

class HomeViewModel: ViewModel() {

    private val _myInfo = MutableLiveData<MyInfo>()
    val myInfo: LiveData<MyInfo> get() = _myInfo

    fun fetchData(newInfo: MyInfo) {
        _myInfo.value = newInfo


//        _uiState.value = UiState.Loading
//        viewModelScope.launch {
//            repositoryImpl.getRecommendPost(
//                app.userPreferences.getAccessToken().getOrNull().orEmpty(), workspaceId
//            )
//                .onSuccess { it ->
//                    val result = mutableListOf<RealRecommendPost>()
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
//
//                    _uiState.value = UiState.Success(result)
//                }
//                .onFailure {
//                    _uiState.value = UiState.Failure(it.message)
//                }
//        }
    }

}