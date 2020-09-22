package com.rm.module_download.bean

data class DownloadChapterResponseBean(
    var list: List<DownloadChapterItemBean>,
    var total: Long
)