package com.rm.module_listen.viewmodel

import androidx.lifecycle.MutableLiveData
import com.rm.baselisten.net.checkResult
import com.rm.baselisten.util.DLog
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.business_lib.bean.AudioBean
import com.rm.component_comm.home.HomeService
import com.rm.component_comm.router.RouterHelper
import com.rm.module_listen.bean.ListenSheetCollectedBean
import com.rm.module_listen.bean.ListenSheetCollectedDataBean
import com.rm.module_listen.repository.ListenSheetCollectedRepository

class ListenSheetCollectedListViewModel(private val repository: ListenSheetCollectedRepository) :
    BaseVMViewModel() {

    //数据源
    val data = MutableLiveData<ListenSheetCollectedBean>()

    //网络请求是否完成
    val isRefreshOrLoadComplete = MutableLiveData<Boolean>()

    //item点击事件
    var itemClick: (ListenSheetCollectedDataBean) -> Unit = {}
    var itemChildClick: (AudioBean) -> Unit = {}

    /**
     * 请求加载数据
     */
    fun getData(page: Int, pageSize: Int) {
        showLoading()
        launchOnIO {
            repository.getCollectedList(page, pageSize).checkResult(
                onSuccess = {
                    showContentView()
                    isRefreshOrLoadComplete.value = true
                    if (it.list.isNotEmpty()) {
                        data.postValue(it)
                    } else {
                        showDataEmpty()
                    }
                },
                onError = {
                    isRefreshOrLoadComplete.value = true
                    showNetError()
                    DLog.i("-------->", "$it")
                }
            )
        }
    }

    /**
     * item点击事件
     */
    fun itemClickFun(bean: ListenSheetCollectedDataBean) {
        itemClick(bean)
    }

    /**
     * 子view item点击事件
     */
    fun itemChildClickFun(bean: AudioBean) {
        itemChildClick(bean)
    }

}