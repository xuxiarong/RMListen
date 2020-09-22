package com.rm.module_download.dialog

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.FragmentActivity
import com.rm.baselisten.dialog.BaseFragmentDialog
import com.rm.module_download.R

/**
 * 下载选集弹窗
 */
class DownloadSelectChaptersDialog : BaseFragmentDialog() {
    init {
        val bundle = Bundle()
        bundle.putInt(LAYOUT_ID, R.layout.download_dialog_select_chapters)
        arguments = bundle
    }


    // 下载按钮点击
    var downloadClick: (View) -> Unit = {}


    override fun initView() {
        super.initView()
        // 设置dialog有自己的背景
        dialogHasBackground = true
        rootView?.findViewById<AppCompatButton>(R.id.download_btn_download)?.setOnClickListener { downloadClick(it) }
    }

    fun show(activity: FragmentActivity) {
        showDialog<DownloadSelectChaptersDialog>(activity)
    }
}