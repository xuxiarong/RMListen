package com.rm.module_home.activity.menu

import androidx.lifecycle.MutableLiveData
import com.rm.baselisten.net.checkResult
import com.rm.baselisten.util.DLog
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.business_lib.bean.BannerInfoBean
import com.rm.module_home.bean.MenuSheetBean
import com.rm.module_home.repository.MenuRepository

/**
 * desc   :
 * date   : 2020/08/04
 * version: 1.0
 */
class MenuViewModel(private val repository: MenuRepository) : BaseVMViewModel() {
    // 听单列表数据
    var menuList = MutableLiveData<MenuSheetBean>()


    fun getMenuListInfo() {
        launchOnIO {
            repository.sheet().checkResult(
                onSuccess = {
                    menuList.value = it
                },
                onError = {
                    DLog.i("------>", "$it")
                }
            )
        }
    }
}