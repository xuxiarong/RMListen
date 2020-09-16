package com.rm.module_listen.utils

import androidx.fragment.app.FragmentActivity
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.module_listen.BR
import com.rm.module_listen.R
import com.rm.module_listen.bean.ListenSheetBean
import com.rm.module_listen.bean.ListenSheetDetailDataBean
import com.rm.module_listen.viewmodel.ListenDialogCreateSheetViewModel

/**
 * 创建听单dialog
 */
class ListenDialogCreateSheetHelper(
    private val baseViewModel: BaseVMViewModel,
    private val mActivity: FragmentActivity
) {

    /**
     * viewModel对象
     */
    private val mViewModel by lazy {
        ListenDialogCreateSheetViewModel(baseViewModel)
    }

    fun setTitle(title: String): ListenDialogCreateSheetHelper {
        mViewModel.titleData.set(title)
        return this
    }

    fun showDialog() {
        mViewModel.mDialog.showCommonDialog(
            mActivity, R.layout.listen_dialog_create_sheet,
            mViewModel,
            BR.dialogViewModel
        )
    }

    fun showEditDialog(sheetBean: ListenSheetBean, success: () -> Unit) {
        mViewModel.sheetBean.set(sheetBean)
        mViewModel.editSuccess = { success() }
        mViewModel.mDialog.showCommonDialog(
            mActivity, R.layout.listen_dialog_create_sheet,
            mViewModel,
            BR.dialogViewModel
        )
    }
}