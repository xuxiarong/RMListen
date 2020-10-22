package com.rm.module_download.binding

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.rm.business_lib.DownloadConstant
import com.rm.business_lib.db.download.DownloadChapter
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
fun ImageView.bindDownloadStatusSrc(chapter: DownloadChapter) {
    visibility = View.VISIBLE
    when (chapter.down_status) {
        DownloadConstant.CHAPTER_STATUS_NOT_DOWNLOAD -> {
            when (chapter.need_pay) {
                DownloadConstant.CHAPTER_PAY_STATUS_FREE -> {
                    visibility = View.GONE
                    return
                }
                DownloadConstant.CHAPTER_PAY_STATUS_NEED_BUY -> {
                    setImageResource(R.drawable.business_ic_pay)
                }
                DownloadConstant.CHAPTER_PAY_STATUS_VIP -> {
                    setImageResource(R.drawable.business_ic_pay)
                }
            }
        }
        DownloadConstant.CHAPTER_STATUS_DOWNLOAD_FINISH -> setImageResource(R.drawable.download_ic_download_completed)
        DownloadConstant.CHAPTER_STATUS_DOWNLOAD_WAIT -> setImageResource(R.drawable.shape_base_net_error_loading)
        DownloadConstant.CHAPTER_STATUS_DOWNLOADING -> setImageResource(R.drawable.shape_base_net_error_loading)
        DownloadConstant.CHAPTER_STATUS_DOWNLOAD_PAUSE -> setImageResource(R.drawable.shape_base_net_error_loading)
    }
}

@BindingAdapter("bindDownloadChapterStatus")
fun ImageView.bindDownloadChapterStatus(chapter: DownloadChapter) {

    if (chapter.down_status != DownloadConstant.CHAPTER_STATUS_NOT_DOWNLOAD) {
        setImageResource(R.drawable.download_ic_item_disable)
        return
    }
    if (chapter.chapter_edit_select) {
        setImageResource(R.drawable.download_ic_item_checked)
    } else {
        setImageResource(R.drawable.download_ic_item_unchecked)
    }
}
