package com.rm.business_lib.base.dialog

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.text.TextUtils
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import com.rm.baselisten.binding.bindText
import com.rm.baselisten.dialog.BaseFragmentDialog
import com.rm.business_lib.R
import com.rm.business_lib.aria.AriaUploadVersionDownloadManager


/**
 * desc   : 纯文本内容提示框
 * date   : 2020/09/10
 * version: 1.0
 */
class VersionUploadDialog : BaseFragmentDialog() {
    init {
        val bundle = Bundle()
        bundle.putInt(LAYOUT_ID, R.layout.business_dialog_version_upload_layout)
        arguments = bundle
    }

    // 显示内容
    var contentText = ""
    var uploadUrl = ""
    var version = ""
    var downloadComplete: (String) -> Unit = {}

    override fun initView() {
        super.initView()
        dialogCanceledOnTouchOutside = false

        // 设置dialog有自己的背景
        dialogHasBackground = true
        // 设置内容显示
        rootView?.findViewById<TextView>(R.id.business_version_upload_dialog_content)
            ?.bindText(contentText)
        val progressBar =
            rootView?.findViewById<ProgressBar>(R.id.business_version_upload_dialog_progress)

        rootView?.findViewById<View>(R.id.business_version_upload_dialog_cancel)
            ?.setOnClickListener { dismiss() }
        rootView?.findViewById<View>(R.id.business_version_upload_dialog_sure)
            ?.setOnClickListener {
                if (!TextUtils.isEmpty(uploadUrl)) {
                    progressBar?.visibility = View.VISIBLE
                    AriaUploadVersionDownloadManager.startDownload(
                        uploadUrl,
                        version,
                        downloadProgress = { progress ->
                            progressBar?.progress = progress
                        },
                        downloadComplete = {
                            downloadComplete(it)
                        })
                }
            }
    }

    fun show(activity: FragmentActivity) {
        showDialog<VersionUploadDialog>(activity)
    }
}