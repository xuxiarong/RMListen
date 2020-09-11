package com.rm.module_listen.viewmodel

import androidx.lifecycle.MutableLiveData
import com.rm.baselisten.net.checkResult
import com.rm.baselisten.util.DLog
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.module_listen.bean.ListenSheetCollectedBean
import com.rm.module_listen.repository.ListenSheetCollectedRepository

class ListenSheetCollectedListViewModel(private val repository: ListenSheetCollectedRepository) :
    BaseVMViewModel() {
    val data = MutableLiveData<ListenSheetCollectedBean>()

    fun getData() {
        launchOnIO {
            repository.getCollectedList().checkResult(
                onSuccess = {
                    data.value = it
                },
                onError = {
                    DLog.i("--------?", "$it")
                }
            )
        }
    }

}