package com.rm.module_download.bean

data class DownloadChapterRequestBean(var audio_id: Long, var start_sequence: Int, var end_sequence: Int, var sequences: List<Int>, var type: Int)
data class DownloadAudioRequestBean(var audio_id: Long, var page: Int = 1, var page_size: Int = 12, var chapter_id: Long = 0L, var sort: String = "asc")