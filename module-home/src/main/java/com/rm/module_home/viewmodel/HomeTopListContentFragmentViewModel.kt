package com.rm.module_home.viewmodel

import androidx.lifecycle.MutableLiveData
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.module_home.bean.TopListBean
import com.rm.module_home.repository.TopListRepository

/**
 * desc   :
 * date   : 2020/08/20
 * version: 1.0
 */
class HomeTopListContentFragmentViewModel(private val repository: TopListRepository) :
    BaseVMViewModel() {
    // 榜单列表数据
    var dataList = MutableLiveData<MutableList<TopListBean>>()

    fun getListInfo() {
        dataList.value = repository.getDataList()
    }

}