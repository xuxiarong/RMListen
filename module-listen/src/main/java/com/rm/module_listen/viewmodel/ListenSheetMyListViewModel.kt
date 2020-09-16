package com.rm.module_listen.viewmodel

import androidx.lifecycle.MutableLiveData
import com.rm.baselisten.net.checkResult
import com.rm.baselisten.util.DLog
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.business_lib.bean.SheetBean
import com.rm.module_listen.bean.ListenSheetBean
import com.rm.module_listen.bean.ListenSheetMyListBean
import com.rm.module_listen.repository.ListenSheetMyListRepository

class ListenSheetMyListViewModel(private val repository: ListenSheetMyListRepository) :
    BaseVMViewModel() {
    val data = MutableLiveData<ListenSheetMyListBean>()
    var itemClick: (ListenSheetBean) -> Unit = {}

    fun itemClickFun(bean: ListenSheetBean) {
        itemClick(bean)
    }

    fun getData() {
        launchOnIO {
            repository.getMyList().checkResult(
                onSuccess = {
                    data.postValue(it)
                },
                onError = {
                    DLog.i("------->", "$it")
                }
            )
        }
    }
}