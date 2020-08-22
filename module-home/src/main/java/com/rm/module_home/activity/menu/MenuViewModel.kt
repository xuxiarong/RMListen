package com.rm.module_home.activity.menu

import androidx.lifecycle.MutableLiveData
import com.rm.baselisten.viewmodel.BaseNetViewModel
import com.rm.business_lib.xbanner.BannerInfo
import com.rm.module_home.bean.MenuItemBean
import com.rm.module_home.repository.MenuRepository

/**
 * desc   :
 * date   : 2020/08/04
 * version: 1.0
 */
class MenuViewModel(private val repository: MenuRepository) : BaseNetViewModel() {
    var bannerInfoList = MutableLiveData<List<BannerInfo>>()

    // 听单列表数据
    var menuList = MutableLiveData<MutableList<MenuItemBean>>()

    fun getMenuBanner() {
        bannerInfoList.value = repository.getMenuBanner()
    }

    fun getMenuListInfo() {
        menuList.value = repository.getMenuListInfo()
    }
}