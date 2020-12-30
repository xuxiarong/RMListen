package com.rm.business_lib.binding

import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.flyco.roundview.RoundTextView
import com.rm.business_lib.R
import com.rm.business_lib.db.download.DownloadAudio
import com.rm.business_lib.db.download.DownloadChapter
import com.rm.business_lib.download.DownloadMemoryCache
import com.rm.business_lib.download.file.DownLoadFileUtils
import com.rm.business_lib.wedgit.download.DownloadStatusView

/**
 * desc   :
 * date   : 2020/10/30
 * version: 1.0
 */

@BindingAdapter("bindDownloadAudio", "bindDownloadStatusChapter", "bindCurrentDownChapter", requireAll = true)
fun DownloadStatusView.bindChapterList(
        downloadAudio: DownloadAudio?,
        chapter: DownloadChapter,
        downloadChapter: DownloadChapter?
) {
    if (downloadAudio != null) {
        this.audio = downloadAudio
    }
    if (downloadChapter != null && chapter.chapter_id == downloadChapter.chapter_id) {
        chapter.current_offset = downloadChapter.current_offset
        chapter.down_status = downloadChapter.down_status
    }
    setDownloadStatus(chapter)

}

@BindingAdapter("bindDownloadNum")
fun RoundTextView.bindDownloadNum(list: List<Any>?) {

    if (list == null) {
        visibility = View.GONE
        return
    }

    if (list.isEmpty()) {
        visibility = View.GONE
        return
    }
    visibility = View.VISIBLE

    if (list.size > 99) {
        text = "99+"
    } else {
        text = list.size.toString()
    }

}

@BindingAdapter("bindDownDeleteNum")
fun TextView.bind(num: Int?) {
    if (num == null) {
        visibility = View.GONE
        return
    }
    if (num == 0) {
        visibility = View.GONE
        return
    }
    visibility = View.VISIBLE
    text = String.format(context.getString(R.string.download_delete_number), num)
}