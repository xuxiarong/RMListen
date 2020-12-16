package com.rm.module_download.binding

import android.annotation.SuppressLint
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.databinding.BindingAdapter
import com.rm.baselisten.util.ConvertUtils
import com.rm.baselisten.util.SDCardUtils
import com.rm.business_lib.db.download.DownloadAudio
import com.rm.business_lib.db.download.DownloadChapter
import com.rm.business_lib.download.DownloadConstant
import com.rm.business_lib.download.file.DownLoadFileUtils
import com.rm.business_lib.wedgit.download.DownloadStatusView
import com.rm.module_download.R
import com.rm.module_download.bean.DownloadChapterUIStatus
import com.tencent.bugly.proguard.t

@BindingAdapter("bindDownloadCheckSrc")
fun ImageView.bindDownloadCheckSrc(status: DownloadChapterUIStatus) {
    when (status) {
        DownloadChapterUIStatus.DOWNLOADING, DownloadChapterUIStatus.COMPLETED -> setImageResource(R.drawable.download_ic_item_disable)
        DownloadChapterUIStatus.UNCHECK -> setImageResource(R.drawable.download_ic_item_unchecked)
        DownloadChapterUIStatus.CHECKED -> setImageResource(R.drawable.download_ic_item_checked)
    }
}

@BindingAdapter("bindDownAll")
fun TextView.bindDownAll(isDownAll: Boolean?) {
    if (isDownAll == null) {
        text = context.getText(R.string.download_continue_down)
        return
    }
    text = if (isDownAll) {
        context.getText(R.string.download_pause_all)
    } else {
        context.getText(R.string.download_continue_down)
    }
}

@BindingAdapter("bindDownAll")
fun ImageView.bindDownAll(isDownAll: Boolean?) {
    if (isDownAll == null) {
        setImageResource(R.drawable.download_ic_start_download)
        return
    }
    if (isDownAll) {
        setImageResource(R.drawable.download_ic_pause_download)
    } else {
        setImageResource(R.drawable.download_ic_start_download)
    }
}


@BindingAdapter("bindDownloadingSize")
fun TextView.bindDownloadingSize(downList: List<DownloadChapter>?) {
    if (downList == null) {
        visibility = View.GONE
        return
    }
    if (downList.isEmpty()) {
        visibility = View.GONE
        return
    }
    visibility = View.GONE
    text = String.format(context.getString(R.string.download_downloading_number), downList.size)
}


