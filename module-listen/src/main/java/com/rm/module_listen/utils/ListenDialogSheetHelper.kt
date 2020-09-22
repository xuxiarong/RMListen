package com.rm.module_listen.utils

import android.view.Gravity
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.observe
import com.rm.baselisten.BaseApplication
import com.rm.baselisten.adapter.single.CommonBindVMAdapter
import com.rm.baselisten.binding.bindVerticalLayout
import com.rm.baselisten.dialog.CommonMvFragmentDialog
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.module_listen.BR
import com.rm.module_listen.R
import com.rm.module_listen.bean.ListenSheetBean
import com.rm.module_listen.databinding.ListenDialogSheetListBinding
import com.rm.module_listen.viewmodel.ListenDialogSheetViewModel
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener

/**
 * 添加听单dialog
 */
class ListenDialogSheetHelper(
    private val baseViewModel: BaseVMViewModel,
    private val mActivity: FragmentActivity,
    private val audioId: String
) {

    /**
     * viewModel对象
     */
    private val mViewModel by lazy { ListenDialogSheetViewModel(baseViewModel) }

    /**
     * 懒加载adapter
     */
    private val mAdapter by lazy {
        CommonBindVMAdapter<ListenSheetBean>(
            mViewModel,
            mutableListOf(),
            R.layout.listen_adapter_dialog_book_list,
            BR.dialogClick,
            BR.dialogItem
        )
    }
    private var dateBinding: ListenDialogSheetListBinding? = null

    /**
     * 懒加载dialog
     */
    private val mDialog by lazy {
        val height = BaseApplication.CONTEXT.resources.getDimensionPixelSize(R.dimen.dp_390)
        CommonMvFragmentDialog().apply {
            gravity = Gravity.BOTTOM
            dialogWidthIsMatchParent = true
            dialogHeight = height
            dialogHasBackground = true
            initDialog = {
                dateBinding = mDataBind as ListenDialogSheetListBinding
                initView(dateBinding!!)
            }
        }
    }

    private val pageSize = 10
    private var page = 1

    /**
     * 初始化操作
     */
    private fun CommonMvFragmentDialog.initView(dateBinding: ListenDialogSheetListBinding) {
        addRefreshListener(dateBinding.listenDialogSheetRefresh)
        dateBinding.listenDialogSheetRecyclerView.let {
            it.bindVerticalLayout(mAdapter)
        }

        dateBinding.listenDialogSheetCreateBookList.setOnClickListener {
            ListenDialogCreateSheetHelper(baseViewModel, mActivity).showDialog()
            dismiss()
        }
        mViewModel.audioId.value = audioId
        startObserveData()

        mViewModel.getData(page, pageSize, true)
        mViewModel.dismiss = { dismiss() }
    }


    private fun startObserveData() {
        mViewModel.data.observe(mActivity) {
            if (page == 1) {
                mAdapter.setList(it.list)
            } else {
                it.list?.let { mAdapter.addData(it) }
            }

            //没有更多数据
            if (pageSize > it.list?.size ?: 0) {
                dateBinding?.listenDialogSheetRefresh?.finishLoadMoreWithNoMoreData()
            }
        }

        mViewModel.isRefreshOrLoadComplete.observe(mActivity) {
            if (page == 1) {
                //刷新完成
                dateBinding?.listenDialogSheetRefresh?.finishRefresh()
            } else {
                //加载更多完成
                dateBinding?.listenDialogSheetRefresh?.finishLoadMore()
            }
        }

    }

    private fun addRefreshListener(refreshLayout: SmartRefreshLayout) {
        refreshLayout.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
            override fun onLoadMore(refreshLayout: RefreshLayout) {
                ++page
                mViewModel.getData(page, pageSize, false)
            }

            override fun onRefresh(refreshLayout: RefreshLayout) {
                page = 1
                mViewModel.getData(page, pageSize, true)
            }
        })
    }

    /**
     * 显示dialog
     */
    fun showDialog() {
        mDialog.showCommonDialog(
            mActivity, R.layout.listen_dialog_sheet_list,
            mViewModel,
            BR.viewModel
        )
    }
}