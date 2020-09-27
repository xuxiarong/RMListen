package com.rm.module_listen.viewmodel

import androidx.lifecycle.MutableLiveData
import com.rm.baselisten.BaseApplication.Companion.CONTEXT
import com.rm.baselisten.adapter.single.CommonBindVMAdapter
import com.rm.baselisten.net.checkResult
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.business_lib.net.BusinessRetrofitClient
import com.rm.business_lib.wedgit.smartrefresh.model.SmartRefreshLayoutStatusModel
import com.rm.module_listen.BR
import com.rm.module_listen.R
import com.rm.module_listen.api.ListenApiService
import com.rm.module_listen.bean.ListenSheetBean
import com.rm.module_listen.bean.ListenSheetMyListBean
import com.rm.module_listen.repository.ListenDialogSheetRepository

class ListenDialogSheetViewModel(private val baseViewModel: BaseVMViewModel) : BaseVMViewModel() {
    private val repository by lazy {
        ListenDialogSheetRepository(BusinessRetrofitClient().getService(ListenApiService::class.java))
    }

    /**
     * 懒加载adapter
     */
    val mAdapter by lazy {
        CommonBindVMAdapter<ListenSheetBean>(
            this,
            mutableListOf(),
            R.layout.listen_adapter_dialog_book_list,
            BR.dialogClick,
            BR.dialogItem
        )
    }

    val refreshStateModel = SmartRefreshLayoutStatusModel()

    //音频Id
    val audioId = MutableLiveData<String>()

    var dismiss: () -> Unit = {}

    private val pageSize = 10
    private var page = 1

    /**
     * 获取我的听单列表
     */
    fun getData() {
        launchOnIO {
            repository.getData(page, pageSize).checkResult(
                onSuccess = {
                    processSuccessData(it)
                },
                onError = {
                    processFailData()
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
                    baseViewModel.showToast(CONTEXT.getString(R.string.listen_add_success))
                    baseViewModel.showContentView()
                    dismissFun()
                },
                onError = {
                    baseViewModel.showContentView()
                    baseViewModel.showToast(CONTEXT.getString(R.string.listen_add_fail))
                }
            )
        }
    }

    /**
     * 处理成功数据
     */
    private fun processSuccessData(bean: ListenSheetMyListBean) {
        baseViewModel.showContentView()
        if (page == 1) {
            //刷新完成
            refreshStateModel.finishRefresh(true)
            mAdapter.setList(bean.list)
        } else {
            //加载更多完成
            refreshStateModel.finishLoadMore(true)
            bean.list?.let { mAdapter.addData(it) }
        }

        //是否有跟多数据
        refreshStateModel.setHasMore(bean.list?.size ?: 0 >= pageSize)
    }

    /**
     * 处理失败数据
     */
    private fun processFailData() {
        baseViewModel.showContentView()
        if (page == 1) {
            refreshStateModel.finishRefresh(false)
        } else {
            refreshStateModel.finishLoadMore(false)
        }
    }

    /**
     * 刷新数据
     */
    fun refreshData() {
        page = 1
        getData()
    }

    /**
     * 加载更多数据
     */
    fun loadData() {
        ++page
        getData()
    }

    /**
     * item 点击事件
     */
    fun itemClickFun(bean: ListenSheetBean) {
        audioId.value?.let {
            addSheet("${bean.sheet_id}", it)
        }
    }

    /**
     *dismiss
     */
    private fun dismissFun() {
        dismiss()
    }
}