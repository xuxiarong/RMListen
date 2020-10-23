package com.rm.module_listen.utils

import android.view.Gravity
import androidx.fragment.app.FragmentActivity
import com.rm.baselisten.BaseApplication
import com.rm.baselisten.dialog.CommonDragMvDialog
import com.rm.baselisten.utilExt.dip
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.module_listen.BR
import com.rm.module_listen.R
import com.rm.module_listen.databinding.ListenDialogCreateSheetBinding
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


    fun showCreateSheetDialog(audioId: String) {
        mViewModel.audioId = audioId
        mViewModel.mDialog = CommonDragMvDialog().apply {
            gravity = Gravity.BOTTOM
            dialogWidthIsMatchParent = true
            dialogHeight = mActivity.dip(600)
            dialogHasBackground = true

        }.showCommonDialog(
            mActivity,
            R.layout.listen_dialog_create_sheet,
            mViewModel,
            BR.dialogViewModel
        )
    }

    fun showCreateSheetDialog() {
        mViewModel.mDialog = CommonDragMvDialog().apply {
            gravity = Gravity.BOTTOM
            dialogWidthIsMatchParent = true
            dialogHeight = mActivity.dip(600)
            dialogHasBackground = true
        }.showCommonDialog(
            mActivity,
            R.layout.listen_dialog_create_sheet,
            mViewModel,
            BR.dialogViewModel
        )
    }

    fun showEditDialog(sheetId: String, success: (String) -> Unit) {
        mViewModel.sheetId.set(sheetId)
        mViewModel.editSuccess = { success(it) }
        mViewModel.mDialog = CommonDragMvDialog().apply {
            gravity = Gravity.BOTTOM
            dialogWidthIsMatchParent = true
            dialogHeight = mActivity.dip(600)
            dialogHasBackground = true
            initDialog={
                (mDataBind as ListenDialogCreateSheetBinding).listenDialogCreateSheetTitle.text =
                    (BaseApplication.CONTEXT.getString(R.string.listen_edit_sheet))
            }
        }.showCommonDialog(
            mActivity,
            R.layout.listen_dialog_create_sheet,
            mViewModel,
            BR.dialogViewModel
        )
    }
}