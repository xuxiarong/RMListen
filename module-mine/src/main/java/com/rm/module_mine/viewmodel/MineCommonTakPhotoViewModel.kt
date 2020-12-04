package com.rm.module_mine.viewmodel

import android.content.pm.ActivityInfo
import androidx.fragment.app.FragmentActivity
import com.bugrui.cameralibrary.*
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.language.LanguageConfig
import com.luck.picture.lib.listener.OnResultCallbackListener
import com.rm.baselisten.dialog.CommBottomDialog
import com.rm.baselisten.util.DLog
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.module_mine.activity.MineCropActivity
import com.rm.module_mine.util.GlideEngine

/**
 *
 * @author yuanfang
 * @date 12/2/20
 * @description
 *
 */
class MineCommonTakPhotoViewModel(
    val activity: FragmentActivity,
    val isCropPic: Boolean,
    val onSuccess: (String) -> Unit?,
    val onFailure: (String) -> Unit?
) : BaseVMViewModel() {
    val imageDialog by lazy { CommBottomDialog() }

    /**
     * 相机
     */
    fun imageDialogCameraFun() {
        activity.openCamera(
            chooseMode = PictureMimeType.ofImage(),
            cameraTheme = CameraTheme(theme = pictureCameraThemeWhite),
            compress = CameraCompress(
                isCompress = true,
                synOrAsy = true
            ),
            language = LanguageConfig.CHINESE,
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT,
            resultListener = object : OnResultCallbackListener<LocalMedia> {
                override fun onResult(result: MutableList<LocalMedia>?) {
                    //返回结果
                    result?.let {
                        if (isCropPic) {
                            cropPic(it[0].androidQToPath)
                        } else {
                            onSuccess(it[0].androidQToPath)
                        }
                    }
                }

                override fun onCancel() {
                    //取消
                    DLog.i("---->拍照", "取消")
                    onFailure("拍照取消")
                }
            }
        )
        imageDialog.dismiss()
    }

    /**
     * 相册
     */
    fun imageDialogAlbumFun() {
        activity.openGallery(
            chooseMode = PictureMimeType.ofImage(),    //图片or视频
            isCamera = false,              //是否显示拍照按钮
            isOriginalControl = false,      //是否显示原图控制按钮，如果用户勾选了 压缩、裁剪功能将会失效
            isGif = false,                 //是否显示gif图片
            language = LanguageConfig.CHINESE,  //设置语言，默认中文
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT,//屏幕旋转方向
            engine = GlideEngine,
            resultListener = object : OnResultCallbackListener<LocalMedia> {
                override fun onResult(result: MutableList<LocalMedia>?) {
                    //返回结果
                    result?.let {
                        if (isCropPic) {
                            cropPic(it[0].androidQToPath)
                        } else {
                            onSuccess(it[0].androidQToPath)
                        }
                    }
                }

                override fun onCancel() {
                    //取消
                    DLog.i("---->相册", "取消")
                    onFailure("相册取消")
                }
            }
        )
        imageDialog.dismiss()
    }

    /**
     * 取消
     */
    fun imageDialogCancelFun() {
        imageDialog.dismiss()
    }

    private fun cropPic(filePath: String) {
        MineCropActivity.startActivityForResult(activity, filePath, 100)
    }
}