package com.rm.module_listen.viewmodel

import android.widget.ImageView
import androidx.fragment.app.FragmentActivity
import com.rm.baselisten.adapter.single.CommonBindVMAdapter
import com.rm.baselisten.dialog.CommBottomDialog
import com.rm.baselisten.net.checkResult
import com.rm.baselisten.util.getBooleanMMKV
import com.rm.baselisten.util.putMMKV
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.business_lib.IS_FIRST_ADD_SHEET
import com.rm.business_lib.LISTEN_SHEET_LIST_MY_LIST
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
import com.rm.module_listen.utils.ListenDialogCreateSheetHelper

class ListenDialogSheetViewModel(
    private val mActivity: FragmentActivity,
    private val audioId: String,
    private val successBlock: () -> Unit,
    private val viewModel: BaseVMViewModel
) : BaseVMViewModel() {

    private val repository by lazy {
        ListenRepository(BusinessRetrofitClient().getService(ListenApiService::class.java))
    }

    /**
     * 懒加载dialog
     */
    val mDialog by lazy { CommBottomDialog() }

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
    val contentRvId = R.id.listen_dialog_sheet_recycler_view
    private val pageSize = 12
    private var page = 1

    init {
        getData()
    }

    /**
     * 获取我的听单列表
     */
    fun getData() {
        launchOnIO {
            repository.getMyList(page, pageSize).checkResult(
                onSuccess = {
                    processSuccessData(it)
                },
                onError = { msg, _ ->
                    processFailData(msg)
                }
            )
        }
    }

    /**
     * 添加到听单列表
     */
    private fun addSheet(sheet_id: String, audio_id: String) {
        launchOnIO {
            repository.addSheetList(sheet_id, audio_id).checkResult(
                onSuccess = {
                    addSheetSuccess()
                },
                onError = { it, _ ->
                    viewModel.showErrorToast("$it")
                }
            )
        }
    }

    /**
     * 创建听单点击事件
     */
    fun clickCreateSheet() {
        ListenDialogCreateSheetHelper(
            mActivity,
            successBlock = successBlock,
            viewModel = viewModel
        ).showCreateSheetDialog(audioId)
        mDialog.dismiss()
    }

    /**
     * 处理成功数据
     */
    private fun processSuccessData(bean: ListenSheetMyListBean) {
        if (page == 1) {
            //刷新完成
            refreshStateModel.finishRefresh(true)
            if (bean.list?.size ?: 0 > 0) {
                mAdapter.setList(bean.list)
            } else {
                showData()
            }
        } else {
            //加载更多完成
            bean.list?.let { mAdapter.addData(it) }
            refreshStateModel.finishLoadMore(true)
        }
        ++page
        //是否有跟多数据
        refreshStateModel.setNoHasMore(bean.list?.size ?: 0 < pageSize)
    }

    private fun showData() {

    }

    /**
     * 处理失败数据
     */
    private fun processFailData(msg: String?) {
        if (page == 1) {
            refreshStateModel.finishRefresh(false)
        } else {
            refreshStateModel.finishLoadMore(false)
        }
        viewModel.showErrorToast("$msg")
    }

    /**
     * 添加成功
     */
    private fun addSheetSuccess() {
        if (IS_FIRST_ADD_SHEET.getBooleanMMKV(true)) {
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
                    RouterHelper.createRouter(ListenService::class.java).startListenSheetList(
                        mActivity,
                        LISTEN_SHEET_LIST_MY_LIST,
                        ""
                    )
                    dismiss()
                }
                customView =
                    ImageView(mActivity).apply { setImageResource(R.mipmap.business_img_dycg) }
            }.show(mActivity)
        } else {
            successBlock()
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
        getData()
    }

    /**
     * item 点击事件
     */
    fun itemClickFun(bean: ListenSheetBean) {
        addSheet("${bean.sheet_id}", audioId)
    }

    /**
     *dismiss
     */
    private fun dismissFun() {
        mDialog.dismiss()
    }

}