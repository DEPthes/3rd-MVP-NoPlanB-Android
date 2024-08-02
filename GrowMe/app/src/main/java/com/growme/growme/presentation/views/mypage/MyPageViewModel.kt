package com.growme.growme.presentation.views.mypage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.growme.growme.data.model.MyInfo

class MyPageViewModel : ViewModel() {
    private val _myInfo = MutableLiveData<MyInfo>()
    val myInfo: LiveData<MyInfo> get() = _myInfo

    fun fetchData(newInfo: MyInfo) {
        _myInfo.value = newInfo
    }
}