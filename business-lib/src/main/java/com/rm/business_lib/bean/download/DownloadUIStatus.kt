package com.rm.business_lib.bean.download

/**
 *
 * 文件下载状态类型
 */
enum class DownloadUIStatus {
    DOWNLOAD_COMPLETED,//已完成
    DOWNLOAD_PAUSED,//已暂停
    DOWNLOAD_IN_PROGRESS,//正在下载
    DOWNLOAD_PENDING,//等待中，已加入下载队列
}