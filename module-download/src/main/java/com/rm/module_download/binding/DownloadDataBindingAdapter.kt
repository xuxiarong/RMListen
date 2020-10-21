package com.rm.module_download.binding

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.rm.business_lib.DownloadConstant
import com.rm.business_lib.bean.download.DownloadChapterStatusModel
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
fun ImageView.bindDownloadStatusSrc(chapterStatusModel: DownloadChapterStatusModel) {
    visibility = View.VISIBLE
    when (chapterStatusModel.downStatus) {
        DownloadConstant.CHAPTER_STATUS_NOT_DOWNLOAD -> {
            when (chapterStatusModel.chapter.need_pay) {
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
fun ImageView.bindDownloadChapterStatus(chapterStatusModel: DownloadChapterStatusModel) {

    if (chapterStatusModel.downStatus != DownloadConstant.CHAPTER_STATUS_NOT_DOWNLOAD) {
        setImageResource(R.drawable.download_ic_item_disable)
    } else {
        when (chapterStatusModel.select) {
            DownloadConstant.CHAPTER_UN_SELECT -> {
                setImageResource(R.drawable.download_ic_item_unchecked)
            }
            DownloadConstant.CHAPTER_SELECTED -> {
                setImageResource(R.drawable.download_ic_item_checked)
            }
            else -> {
                setImageResource(R.drawable.download_ic_item_unchecked)
            }
        }
    }
}
