package com.rm.module_home.util

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.annotation.RequiresApi
import androidx.fragment.app.FragmentActivity
import com.rm.business_lib.aria.AriaUploadVersionDownloadManager
import com.rm.business_lib.base.dialog.TipsFragmentDialog
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
    private val enforceUpdate: Boolean,
    private val downloadComplete:(String)->Unit,
    private val sureIsDismiss: Boolean? = true,
    private var sureBlock: () -> Unit? = {},
    private var cancelBlock: () -> Unit? = {}
) {
    companion object {
        @RequiresApi(api = Build.VERSION_CODES.O)
        fun startInstallSettingPermission(activity: Activity, path: String, installCode: Int) {
            val intent = Intent(
                Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES,
                Uri.parse("package:" + activity.packageName)
            )
            intent.putExtra("apkPath", path)
            activity.startActivityForResult(intent, installCode)
        }

    }

    fun showUploadDialog() {
        TipsFragmentDialog().apply {
            titleText = mActivity.getString(R.string.business_upload_tips)
            contentText = "${versionInfo.description}"
            leftBtnText = mActivity.getString(R.string.business_not_upgrade)
            rightBtnText = mActivity.getString(R.string.business_upgrade)
            dialogCancel = !enforceUpdate
            rightBtnTextColor = R.color.business_color_ff5e5e
            leftBtnClick = {
                if (enforceUpdate) {
                    mActivity.finish()
                }
                dismiss()
                cancelBlock()
            }
            rightBtnClick = {
                downApk()
                if (sureIsDismiss == true) {
                    dismiss()
                }
                sureBlock()
            }
        }.show(mActivity)
    }


    private fun downApk() {
        val notifyManager = NotifyManager(mActivity)
        AriaUploadVersionDownloadManager.startDownload(
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
                        startInstallSettingPermission(mActivity, path, installCode)
                    } else {
                        ApkInstallUtils.install(mActivity, path)
                    }
                } else {
                    ApkInstallUtils.install(mActivity, path)
                }
            })
    }
}