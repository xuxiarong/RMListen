package com.rm.module_mine.util

import android.net.Uri
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
class CommonTakePhotoHelp @JvmOverloads constructor(val activity: FragmentActivity) {

    private val mViewModel by lazy {
        MineCommonTakPhotoViewModel(activity)
    }

    fun showTakePhoto() {
        mViewModel.imageDialog.showCommonDialog(
            activity,
            R.layout.mine_dialog_bottom_select_image,
            mViewModel,
            BR.viewModel
        )
    }

    fun getCameraUri(): Uri? {
        return mViewModel.getCameraUri()
    }

    fun getCameraImagePath(): String? {
        return mViewModel.getCameraImagePath()
    }

}