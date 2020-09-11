package com.rm.module_listen.utils

import android.view.Gravity
import androidx.fragment.app.FragmentActivity
import com.rm.baselisten.BaseApplication
import com.rm.baselisten.adapter.single.CommonBindVMAdapter
import com.rm.baselisten.dialog.CommonMvFragmentDialog
import com.rm.business_lib.bean.BookBean
import com.rm.module_listen.BR
import com.rm.module_listen.R
import com.rm.module_listen.databinding.ListenDialogSheetListBinding
import com.rm.module_listen.viewmodel.ListenDialogSheetViewModel

class ListenDialogSheetHelper(private val mActivity: FragmentActivity) {

    private val mViewModel by lazy {
        ListenDialogSheetViewModel()
    }

    private val mAdapter by lazy {
        CommonBindVMAdapter<BookBean>(
            mViewModel,
            mutableListOf(),
            R.layout.listen_adapter_dialog_book_list,
            BR.dialogItem,
            BR.dialogClick
        )
    }

    private val mDialog by lazy {
        val height = BaseApplication.CONTEXT.resources.getDimensionPixelSize(R.dimen.dp_390)
        CommonMvFragmentDialog().apply {
            gravity = Gravity.BOTTOM
            dialogWidthIsMatchParent = true
            dialogHeight = height
            dialogHasBackground = true
            initDialog = {
//                val homeDialogMenuDetailBinding =
//                    (menuDialog?.mDataBind) as ListenDialogSheetListBinding?
//
//                homeDialogMenuDetailBinding?.homeDialogMenuRecyclerView?.let {
//                    it.bindVerticalLayout(mDialogAdapter)
//                    it.linearBottomItemDecoration(resources.getDimensionPixelSize(R.dimen.dp_14))
//                }
            }
        }
    }

    fun showDialog() {
        mDialog.showCommonDialog(
            mActivity, R.layout.listen_dialog_sheet_list,
            mViewModel,
            BR.viewModel
        )
    }

}