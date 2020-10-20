package com.rm.module_download.bean

import com.rm.business_lib.db.download.DownloadChapter

data class DownloadChapterResponseBean(
    var list: MutableList<DownloadChapterItemBean>,
    var total: Int
)

data class DownloadAudioResponseBean(
    var list: MutableList<DownloadChapter>,
    var total: Int
)