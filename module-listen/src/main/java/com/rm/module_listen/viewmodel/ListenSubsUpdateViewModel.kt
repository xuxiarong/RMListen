package com.rm.module_listen.viewmodel

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.MutableLiveData
import com.rm.baselisten.net.checkResult
import com.rm.baselisten.util.DLog
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.business_lib.net.BusinessRetrofitClient
import com.rm.module_listen.api.ListenApiService
import com.rm.module_listen.model.ListenSubsDateModel
import com.rm.module_listen.model.ListenSubsModel
import com.rm.module_listen.repository.ListenSubsUpdateRepository

/**
 * desc   :
 * date   : 2020/09/02
 * version: 1.0
 */
class ListenSubsUpdateViewModel : BaseVMViewModel() {

    private val repository by lazy {
        ListenSubsUpdateRepository(BusinessRetrofitClient().getService(ListenApiService::class.java))
    }


    var subsDateVisible = ObservableBoolean(true)
    var subsDateListDate = MutableLiveData<MutableList<ListenSubsDateModel>>()
    var subsTodayData = MutableLiveData<ListenSubsModel>()


    fun getSubsDataFromService(){

        launchOnIO {
            repository.getListenSubsUpgradeList().checkResult(
                onSuccess = {
                    subsTodayData.value = it
                },onError = {
                    DLog.d("suolong","on error")
                }
            )
        }


        subsDateListDate.value = mutableListOf(
            ListenSubsDateModel("今天",false),
            ListenSubsDateModel("昨天",false),
            ListenSubsDateModel("8-29",false),
            ListenSubsDateModel("8-28",true),
            ListenSubsDateModel("8-27",false),
            ListenSubsDateModel("8-26",false),
            ListenSubsDateModel("8-25",false),
            ListenSubsDateModel("8-24",false),
            ListenSubsDateModel("8-23",false),
            ListenSubsDateModel("8-22",false)
        )
    }

}