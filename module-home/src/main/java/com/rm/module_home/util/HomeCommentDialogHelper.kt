package com.rm.module_home.util

import android.content.Context
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.FragmentActivity
import com.rm.baselisten.mvvm.BaseActivity
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.module_home.BR
import com.rm.module_home.R
import com.rm.module_home.viewmodel.HomeCommentDialogViewModel
import kotlinx.android.synthetic.main.home_dialog_comment.*

/**
 *
 * @author yuanfang
 * @date 10/16/20
 * @description
 *
 */
class HomeCommentDialogHelper(
    private val baseViewModel: BaseVMViewModel,
    private val mActivity: FragmentActivity,
    private val audio: String,
    private val commentSuccessBlock: () -> Unit
) {
    /**
     * viewModel对象
     */
    private val mViewModel by lazy {
        HomeCommentDialogViewModel(baseViewModel, audio, commentSuccessBlock)
    }

    fun showDialog() {
        mViewModel.mDialog.showCommonDialog(
            mActivity,
            R.layout.home_dialog_comment,
            mViewModel,
            BR.homeCommentDialogViewModel
        )
    }
}