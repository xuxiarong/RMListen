package com.rm.module_home.viewmodel

import androidx.lifecycle.MutableLiveData
import com.rm.baselisten.viewmodel.BaseNetViewModel
import com.rm.module_home.model.HomeDetailModel

class HomeDetailViewModel : BaseNetViewModel(){
    var detailbean = MutableLiveData<List<HomeDetailModel>>()
}