package com.rm.module_mine.util

import androidx.fragment.app.FragmentActivity
import com.rm.module_mine.BR
import com.rm.module_mine.R
import com.rm.module_mine.viewmodel.MineCommonTakPhotoViewModel

/**
 *
 * @author yuan fang
 * @date 12/2/20
 * @description
 *
 */
class CommonTakePhotoHelp @JvmOverloads constructor(
    val activity: FragmentActivity,
    isCropPic: Boolean = false,
    onSuccess: (String) -> Unit? = {},
    onFailure: (String) -> Unit? = {}
) {
    private val mViewModel by lazy {
        MineCommonTakPhotoViewModel(activity, isCropPic, onSuccess, onFailure)
    }

    fun showTakePhoto() {
        mViewModel.imageDialog.showCommonDialog(
            activity,
            R.layout.mine_dialog_bottom_select_image,
            mViewModel,
            BR.viewModel
        )
    }

}