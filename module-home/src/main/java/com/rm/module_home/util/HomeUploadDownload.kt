package com.rm.module_home.util

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.annotation.RequiresApi
import androidx.fragment.app.FragmentActivity
import com.rm.business_lib.aria.AriaUploadVersionDownloadManager
import com.rm.baselisten.dialog.TipsFragmentDialog
import com.rm.baselisten.util.ToastUtil
import com.rm.business_lib.bean.BusinessVersionUrlBean
import com.rm.business_lib.utils.ApkInstallUtils
import com.rm.business_lib.utils.NotifyManager
import com.rm.module_home.R

/**
 *
 * @author yuanfang
 * @date 12/10/20
 * @description
 *
 */
class HomeUploadDownload(
    private val versionInfo: BusinessVersionUrlBean,
    private val mActivity: FragmentActivity,
    private val installCode: Int,
    private val mDialogCancel: Boolean,
    private val cancelIsFinish: Boolean,
    private val downloadComplete: (String) -> Unit,
    private val sureIsDismiss: Boolean? = true,
    private var sureBlock: () -> Unit? = {},
    private var cancelBlock: () -> Unit? = {},
    private var downloadFail: () -> Unit? = {}
) {
    companion object {
        @RequiresApi(api = Build.VERSION_CODES.O)
        fun startInstallSettingPermission(activity: Activity, installCode: Int) {
            val intent = Intent(
                Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES,
                Uri.parse("package:" + activity.packageName)
            )
            activity.startActivityForResult(intent, installCode)
        }

    }

    fun showUploadDialog() {
        TipsFragmentDialog().apply {
            titleText = mActivity.getString(R.string.business_upload_tips)
            contentText = "${versionInfo.description}"
            if (!cancelIsFinish) {
                leftBtnText = mActivity.getString(R.string.business_not_upgrade)
            }
            rightBtnText = mActivity.getString(R.string.business_upgrade)
            dialogCancel = mDialogCancel
            rightBtnTextColor = R.color.business_color_ff5e5e
            leftBtnClick = {
                dismiss()
                cancelBlock()
                if (cancelIsFinish) {
                    mActivity.finish()
                }
            }
            rightBtnClick = {
                downApk {
                    downloadFail()
                }
                if (sureIsDismiss == true) {
                    dismiss()
                }
                sureBlock()
            }
        }.show(mActivity)
    }

    private val downloadUrl =
        "http://ls-book.leimans.com/common/groups/7722/a7aec234aec865115c23c1c34cc14aaa.apk"

    private fun downApk(downloadFail: () -> Unit? = {}) {
        val notifyManager = NotifyManager(mActivity)
        AriaUploadVersionDownloadManager.startDownload(
//            downloadUrl,
            versionInfo.package_url ?: "",
            versionInfo.version ?: "",
            downloadStart = {
                notifyManager.startNotificationManager()
            },
            downloadProgress = { progress ->
                notifyManager.updateProgress(progress)
            },
            downloadComplete = { path ->
                downloadComplete(path)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    if (!mActivity.packageManager.canRequestPackageInstalls()) {
                        startInstallSettingPermission(mActivity, installCode)
                    } else {
                        ApkInstallUtils.install(mActivity, path)
                    }
                } else {
                    ApkInstallUtils.install(mActivity, path)
                }
            },
            downloadFail = {
                ToastUtil.showTopToast(mActivity, "下载失败")
                downloadFail()
            }
        )
    }
}