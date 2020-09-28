package com.rm.business_lib.bean.download

/**
 * 文件下载进度bean
 */
data class DownloadProgressUpdateBean(var url: String, var offset: Long, var speed: String)