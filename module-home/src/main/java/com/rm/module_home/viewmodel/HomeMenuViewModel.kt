package com.rm.module_home.viewmodel

import androidx.lifecycle.MutableLiveData
import com.rm.baselisten.net.checkResult
import com.rm.baselisten.util.DLog
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.business_lib.bean.*
import com.rm.module_home.bean.MenuSheetBean
import com.rm.module_home.repository.HomeMenuRepository

/**
 * desc   :
 * date   : 2020/08/04
 * version: 1.0
 */
class HomeMenuViewModel(private val repository: HomeMenuRepository) : BaseVMViewModel() {
    // 听单详情数据
    var menuList = MutableLiveData<MenuSheetBean>()

    //听单列表
    var sheetList = MutableLiveData<SheetListBean>()

    var itemClick: (SheetInfoBean) -> Unit = {}

    /**
     * 获取听单详情
     */
    fun getMenuListInfo() {
        showLoading()
        launchOnIO {
            repository.sheet().checkResult(
                onSuccess = {
                    showContentView()
                    menuList.value = it
                },
                onError = {
                    showContentView()
                    DLog.i("------>", "$it")
                }
            )
        }
    }

    /**
     * 获取听单列表
     */
    fun getSheetList( page: Int, pageSize: Int) {
        showLoading()
        launchOnIO {
            repository.getSheetList( page, pageSize)
                .checkResult(onSuccess = {
                    showContentView()
                    sheetList.value = it
                }, onError = {
                    showContentView()
                    DLog.i("------>", "$it")
                })
        }
    }

    fun itemClickFun(bean: SheetInfoBean) {
        itemClick(bean)
    }

}