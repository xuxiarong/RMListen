package com.rm.business_lib.binding

import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.flyco.roundview.RoundTextView
import com.rm.business_lib.R
import com.rm.business_lib.bean.ChapterList
import com.rm.business_lib.bean.HomeDetailList
import com.rm.business_lib.db.download.DownloadChapter
import com.rm.business_lib.download.DownloadMemoryCache
import com.rm.business_lib.download.file.DownLoadFileUtils
import com.rm.business_lib.wedgit.download.DownloadStatusView

/**
 * desc   :
 * date   : 2020/10/30
 * version: 1.0
 */

@BindingAdapter("bindDownloadAudio","bindDownloadStatusChapter", "bindCurrentDownChapter",requireAll = true)
fun DownloadStatusView.bindChapterList(
    downloadAudio: HomeDetailList?,
    chapter: ChapterList,
    downloadChapter: DownloadChapter?
) {
    if(downloadAudio !=null){
        this.audio = downloadAudio
    }
    val checkChapter = DownLoadFileUtils.checkChapterIsDownload(ChapterList.toDownChapter(chapter))
    if (downloadChapter != null && checkChapter.chapter_id == downloadChapter.chapter_id) {
        chapter.down_status = downloadChapter.down_status
        chapter.current_offset = downloadChapter.current_offset
    }
    setDownloadStatus(checkChapter)
}

@BindingAdapter("bindDownloadNum")
fun RoundTextView.bindDownloadNum(num : Int?){

    if(num == null){
        visibility = View.GONE
        return
    }

    if(num <= 0){
        DownloadMemoryCache.downloadingNum.set(0)
        visibility = View.GONE
        return
    }
    visibility = View.VISIBLE

    if(num>99){
        text = "99+"
    }else{
        text = num.toString()
    }

}

@BindingAdapter("bindDownDeleteNum")
fun TextView.bind(num : Int?){
    if(num == null){
        visibility = View.GONE
        return
    }
    if(num == 0){
        visibility = View.GONE
        return
    }
    visibility = View.VISIBLE
    text = String.format(context.getString(R.string.download_delete_number),num)
}