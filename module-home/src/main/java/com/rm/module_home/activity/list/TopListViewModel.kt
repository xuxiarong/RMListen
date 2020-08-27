package com.rm.module_home.activity.list

import androidx.lifecycle.MutableLiveData
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.module_home.bean.CategoryTabBean
import com.rm.module_home.repository.TopListRepository

/**
 * desc   :
 * date   : 2020/08/04
 * version: 1.0
 */
class TopListViewModel(private val repository: TopListRepository) : BaseVMViewModel() {
    var tabInfoList = MutableLiveData<MutableList<CategoryTabBean>>()

    fun getTabInfo() {
        tabInfoList.value = repository.getTabList()
    }

}