package com.rm.module_home.viewmodel

import androidx.lifecycle.MutableLiveData
import com.rm.baselisten.BaseApplication
import com.rm.baselisten.BaseApplication.Companion.CONTEXT
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.module_home.R
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
        list.add(CategoryTabBean(1, CONTEXT.getString(R.string.home_popular_list)))
        list.add(CategoryTabBean(3, CONTEXT.getString(R.string.home_new_book_list)))
//        list.add(CategoryTabBean(2, CONTEXT.getString(R.string.home_hot_list)))
//        list.add(CategoryTabBean(4, CONTEXT.getString(R.string.home_search_list)))
//        list.add(CategoryTabBean(5, CONTEXT.getString(R.string.home_praise_list)))
        return list
    }

    private fun getRankSeg(): MutableList<HomeRankSegBean> {
        val mutableListOf = mutableListOf<HomeRankSegBean>()
        mutableListOf.add(HomeRankSegBean(CONTEXT.getString(R.string.home_top_list_week), "week"))
        mutableListOf.add(HomeRankSegBean(CONTEXT.getString(R.string.home_top_list_month), "month"))
        mutableListOf.add(HomeRankSegBean(CONTEXT.getString(R.string.home_top_list_all), "all"))
        return mutableListOf
    }

}