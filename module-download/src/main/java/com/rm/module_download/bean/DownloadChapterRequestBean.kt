package com.rm.module_download.bean

data class DownloadChapterRequestBean(var audio_id: Long, var start_sequence: Int, var end_sequence: Int, var type: Int)
