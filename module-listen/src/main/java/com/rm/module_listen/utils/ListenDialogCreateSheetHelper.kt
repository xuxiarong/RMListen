package com.rm.module_listen.utils

import android.view.Gravity
import androidx.fragment.app.FragmentActivity
import com.rm.baselisten.BaseApplication
import com.rm.baselisten.dialog.CommonDragMvDialog
import com.rm.baselisten.utilExt.dip
import com.rm.module_listen.BR
import com.rm.module_listen.R
import com.rm.module_listen.databinding.ListenDialogCreateSheetBinding
import com.rm.module_listen.viewmodel.ListenDialogCreateSheetViewModel

/**
 * 创建听单dialog
 */
class ListenDialogCreateSheetHelper(
    private val mActivity: FragmentActivity,
    private val successBlock: () -> Unit

) {

    /**
     * viewModel对象
     */
    private val mViewModel by lazy {
        ListenDialogCreateSheetViewModel(mActivity,successBlock)
    }


    fun showCreateSheetDialog(audioId: String) {
        mViewModel.audioId = audioId
        mViewModel.mDialog = CommonDragMvDialog().apply {
            gravity = Gravity.BOTTOM
            dialogWidthIsMatchParent = true
            dialogHeightIsMatchParent = true
            dialogHeight = mActivity.dip(600)
            dialogHasBackground = true
        }.showCommonDialog(
            mActivity,
            R.layout.listen_dialog_create_sheet,
            mViewModel,
            BR.dialogViewModel
        ).apply {
            initDialog = {
                mViewModel.dataBinding = mDataBind as ListenDialogCreateSheetBinding
            }
        }
    }


    fun showEditDialog(sheetId: String, success: (String) -> Unit) {
        mViewModel.sheetId.set(sheetId)
        mViewModel.editSuccess = { success(it) }
        mViewModel.mDialog = CommonDragMvDialog().apply {
            gravity = Gravity.BOTTOM
            dialogWidthIsMatchParent = true
            dialogHeight = mActivity.dip(600)
            dialogHasBackground = true
            initDialog = {
                mViewModel.dataBinding = mDataBind as ListenDialogCreateSheetBinding
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