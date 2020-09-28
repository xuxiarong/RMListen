package com.rm.module_download.bean

data class DownloadChapterResponseBean(
    var list: MutableList<DownloadChapterItemBean>,
    var total: Int
)