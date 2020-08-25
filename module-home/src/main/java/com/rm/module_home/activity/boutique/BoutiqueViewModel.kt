package com.rm.module_home.activity.boutique

import androidx.lifecycle.MutableLiveData
import com.rm.baselisten.viewmodel.BaseNetViewModel
import com.rm.module_home.bean.CategoryTabBean
import com.rm.module_home.repository.BoutiqueRepository

/**
 * desc   :
 * date   : 2020/08/24
 * version: 1.0
 */
class BoutiqueViewModel(private val repository: BoutiqueRepository) :
    BaseNetViewModel() {
    // 类别标签列表
    val tabList = MutableLiveData<List<CategoryTabBean>>()

    fun getTabListInfo(){
        tabList.value = repository.getTabList()
    }
}