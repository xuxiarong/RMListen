package com.rm.business_lib.aria

import android.text.TextUtils
import com.arialyy.annotations.Download
import com.arialyy.aria.core.Aria
import com.arialyy.aria.core.task.DownloadTask
import com.rm.baselisten.BaseApplication
import com.rm.baselisten.util.DLog
import com.rm.business_lib.bean.BusinessVersionUrlBean
import com.rm.business_lib.db.download.DownloadChapter
import com.rm.business_lib.download.file.DownLoadFileUtils
import java.io.File


/**
 * desc   :
 * date   : 2020/12/07
 * version: 1.0
 */
object AriaUploadVersionDownloadManager {

    private var downloadUrl: String? = ""

    var mDownloadStart: () -> Unit = {}
    var mDownloadProgress: (Int) -> Unit = {}
    var mDownloadComplete: (String) -> Unit = {}
    var mDownloadFail: () -> Unit = {}

    fun startDownload(
        uploadUrl: String,
        version: String,
        downloadStart: (() -> Unit)? = null,
        downloadProgress: ((Int) -> Unit)? = null,
        downloadComplete: ((String) -> Unit)? = null,
        downloadFail: (() -> Unit)? = null
    ) {
        if (downloadStart != null) {
            mDownloadStart = downloadStart
        }
        if (downloadProgress != null) {
            mDownloadProgress = downloadProgress
        }
        if (downloadComplete != null) {
            mDownloadComplete = downloadComplete
        }
        if (downloadFail != null) {
            mDownloadFail = downloadFail
        }
        val file = File(
            DownLoadFileUtils.createFileWithAudio(
                "download/upload"
            ).absolutePath,
            "V$version.apk"
        )
        Aria.download(this).register()
        downloadUrl = uploadUrl
        Aria.download(this)
            .load(uploadUrl) //读取下载地址
            .setFilePath(file.absolutePath) //设置文件保存的完整路径
            .create() //创建并启动下载
    }


    //在这里处理任务执行中的状态，如进度进度条的刷新
    @Download.onTaskRunning
    fun taskRunning(task: DownloadTask?) {
        if (TextUtils.equals(downloadUrl, task?.key)) {
            val percent = task?.percent ?: 0   //任务进度百分比
            mDownloadProgress(percent)

        }
    }

    @Download.onTaskComplete
    fun taskComplete(task: DownloadTask?) {
        if (TextUtils.equals(downloadUrl, task?.key)) {
            DLog.i("====>onTaskComplete", "----->${task?.filePath}")
            mDownloadProgress(100)
            mDownloadComplete(task?.filePath ?: "")
        }
    }

    @Download.onTaskStart
    fun taskStart(task: DownloadTask?) {
        mDownloadStart()
    }

    @Download.onTaskFail
    fun taskFail(task: DownloadTask?) {
        mDownloadFail()
    }

}