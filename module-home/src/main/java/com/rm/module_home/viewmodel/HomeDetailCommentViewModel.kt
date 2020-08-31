package com.rm.module_home.viewmodel

import androidx.lifecycle.MutableLiveData
import com.rm.baselisten.mvvm.BaseViewModel
import com.rm.module_home.model.home.detail.HomeCommentViewModel

class HomeDetailCommentViewModel :BaseViewModel() {
    var commentViewModel = MutableLiveData<HomeCommentViewModel>()
}