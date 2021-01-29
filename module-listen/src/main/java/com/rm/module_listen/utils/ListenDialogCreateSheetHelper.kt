package com.rm.module_listen.utils

import android.content.Context
import android.view.Gravity
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.FragmentActivity
import com.rm.baselisten.BaseApplication
import com.rm.baselisten.dialog.CommonDragMvDialog
import com.rm.baselisten.utilExt.DisplayUtils.getStateHeight
import com.rm.baselisten.utilExt.dip
import com.rm.baselisten.utilExt.screenHeight
import com.rm.baselisten.view.DragCloseLayout
import com.rm.baselisten.viewmodel.BaseVMViewModel
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
        ListenDialogCreateSheetViewModel(mActivity, successBlock)
    }


    fun showCreateSheetDialog(audioId: String) {
        mViewModel.audioId = audioId
        mViewModel.mDialog = CommonDragMvDialog().apply {
            gravity = Gravity.BOTTOM
            dialogHeight = mActivity.screenHeight-getStateHeight(mActivity)
            dialogWidthIsMatchParent = true
            dialogHasBackground = true
            initDialog = {
                mViewModel.dataBinding = mDataBind as ListenDialogCreateSheetBinding
                dialog?.setOnShowListener {
                    mViewModel.dataBinding?.listenDialogCreateSheetEditName?.postDelayed({
                        mViewModel.dataBinding?.listenDialogCreateSheetEditName?.isFocusable =
                            true
                        mViewModel.dataBinding?.listenDialogCreateSheetEditName?.requestFocus()
                        mViewModel.dataBinding?.listenDialogCreateSheetEditName?.isFocusableInTouchMode =
                            true
                        val inputManager =
                            mViewModel.dataBinding?.listenDialogCreateSheetEditName?.context?.getSystemService(
                                Context.INPUT_METHOD_SERVICE
                            ) as InputMethodManager
                        inputManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS)
                    }, 50)
                }
            }
            moveListener= DragCloseLayout.IDragMoveListener {

            }
        }.showCommonDialog(
            mActivity,
            R.layout.listen_dialog_create_sheet,
            mViewModel,
            BR.dialogViewModel
        )
    }


    fun showEditDialog(sheetName: String, sheetId: String, success: (String) -> Unit) {
        mViewModel.sheetId.set(sheetId)
        mViewModel.editSuccess = { success(it) }
        mViewModel.mDialog = CommonDragMvDialog().apply {
            gravity = Gravity.BOTTOM
            dialogWidthIsMatchParent = true
            dialogHeight = mActivity.screenHeight-getStateHeight(mActivity)
            dialogHasBackground = true
            initDialog = {
                mViewModel.dataBinding = mDataBind as ListenDialogCreateSheetBinding
                mViewModel.dataBinding?.listenDialogCreateSheetTitle?.text =
                    (BaseApplication.CONTEXT.getString(R.string.listen_edit_sheet))
                mViewModel.inputText.set(sheetName)
                mViewModel.dataBinding?.listenDialogCreateSheetEditName?.setText(sheetName)
                mViewModel.dataBinding?.listenDialogCreateSheetEditName?.setSelection(sheetName.length)

                dialog?.setOnShowListener {
                    mViewModel.dataBinding?.listenDialogCreateSheetEditName?.postDelayed({
                        mViewModel.dataBinding?.listenDialogCreateSheetEditName?.isFocusable =
                            true
                        mViewModel.dataBinding?.listenDialogCreateSheetEditName?.requestFocus()
                        mViewModel.dataBinding?.listenDialogCreateSheetEditName?.isFocusableInTouchMode =
                            true
                        val inputManager =
                            mViewModel.dataBinding?.listenDialogCreateSheetEditName?.context?.getSystemService(
                                Context.INPUT_METHOD_SERVICE
                            ) as InputMethodManager
                        inputManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS)
                    }, 50)
                }

            }
        }.showCommonDialog(
            mActivity,
            R.layout.listen_dialog_create_sheet,
            mViewModel,
            BR.dialogViewModel
        )
    }

}