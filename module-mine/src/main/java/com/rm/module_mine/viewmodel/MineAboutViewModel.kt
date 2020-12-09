package com.rm.module_mine.viewmodel

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.text.TextUtils
import androidx.annotation.RequiresApi
import androidx.core.content.FileProvider
import com.rm.baselisten.adapter.single.CommonBindVMAdapter
import com.rm.baselisten.net.checkResult
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.baselisten.web.BaseWebActivity
import com.rm.business_lib.base.dialog.VersionUploadDialog
import com.rm.business_lib.bean.BusinessVersionUrlBean
import com.rm.module_mine.BR
import com.rm.module_mine.R
import com.rm.module_mine.activity.MineAboutUsActivity.Companion.INSTALL_RESULT_CODE
import com.rm.module_mine.bean.MineAboutUsBean
import com.rm.module_mine.repository.MineRepository
import java.io.File


/**
 *
 * @author yuanfang
 * @date 9/21/20
 * @description
 *
 */
class MineAboutViewModel(private val repository: MineRepository) : BaseVMViewModel() {

    var versionInfo: BusinessVersionUrlBean? = null

    /**
     * 安装路径
     */
    var installApkPath = ""

    val mAdapter by lazy {
        CommonBindVMAdapter(
            this,
            mutableListOf(MineAboutUsBean("版本更新", "", "")),
            R.layout.mine_adapter_about_us,
            BR.viewModel,
            BR.item
        )
    }

    fun clickCooperation(context: Context, bean: MineAboutUsBean) {
        if (TextUtils.equals("版本更新", bean.title)) {
//            val lastVersion = versionInfo?.version?.replace(".", "") ?: "0"
//            val localVersion = BuildConfig.VERSION_NAME.replace(".", "")
//            try {
//                if (lastVersion.toInt() - localVersion.toInt() > 0) {
            showUploadDialog(context)
//                }
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }

        } else {
            bean.jump_url?.let {
                BaseWebActivity.startBaseWebActivity(context, it)
            }
        }
    }

    fun getAboutUs() {
        launchOnIO {
            repository.mineAboutUs().checkResult(onSuccess = {
                mAdapter.addData(it)
            }, onError = {
                showTip("$it", R.color.business_color_ff5e5e)
            })
        }
    }

    fun getLaseVersion() {
        launchOnIO {
            repository.mineGetLaseUrl().checkResult(onSuccess = {
                versionInfo = it
                mAdapter.data[0].sub_title = "${it.version}"
                mAdapter.notifyItemChanged(0)
            }, onError = {
                showTip("$it", R.color.business_color_ff5e5e)
            })
        }
    }

    private fun showUploadDialog(context: Context) {
        getActivity(context)?.let {
            VersionUploadDialog().apply {
                contentText = "${versionInfo?.description}"
                uploadUrl = "${versionInfo?.package_url}"
                version = "${versionInfo?.version}"
                downloadComplete = { path ->
                    //TODO 下载完成，跳转安装
                    dismiss()
                    installApkPath = path
                    openAPKFile(it)
                }
            }.show(it)
        }
    }

    fun openAPKFile(activity: Activity) {
        try {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            //7.0
            if (Build.VERSION.SDK_INT == Build.VERSION_CODES.N) {
                intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                val providerUri = FileProvider.getUriForFile(
                    activity,
                    "${activity.packageName}.fileprovider",
                    File(installApkPath)
                )
                intent.setDataAndType(providerUri, installApkPath)

                //8.0
                if (Build.VERSION.SDK_INT == Build.VERSION_CODES.O) {
                    //是否有安装权限
                    val canInstall = activity.packageManager.canRequestPackageInstalls()
                    if (!canInstall) {
                        startInstallSettingPermission(activity)
                        return
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    fun startInstallSettingPermission(activity: Activity) {
        val intent = Intent(
            Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES,
            Uri.parse("package:" + activity.packageName)
        )
        activity.startActivityForResult(intent, INSTALL_RESULT_CODE)
    }

}