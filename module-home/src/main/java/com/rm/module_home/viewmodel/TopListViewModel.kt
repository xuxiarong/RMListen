package com.rm.module_home.viewmodel

import androidx.lifecycle.MutableLiveData
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.module_home.bean.CategoryTabBean
import com.rm.module_home.bean.HomeRankSegBean

/**
 * desc   :
 * date   : 2020/08/04
 * version: 1.0
 */
class TopListViewModel : BaseVMViewModel() {
    val tabInfoList = MutableLiveData<MutableList<CategoryTabBean>>()
    val rankSegList = MutableLiveData<MutableList<HomeRankSegBean>>()

    fun getData() {
        tabInfoList.value = getTabList()
        rankSegList.value = getRankSeg()
    }


    private fun getTabList(): MutableList<CategoryTabBean> {
        val list = mutableListOf<CategoryTabBean>()
        list.add(CategoryTabBean(1, "热门榜"))
        list.add(CategoryTabBean(2, "热销榜"))
        list.add(CategoryTabBean(3, "新书榜"))
        list.add(CategoryTabBean(4, "搜索榜"))
        list.add(CategoryTabBean(5, "好评榜"))
        return list
    }

    private fun getRankSeg(): MutableList<HomeRankSegBean> {
        val mutableListOf = mutableListOf<HomeRankSegBean>()
        mutableListOf.add(HomeRankSegBean("周榜", "week"))
        mutableListOf.add(HomeRankSegBean("月榜", "month"))
        mutableListOf.add(HomeRankSegBean("总榜", "all"))
        return mutableListOf
    }

}