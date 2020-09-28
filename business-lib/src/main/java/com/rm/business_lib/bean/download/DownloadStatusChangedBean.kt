package com.rm.business_lib.bean.download

/**
 * 下载状态改变
 */
data class DownloadStatusChangedBean(var url:String,var downloadUIStatus: DownloadUIStatus)