package com.rm.module_listen.viewmodel

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.MutableLiveData
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.module_listen.model.ListenSubsDateModel

/**
 * desc   :
 * date   : 2020/09/02
 * version: 1.0
 */
class ListenSubsUpdateViewModel : BaseVMViewModel() {


    var subsDateVisible = ObservableBoolean(true)
    var subsDateListDate = MutableLiveData<MutableList<ListenSubsDateModel>>()

    fun getSubsDataFromService(){
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