package com.rm.module_mine.viewmodel

import android.net.Uri
import androidx.fragment.app.FragmentActivity
import com.rm.baselisten.dialog.CommBottomDialog
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.business_lib.utils.BusinessCameraAndAlbum
import com.rm.module_mine.activity.MineCropActivity

/**
 *
 * @author yuanfang
 * @date 12/2/20
 * @description
 *
 */
class MineCommonTakPhotoViewModel(val activity: FragmentActivity) : BaseVMViewModel() {

    val imageDialog by lazy { CommBottomDialog() }

    private val cameraAndAlbum by lazy { BusinessCameraAndAlbum(activity) }

    /**
     * 相机
     */
    fun imageDialogCameraFun() {
        cameraAndAlbum.openCamera()
        imageDialog.dismiss()
    }

    /**
     * 相册
     */
    fun imageDialogAlbumFun() {
        cameraAndAlbum.openAlbum()
        imageDialog.dismiss()
    }

    /**
     * 取消
     */
    fun imageDialogCancelFun() {
        imageDialog.dismiss()
    }

    fun getCameraUri(): Uri? {
        return cameraAndAlbum.getCameraUri()
    }

    fun getCameraImagePath(): String? {
        return cameraAndAlbum.getCameraImagePath()
    }
}