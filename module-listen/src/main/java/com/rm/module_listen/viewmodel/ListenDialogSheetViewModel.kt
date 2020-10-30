package com.rm.module_listen.viewmodel

import android.widget.ImageView
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import com.rm.baselisten.adapter.single.CommonBindVMAdapter
import com.rm.baselisten.net.checkResult
import com.rm.baselisten.util.getBooleanMMKV
import com.rm.baselisten.util.putMMKV
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.business_lib.IS_FIRST_ADD_SHEET
import com.rm.business_lib.base.dialog.CustomTipsFragmentDialog
import com.rm.business_lib.net.BusinessRetrofitClient
import com.rm.business_lib.wedgit.smartrefresh.model.SmartRefreshLayoutStatusModel
import com.rm.component_comm.listen.ListenService
import com.rm.component_comm.router.RouterHelper
import com.rm.module_listen.BR
import com.rm.module_listen.R
import com.rm.module_listen.api.ListenApiService
import com.rm.module_listen.bean.ListenSheetBean
import com.rm.module_listen.bean.ListenSheetMyListBean
import com.rm.module_listen.repository.ListenRepository

class ListenDialogSheetViewModel(
   private val mActivity: FragmentActivity,
    private val baseViewModel: BaseVMViewModel
) : BaseVMViewModel() {
    private val repository by lazy {
        ListenRepository(BusinessRetrofitClient().getService(ListenApiService::class.java))
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

    private val pageSize = 12
    private var page = 1

    /**
     * 获取我的听单列表
     */
    fun getData() {
        launchOnIO {
            repository.getMyList(page, pageSize).checkResult(
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
                    baseViewModel.showContentView()
                    addSheetSuccess(sheet_id)
                },
                onError = {
                    baseViewModel.showContentView()
                    baseViewModel.showToast("$it")
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
        refreshStateModel.setHasMore(bean.list?.size ?: 0 > pageSize)
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
     * 添加成功
     */
    private fun addSheetSuccess(sheetId: String) {
        if (IS_FIRST_ADD_SHEET.getBooleanMMKV(true) ) {
            CustomTipsFragmentDialog().apply {
                titleText = mActivity.getString(R.string.listen_add_success)
                contentText = mActivity.getString(R.string.listen_add_success_content)
                leftBtnText = mActivity.getString(R.string.listen_know)
                rightBtnText = mActivity.getString(R.string.listen_goto_look)
                leftBtnTextColor = R.color.business_text_color_333333
                rightBtnTextColor = R.color.business_color_ff5e5e
                leftBtnClick = {
                    dismiss()
                }
                rightBtnClick = {
                    RouterHelper.createRouter(ListenService::class.java).startMySheetDetail(
                        mActivity,
                        sheetId
                    )
                    dismiss()
                }
                customView =
                    ImageView(mActivity).apply { setImageResource(R.mipmap.business_img_dycg) }
            }.show(mActivity)
        } else {
            showToast(mActivity.getString(R.string.listen_add_success_tip))
        }
        dismissFun()
        IS_FIRST_ADD_SHEET.putMMKV(false)
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