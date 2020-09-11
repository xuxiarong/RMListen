package com.rm.module_listen.viewmodel

import androidx.lifecycle.MutableLiveData
import com.rm.baselisten.net.RetrofitKotlinManager
import com.rm.baselisten.net.checkResult
import com.rm.baselisten.util.DLog
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.business_lib.net.BusinessRetrofitClient
import com.rm.module_home.repository.ListenDialogSheetRepository
import com.rm.module_listen.api.ListenApiService
import com.rm.module_listen.bean.ListenSheetBean
import com.rm.module_listen.bean.ListenSheetMyListBean

class ListenDialogSheetViewModel() :
    BaseVMViewModel() {
    private val repository by lazy {
        ListenDialogSheetRepository(BusinessRetrofitClient().getService(ListenApiService::class.java))
    }

    val data = MutableLiveData<ListenSheetMyListBean>()

    fun getData() {
        launchOnIO {
            repository.getData().checkResult(
                onSuccess = {
                    data.value = it
                },
                onError = {
                    DLog.i("------>", "$it")
                }
            )
        }
    }

    fun itemClickFun(bean: ListenSheetBean) {

    }
}