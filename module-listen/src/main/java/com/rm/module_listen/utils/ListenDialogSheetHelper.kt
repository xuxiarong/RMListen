package com.rm.module_listen.utils

import androidx.fragment.app.FragmentActivity
import com.rm.module_listen.BR
import com.rm.module_listen.R
import com.rm.module_listen.viewmodel.ListenDialogSheetViewModel

/**
 * 添加听单dialog
 */
class ListenDialogSheetHelper(
    private val mActivity: FragmentActivity,
    private val audioId: String,
    private val successBlock: () -> Unit
) {
    /**
     * viewModel对象
     */
    private val mViewModel by lazy { ListenDialogSheetViewModel(mActivity, audioId, successBlock) }


    /**
     * 显示dialog
     */
    fun showDialog() {
        mViewModel.mDialog.showCommonDialog(
            mActivity, R.layout.listen_dialog_sheet_list,
            mViewModel,
            BR.viewModel
        )
    }
}