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
                val dateBinding = mDataBind as ListenDialogSheetListBinding

                initView(dateBinding)
            }
        }
    }

    /**
     * 初始化操作
     */
    private fun CommonMvFragmentDialog.initView(dateBinding: ListenDialogSheetListBinding) {
        dateBinding.listenDialogSheetRecyclerView.let {
            it.bindVerticalLayout(mAdapter)
        }

        dateBinding.listenDialogSheetCreateBookList.setOnClickListener {
            ListenDialogCreateSheetHelper(baseViewModel, mActivity).showDialog()
            dismiss()
        }
        mViewModel.audioId.value = audioId
        startObserveData()
        mViewModel.getData()
        mViewModel.dismiss = { dismiss() }
    }


    private fun startObserveData() {
        mViewModel.data.observe(mActivity) {
            mAdapter.setList(it.list)
        }
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