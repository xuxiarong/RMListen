package com.rm.module_home.viewmodel

import androidx.lifecycle.MutableLiveData
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.module_home.model.home.detail.HomeDetailModel

class HomeDetailViewModel : BaseVMViewModel(){
    var detailbean = MutableLiveData<List<HomeDetailModel>>()
}