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

    //音频Id
    val audioId = MutableLiveData<String>()

    //数据源
    val data = MutableLiveData<ListenSheetMyListBean>()

    //网络请求是否完成
    val isRefreshOrLoadComplete = MutableLiveData<Boolean>()

    var dismiss: () -> Unit = {}

    /**
     * 获取我的听单列表
     */
    fun getData(page: Int, pageSize: Int, showLoad: Boolean) {
        if (showLoad) {
            baseViewModel.showLoading()
        }
        launchOnIO {
            repository.getData(page, pageSize).checkResult(
                onSuccess = {
                    baseViewModel.showContentView()
                    data.value = it
                    isRefreshOrLoadComplete.value = true
                },
                onError = {
                    baseViewModel.showContentView()
                    isRefreshOrLoadComplete.value = true
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
                    baseViewModel.showToast("添加成功")
                    baseViewModel.showContentView()
                    dismissFun()
                },
                onError = {
                    DLog.i("----->", "添加失败  $it")
                    baseViewModel.showContentView()
                    baseViewModel.showToast("添加失败")
                }
            )
        }
    }

    fun itemClickFun(bean: ListenSheetBean) {
        audioId.value?.let {
            addSheet("${bean.sheet_id}", it)
        }
    }

    /**
     *
     */
    fun dismissFun() {
        dismiss()
    }
}