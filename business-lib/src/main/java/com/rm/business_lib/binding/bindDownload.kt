package com.rm.business_lib.binding

import androidx.databinding.BindingAdapter
import com.rm.business_lib.bean.ChapterList
import com.rm.business_lib.db.download.DownloadChapter
import com.rm.business_lib.download.file.DownLoadFileUtils
import com.rm.business_lib.wedgit.download.DownloadStatusView

/**
 * desc   :
 * date   : 2020/10/30
 * version: 1.0
 */

@BindingAdapter("bindChapterList", "bindCurrentDownChapter")
fun DownloadStatusView.bindChapterList(
    chapter: ChapterList,
    downloadChapter: DownloadChapter?
) {
    val checkChapter = DownLoadFileUtils.checkChapterIsDownload(ChapterList.toDownChapter(chapter))
    if (downloadChapter != null && checkChapter.chapter_id == downloadChapter.chapter_id) {
        chapter.down_status = downloadChapter.down_status
        chapter.current_offset = downloadChapter.current_offset
    }
    setDownloadStatus(checkChapter)
}