package com.rm.module_mine.util

import androidx.fragment.app.FragmentActivity
import com.rm.module_mine.BR
import com.rm.module_mine.R
import com.rm.module_mine.viewmodel.MineCommentTakPhotoViewModel

/**
 *
 * @author yuan fang
 * @date 12/2/20
 * @description
 *
 */
class CommentTakePhotoHelp(val activity: FragmentActivity,isCropPic:Boolean) {

    private val mViewModel by lazy {
        MineCommentTakPhotoViewModel(activity,isCropPic)
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