package com.rm.module_listen.viewmodel

import androidx.lifecycle.MutableLiveData
import com.rm.baselisten.net.checkResult
import com.rm.baselisten.util.DLog
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.module_listen.bean.ListenSheetBean
import com.rm.module_listen.bean.ListenSheetMyListBean
import com.rm.module_listen.repository.ListenSheetMyListRepository

class ListenSheetMyListViewModel(private val repository: ListenSheetMyListRepository) :
    BaseVMViewModel() {

    //数据源
    val data = MutableLiveData<ListenSheetMyListBean>()

    //网络请求是否完成
    val isRefreshOrLoadComplete = MutableLiveData<Boolean>()


    //item点击事件
    var itemClick: (ListenSheetBean) -> Unit = {}

    fun itemClickFun(bean: ListenSheetBean) {
        itemClick(bean)
    }

    fun getData(page: Int, pageSize: Int) {
        launchOnIO {
            repository.getMyList(page, pageSize).checkResult(
                onSuccess = {
                    data.postValue(it)
                    showContentView()
                    isRefreshOrLoadComplete.value=true
                },
                onError = {
                    showNetError()
                    isRefreshOrLoadComplete.value=true
                    DLog.i("------->", "$it")
                }
            )
        }
    }
}