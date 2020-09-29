package com.rm.module_download.binding

import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.databinding.BindingAdapter
import com.rm.module_download.R
import com.rm.module_download.bean.DownloadChapterUIStatus

@BindingAdapter("bindDownloadCheckSrc")
fun ImageView.bindDownloadCheckSrc(status: DownloadChapterUIStatus) {
    when (status) {
        DownloadChapterUIStatus.DOWNLOADING, DownloadChapterUIStatus.COMPLETED -> setImageResource(R.drawable.download_ic_item_disable)
        DownloadChapterUIStatus.UNCHECK -> setImageResource(R.drawable.download_ic_item_unchecked)
        DownloadChapterUIStatus.CHECKED -> setImageResource(R.drawable.download_ic_item_checked)
    }
}

@BindingAdapter("bindDownloadStatusSrc")
fun ImageView.bindDownloadStatusSrc(status: DownloadChapterUIStatus) {
    when (status) {
        DownloadChapterUIStatus.COMPLETED -> {
            visibility = View.VISIBLE
            setImageResource(R.drawable.download_ic_download_completed)
        }
        else -> visibility = View.GONE
    }
}
