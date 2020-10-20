package com.rm.module_download.binding

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.rm.module_download.DownloadConstant
import com.rm.module_download.R
import com.rm.module_download.bean.DownloadChapterUIStatus
import com.rm.module_download.model.DownloadChapterStatusModel

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
    when (chapterStatusModel.status) {
        DownloadConstant.CHAPTER_STATUS_DOWNLOAD_FINISH -> {
            setImageResource(R.drawable.download_ic_download_completed)
            return
        }
        DownloadConstant.CHAPTER_STATUS_DOWNLOADING -> {
            setImageResource(R.drawable.shape_base_net_error_loading)
            return
        }
        DownloadConstant.CHAPTER_STATUS_UN_SELECT, DownloadConstant.CHAPTER_STATUS_SELECTED -> {
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
    }
}

@BindingAdapter("bindDownloadChapterStatus")
fun ImageView.bindDownloadChapterStatus(chapterStatusModel: DownloadChapterStatusModel) {

    when (chapterStatusModel.status) {
        DownloadConstant.CHAPTER_STATUS_DOWNLOAD_FINISH -> {
            setImageResource(R.drawable.download_ic_item_disable)
        }
        DownloadConstant.CHAPTER_STATUS_UN_SELECT -> {
            setImageResource(R.drawable.download_ic_item_unchecked)
        }
        DownloadConstant.CHAPTER_STATUS_SELECTED -> {
            setImageResource(R.drawable.download_ic_item_checked)
        }
        else -> {
            setImageResource(R.drawable.download_ic_item_unchecked)
        }
    }
}
