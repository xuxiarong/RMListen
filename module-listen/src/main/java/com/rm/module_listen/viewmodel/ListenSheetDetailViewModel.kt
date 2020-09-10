package com.rm.module_listen.viewmodel

import androidx.lifecycle.MutableLiveData
import com.rm.baselisten.dialog.CommBottomDialog
import com.rm.baselisten.net.checkResult
import com.rm.baselisten.util.DLog
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.module_listen.bean.ListenSheetDetailBean
import com.rm.module_listen.repository.ListenSheetDetailRepository

class ListenSheetDetailViewModel(private val repository: ListenSheetDetailRepository) :
    BaseVMViewModel() {

    fun dialogSheetDetailEditSheetFun() {}
    fun dialogSheetDetailDeleteFun() {}
    fun dialogSheetDetailCancelFun() {
        mDialog.dismiss()
    }

    val mDialog by lazy { CommBottomDialog() }
    val data = MutableLiveData<ListenSheetDetailBean>()

    fun getData(sheetId: String, page: Int) {
        launchOnIO {
            repository.getSheetDetail(sheetId, page).checkResult(
                onSuccess = {
                    data.value = it
                },
                onError = {
                    DLog.i("----->", "$it")
                }
            )
        }
    }
}