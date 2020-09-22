package com.rm.business_lib.bean.download

/**
 * 音频文件下载bean
 */
data class DownloadAudioBean(
    var url: String,
    var audioId: String,
    var audioName: String,
    var bookName: String,
    var downloadUIStatus: DownloadUIStatus
)