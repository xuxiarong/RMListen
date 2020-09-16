package com.rm.module_listen.viewmodel

import androidx.lifecycle.MutableLiveData
import com.rm.baselisten.net.checkResult
import com.rm.baselisten.util.DLog
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.business_lib.net.BusinessRetrofitClient
import com.rm.module_listen.api.ListenApiService
import com.rm.module_listen.bean.ListenSheetBean
import com.rm.module_listen.bean.ListenSheetMyListBean
import com.rm.module_listen.repository.ListenDialogSheetRepository

class ListenDialogSheetViewModel(private val baseViewModel: BaseVMViewModel) : BaseVMViewModel() {
    private val repository by lazy {
        ListenDialogSheetRepository(BusinessRetrofitClient().getService(ListenApiService::class.java))
    }

    val audioId = MutableLiveData<String>()
    val data = MutableLiveData<ListenSheetMyListBean>()

    /**
     * 获取我的听单列表
     */
    fun getData() {
        baseViewModel.showLoading()
        launchOnIO {
            repository.getData().checkResult(
                onSuccess = {
                    baseViewModel.showContentView()
                    data.value = it
                },
                onError = {
                    baseViewModel.showContentView()
                    DLog.i("------>", "$it")
                }
            )
        }
    }

    /**
     * 添加到听单列表
     */
    private fun addSheet(sheet_id: String, audio_id: String) {
        baseViewModel.showLoading()
        launchOnIO {
            repository.addSheetList(sheet_id, audio_id).checkResult(
                onSuccess = {
                    DLog.i("----->", "添加成功")
                    baseViewModel.showContentView()
                },
                onError = {
                    DLog.i("----->", "添加失败  $it")
                    baseViewModel.showContentView()
                }
            )
        }
    }

    fun itemClickFun(bean: ListenSheetBean) {
        audioId.value?.let {
            addSheet("${bean.sheet_id}", it)
        }
    }
}