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

    init {
        BaseApplication.CONTEXT
    }

    private var downloadUrl: String? = ""

    var mDownloadProgress: (Int) -> Unit = {}
    var mDownloadComplete: (String) -> Unit = {}

    fun startDownload(
        uploadUrl: String,
        version: String,
        downloadProgress: ((Int) -> Unit)?,
        downloadComplete: ((String) -> Unit)?
    ) {
        if (downloadProgress != null) {
            mDownloadProgress = downloadProgress
        }
        if (downloadComplete != null) {
            mDownloadComplete = downloadComplete
        }
        val file = File(
            DownLoadFileUtils.createFileWithAudio(
                System.currentTimeMillis().toString()
            ).absolutePath,
            version
        )
        Aria.download(this).register()
        downloadUrl = uploadUrl
        val taskId: Long = Aria.download(this)
            .load(uploadUrl) //读取下载地址
            .setFilePath(file.absolutePath) //设置文件保存的完整路径
            .create() //创建并启动下载
    }

    fun pauseDownloadChapter(chapter: DownloadChapter) {
        Aria.download(this).stopAllTask()
    }

    fun resumeDownloadChapter() {
        Aria.download(this).resumeAllTask()
    }

    //在这里处理任务执行中的状态，如进度进度条的刷新
    @Download.onTaskRunning
    fun taskRunning(task: DownloadTask) {
        if (TextUtils.equals(downloadUrl, task.key)) {
            val percent = task.percent    //任务进度百分比
            val convertSpeed = task.convertSpeed    //转换单位后的下载速度，单位转换需要在配置文件中打开
            val speed = task.speed //原始byte长度速度
            val size = task.fileSize

            mDownloadProgress(percent)
            DLog.i(
                "====>taskRunning",
                "percent:$percent     convertSpeed:$convertSpeed   speed:$speed   size:$size"
            )
        }
    }

    @Download.onTaskComplete
    fun taskComplete(task: DownloadTask) {
        if (TextUtils.equals(downloadUrl, task.key)) {
            DLog.i("====>onTaskComplete", "----->${task.filePath}")
            mDownloadProgress(100)
            mDownloadComplete(task.filePath)
        }
    }

    @Download.onTaskStart
    fun taskStart(task: DownloadTask) {
        DLog.i("====>onTaskStart", "----->")

    }

}