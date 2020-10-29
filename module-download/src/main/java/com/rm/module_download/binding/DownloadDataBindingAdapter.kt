package com.rm.module_download.binding

import android.annotation.SuppressLint
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.rm.baselisten.util.ConvertUtils
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

@BindingAdapter("bindDownAll")
fun TextView.bindDownAll(chapter: DownloadChapter?){
    if(chapter ==null){
        text = context.getText(R.string.download_continue_down)
        return
    }
    text = if (chapter.isDownloading) {
        context.getText(R.string.download_pause_all)
    }else{
        context.getText(R.string.download_continue_down)
    }
}
@BindingAdapter("bindDownAll")
fun ImageView.bindDownAll(chapter: DownloadChapter?){
    if(chapter ==null){
        setImageResource(R.drawable.download_ic_start_download)
        return
    }
    if (chapter.isDownloading) {
        setImageResource(R.drawable.download_ic_pause_download)
    }else{
        setImageResource(R.drawable.download_ic_start_download)
    }
}


@BindingAdapter("bindDownloadingSize")
fun TextView.bindDownloadingSize(downList : List<DownloadChapter>?){
    if(downList == null){
        visibility = View.GONE
        return
    }
    if(downList.isEmpty()){
        visibility = View.GONE
        return
    }
    visibility = View.GONE
    text = String.format(context.getString(R.string.download_downloading_number),downList.size)
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

@BindingAdapter("bindItemChapter", "bindDownChapter")
fun ImageView.bindDownOrPause(
    chapter: DownloadChapter,
    downloadChapter: DownloadChapter?
) {
    if(downloadChapter ==null){
        return
    }
    if(chapter.chapter_id == downloadChapter.chapter_id){
        chapter.down_status = downloadChapter.down_status
    }
    when (chapter.down_status) {
        DownloadConstant.CHAPTER_STATUS_DOWNLOADING -> {
            setImageResource(R.drawable.download_ic_pause_download)
        }
        DownloadConstant.CHAPTER_STATUS_DOWNLOAD_PAUSE ->{
            setImageResource(R.drawable.download_ic_start_download)
        }
    }
}


@BindingAdapter("bindDownChapter", "bindDownProgressChapter")
fun ProgressBar.bindDownProgressChapter(
    chapter: DownloadChapter,
    downloadChapter: DownloadChapter?
) {
    if(downloadChapter ==null){
        return
    }
    if (chapter.chapter_id == downloadChapter.chapter_id) {
        chapter.down_status = downloadChapter.down_status
        chapter.down_speed = downloadChapter.down_speed
        chapter.current_offset = downloadChapter.current_offset
    }

    when (chapter.down_status) {
        DownloadConstant.CHAPTER_STATUS_DOWNLOADING,DownloadConstant.CHAPTER_STATUS_DOWNLOAD_PAUSE -> {
            progress = (chapter.current_offset/(chapter.size/100)).toInt()
        }
    }
}


@BindingAdapter("bindDownloadItem", "bindDownloadChapter")
fun TextView.bindDownloadCurrentSize(chapter: DownloadChapter, downloadChapter: DownloadChapter?) {
    if (downloadChapter != null && chapter.chapter_id == downloadChapter.chapter_id) {
        chapter.down_status = downloadChapter.down_status
        chapter.down_speed = downloadChapter.down_speed
        chapter.current_offset = downloadChapter.current_offset
    }
    visibility = View.GONE

    when (chapter.down_status) {
        DownloadConstant.CHAPTER_STATUS_NOT_DOWNLOAD -> {
            text = ""
        }
        DownloadConstant.CHAPTER_STATUS_DOWNLOAD_WAIT -> {
            text = context.getString(R.string.business_download_wait)
        }
        DownloadConstant.CHAPTER_STATUS_DOWNLOADING -> {
            if (downloadChapter != null) {
                visibility = View.VISIBLE
                chapter.current_offset = chapter.current_offset
                val currentSize = ConvertUtils.byte2FitMemorySize(chapter.current_offset, 1)
                val totalSize = ConvertUtils.byte2FitMemorySize(chapter.size, 1)
                text = String.format(
                    context.getString(R.string.business_down_and_total_size),
                    currentSize,
                    totalSize
                )
            }
        }
        DownloadConstant.CHAPTER_STATUS_DOWNLOAD_PAUSE -> {
            text = context.getString(R.string.business_download_pause)
            visibility = View.VISIBLE
            val currentSize = ConvertUtils.byte2FitMemorySize(chapter.current_offset, 1)
            val totalSize = ConvertUtils.byte2FitMemorySize(chapter.size, 1)
            text = String.format(
                context.getString(R.string.business_down_and_total_size),
                currentSize,
                totalSize
            )
        }
        DownloadConstant.CHAPTER_STATUS_DOWNLOAD_FINISH -> {
            text = ""
        }
    }
}

@BindingAdapter("bindDownloadText", "bindDownloadSpeedChapter")
fun TextView.bindDownloadText(chapter: DownloadChapter, downloadChapter: DownloadChapter?) {
    if (downloadChapter != null && chapter.chapter_id == downloadChapter.chapter_id) {
        chapter.down_status = downloadChapter.down_status
        chapter.down_speed = downloadChapter.down_speed
    }

    when (chapter.down_status) {
        DownloadConstant.CHAPTER_STATUS_NOT_DOWNLOAD -> {
            text = ""
        }
        DownloadConstant.CHAPTER_STATUS_DOWNLOAD_WAIT -> {
            text = context.getString(R.string.business_download_wait)
        }
        DownloadConstant.CHAPTER_STATUS_DOWNLOADING -> {
            if (downloadChapter != null) {
                text = chapter.down_speed
            }
        }
        DownloadConstant.CHAPTER_STATUS_DOWNLOAD_PAUSE -> {
            text = context.getString(R.string.business_download_pause)
        }
        DownloadConstant.CHAPTER_STATUS_DOWNLOAD_FINISH -> {
            text = ""
        }
    }
}

@SuppressLint("SetTextI18n")
@BindingAdapter("bindDownloadListen")
fun TextView.bindDownloadListen(chapter : DownloadChapter){
    text = ""
    try {
        var result = ((chapter.listen_duration * 1.0f) / (chapter.duration * 1000L) * 100).toInt()
        if(result <= 0){
            result =1
        }
        if(result == 100){
            text = context.getString(R.string.download_listen_finish)
            setTextColor(ContextCompat.getColor(context,R.color.business_color_b1b1b1))
        }else {
            setTextColor(ContextCompat.getColor(context,R.color.business_color_ffba56))
            text = "${String.format(context.getString(R.string.download_listen_progress),result)}%"
        }

    }catch (e : Exception){
        e.printStackTrace()
    }
}