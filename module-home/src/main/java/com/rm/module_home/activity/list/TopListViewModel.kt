package com.rm.module_home.activity.list

import androidx.lifecycle.MutableLiveData
import com.rm.baselisten.viewmodel.BaseNetViewModel
import com.rm.module_home.bean.CategoryTabBean
import com.rm.module_home.bean.TopListBean
import com.rm.module_home.repository.TopListRepository

/**
 * desc   :
 * date   : 2020/08/04
 * version: 1.0
 */
class TopListViewModel(private val repository: TopListRepository) : BaseNetViewModel() {
    var tabInfoList = MutableLiveData<MutableList<CategoryTabBean>>()

    // 榜单列表数据
    var dataList = MutableLiveData<MutableList<TopListBean>>()

    fun getTabInfo() {
        tabInfoList.value = repository.getTabList()
    }

    fun getListInfo() {
        dataList.value = repository.getDataList()
    }
}