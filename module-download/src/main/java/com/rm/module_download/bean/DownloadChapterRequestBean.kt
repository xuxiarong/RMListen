package com.rm.module_download.bean

data class DownloadChapterRequestBean(var audio_id: String, var start_sequence: Int, var end_sequence: Int, var sequences: List<Int>, var type: Int)