@BindingAdapter("bindDownloadStatusSrc")
fun ImageView.bindDownloadStatusSrc(chapter: DownloadChapter) {
    visibility = View.GONE
    when (chapter.down_status) {
        DownloadConstant.CHAPTER_STATUS_NOT_DOWNLOAD -> {
            when (chapter.need_pay) {
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


@BindingAdapter("bindDownloadChapterStatus", "bindDownloadChapterSelectAll")
fun ImageView.bindDownloadChapterStatus(chapter: DownloadChapter, isSelectAll: Boolean) {
    DownLoadFileUtils.checkChapterIsDownload(chapter)
    if (chapter.down_status != DownloadConstant.CHAPTER_STATUS_NOT_DOWNLOAD) {
        setImageResource(R.drawable.download_ic_item_disable)
        return
    }
    if (chapter.chapter_edit_select || isSelectAll) {
        chapter.chapter_edit_select = true
        setImageResource(R.drawable.download_ic_item_checked)
    } else {
        chapter.chapter_edit_select = false
        setImageResource(R.drawable.download_ic_item_unchecked)
    }
}

@BindingAdapter("bindItemChapter", "bindDownChapter","bindDownloadAll",requireAll = true)
fun ImageView.bindDownOrPause(
        chapter: DownloadChapter,
        downloadChapter: DownloadChapter?,
        isDownAll: Boolean?
) {
    if (downloadChapter == null) {
        return
    }
    if(isDownAll!=null && !isDownAll){
        setImageResource(R.drawable.download_ic_start_download)
        return
    }

    if (chapter.chapter_id == downloadChapter.chapter_id) {
        chapter.down_status = downloadChapter.down_status
    }
    when (chapter.down_status) {
        DownloadConstant.CHAPTER_STATUS_DOWNLOADING, DownloadConstant.CHAPTER_STATUS_DOWNLOAD_WAIT -> {
            setImageResource(R.drawable.download_ic_pause_download)
        }
        DownloadConstant.CHAPTER_STATUS_DOWNLOAD_PAUSE -> {
            setImageResource(R.drawable.download_ic_start_download)
        }
    }
}


@BindingAdapter("bindDownChapter", "bindDownProgressChapter")
fun ProgressBar.bindDownProgressChapter(
        chapter: DownloadChapter,
        downloadChapter: DownloadChapter?
) {
    if (downloadChapter == null) {
        return
    }
    try {
        if (chapter.chapter_id == downloadChapter.chapter_id) {
            chapter.down_status = downloadChapter.down_status
            chapter.down_speed = downloadChapter.down_speed
            chapter.current_offset = downloadChapter.current_offset
        }
        progress = (chapter.current_offset / (chapter.size / 100)).toInt()
    }catch (e : Exception){
        progress = 0
    }
}


@BindingAdapter("bindDownloadItem", "bindDownloadChapter")
fun TextView.bindDownloadCurrentSize(chapter: DownloadChapter, downloadChapter: DownloadChapter?) {
    if (downloadChapter != null && chapter.chapter_id == downloadChapter.chapter_id) {
        chapter.down_status = downloadChapter.down_status
        chapter.down_speed = downloadChapter.down_speed
        chapter.current_offset = downloadChapter.current_offset
    }

    chapter.current_offset = chapter.current_offset
    val currentSize = ConvertUtils.byte2FitMemorySize(chapter.current_offset, 1)
    val totalSize = ConvertUtils.byte2FitMemorySize(chapter.size, 1)
    text = String.format(context.getString(R.string.business_down_and_total_size), currentSize, totalSize)
}

@BindingAdapter("bindDownloadText", "bindDownloadSpeedChapter","bindDownloadAll",requireAll = true)
fun TextView.bindDownloadText(chapter: DownloadChapter, downloadChapter: DownloadChapter?,isDownAll: Boolean?) {
    if(isDownAll!=null && !isDownAll){
        text = context.getString(R.string.business_download_pause)
        return
    }
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
fun TextView.bindDownloadListen(chapter: DownloadChapter) {
    text = ""
    try {
        var result = ((chapter.listen_duration * 1.0f) / (chapter.duration * 1000L) * 100).toInt()
        if (result <= 0) {
            result = 1
        }
        if (result == 100) {
            text = context.getString(R.string.download_listen_finish)
            setTextColor(ContextCompat.getColor(context, R.color.business_color_b1b1b1))
        } else {
            setTextColor(ContextCompat.getColor(context, R.color.business_color_ffba56))
            text = "${String.format(context.getString(R.string.download_listen_progress), result)}%"
        }

    } catch (e: Exception) {
        e.printStackTrace()
    }
}

@BindingAdapter("bindDownloadStatusChapter", "bindCurrentDownChapter")
fun DownloadStatusView.bindDownloadStatusChapter(
        chapter: DownloadChapter,
        downloadChapter: DownloadChapter?
) {
    DownLoadFileUtils.checkChapterIsDownload(chapter)

    if (downloadChapter != null && chapter.chapter_id == downloadChapter.chapter_id) {
        chapter.down_status = downloadChapter.down_status
        chapter.down_speed = downloadChapter.down_speed
        chapter.current_offset = downloadChapter.current_offset
    }
    setDownloadStatus(chapter)
}

@BindingAdapter("bindChapterList", "bindCurrentDownChapter")
fun DownloadStatusView.bindChapterList(
        chapter: DownloadChapter,
        downloadChapter: DownloadChapter?
) {
    val checkChapter = DownLoadFileUtils.checkChapterIsDownload(chapter)
    if (downloadChapter != null && checkChapter.chapter_id == downloadChapter.chapter_id) {
        chapter.down_status = downloadChapter.down_status
        chapter.current_offset = downloadChapter.current_offset
    }
    setDownloadStatus(checkChapter)
}

@BindingAdapter("bindDownloadMaxSequence")
fun EditText.bindDownloadMaxSequence(maxSelectSequence: String) {

    addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            s?.let {
                try {
                    if (!TextUtils.isEmpty(s)) {
                        val result = s.toString().toInt()
                        if (result > maxSelectSequence.toInt()) {
                            setText(maxSelectSequence)
                        }
                        setSelection(s.length)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    })

}

@BindingAdapter("bindDownSelectChapterSize")
fun TextView.bindDownSelectChapterSize(size: Long) {
    try {
        text = String.format(context.getString(R.string.download_audio_size_and_total_size),
                ConvertUtils.byte2FitMemorySize(size, 1),
                ConvertUtils.byte2FitMemorySize(SDCardUtils.getExternalAvailableSize(), 1))
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

@BindingAdapter("bindDownFinishAudioSize")
fun TextView.bindDownFinishAudioSize(list: List<DownloadAudio>?) {
    if (list != null && list.isNotEmpty()) {
        try {
            var totalSize = 0L
            list.forEach { audio ->
                audio.chapterList?.forEach { chapter ->
                    if (chapter.isDownloadFinish) {
                        totalSize += chapter.size
                    }
                }
            }
            text = String.format(context.getString(R.string.download_finish_audio_size),
                    ConvertUtils.byte2FitMemorySize(totalSize, 1),
                    ConvertUtils.byte2FitMemorySize(SDCardUtils.getExternalAvailableSize(), 1))
        } catch (e: Exception) {
            e.printStackTrace()
            text = ""
        }
    } else {
        text = ""
    }


}
