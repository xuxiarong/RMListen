package com.rm.business_lib.aria

import androidx.databinding.ObservableBoolean
import com.arialyy.annotations.Download
import com.arialyy.aria.core.Aria
import com.arialyy.aria.core.task.DownloadTask
import com.rm.baselisten.BaseApplication
import com.rm.baselisten.util.ConvertUtils
import com.rm.baselisten.util.DLog
import com.rm.baselisten.util.NetWorkUtils
import com.rm.business_lib.db.download.DownloadChapter
import com.rm.business_lib.download.DownloadMemoryCache
import com.rm.business_lib.download.file.DownLoadFileUtils
import java.io.File


/**
 * desc   :
 * date   : 2020/12/07
 * version: 1.0
 */
object AriaDownloadManager {

    const val TAG = "suolong_AriaDownloadManager"
    //是否在下载状态
    var isDownloading = ObservableBoolean(false)
    //是否是处于单个任务下载
    var isSingleDown = ObservableBoolean(false)

    init {
    }

    fun startDownload(chapter: DownloadChapter,isSingleDownload : Boolean = false) {

        this.isSingleDown.set(isSingleDownload)
        if (chapter.path_url == DownloadMemoryCache.downloadingChapter.get()?.path_url) {
            Aria.download(this).resumeAllTask()
        } else {
            Aria.download(this).stopAllTask()
            val file = File(DownLoadFileUtils.createFileWithAudio(chapter.audio_id.toString()).absolutePath, chapter.chapter_name)
            Aria.download(this).register()
            DLog.d(TAG, "register filepath = ${file.absolutePath}")
            Aria.download(this).load(chapter.path_url) //读取下载地址
                    .setFilePath(file.absolutePath) //设置文件保存的完整路径
                    .create() //创建并启动下载
            DownloadMemoryCache.downloadingChapter.set(chapter)
            DownloadMemoryCache.downloadingChapter.notifyChange()
        }
    }

    fun pauseDownloadChapter(chapter: DownloadChapter) {
        Aria.download(this).stopAllTask()
    }

    fun stopAll() {
        Aria.download(this).stopAllTask()
    }

    fun deleteDownload(chapter: DownloadChapter) {
        Aria.download(this).removeAllTask(true)
    }

    fun deleteAllDownload() {
        Aria.download(this).removeAllTask(true)
    }

    fun resumeDownloadChapter() {
        Aria.download(this).resumeAllTask()
    }

    //在这里处理任务执行中的状态，如进度进度条的刷新
    @Download.onTaskRunning
    fun taskRunning(task: DownloadTask) {
        DLog.d(TAG, "taskRunning")
        if (task.key == (DownloadMemoryCache.downloadingChapter.get()?.path_url)) {
            isDownloading.set(true)
            val percent = task.percent    //任务进度百分比
            val convertSpeed = task.convertSpeed    //转换单位后的下载速度，单位转换需要在配置文件中打开
            val speed = ConvertUtils.byte2FitMemorySize(task.speed, 1) //原始byte长度速度
            val size = task.fileSize
            DownloadMemoryCache.updateDownloadingSpeed(url = task.key, speed = speed, currentOffset = size * percent / 100)
            DLog.d(TAG, "taskRunning  percent = $percent convertSpeed = $convertSpeed speed = $speed")
        }
    }

    @Download.onTaskComplete
    fun taskComplete(task: DownloadTask) {
        DLog.d(TAG, "taskComplete")
        if (task.key == (DownloadMemoryCache.downloadingChapter.get()?.path_url)) {
            DownloadMemoryCache.setDownloadFinishChapter(task.key)
        }
    }

    @Download.onTaskCancel
    fun onTaskCancel(task: DownloadTask) {
        isDownloading.set(false)
        DLog.d(TAG, "onTaskCancel")
    }

    @Download.onTaskStop
    fun onTaskStop(task: DownloadTask) {
        isDownloading.set(false)
        DLog.d(TAG, "onTaskStop")
    }

    @Download.onTaskStart
    fun taskStart(task: DownloadTask) {
        if (task.key == (DownloadMemoryCache.downloadingChapter.get()?.path_url)) {
            isDownloading.set(true)
        }
        DLog.d(TAG, "taskStart")
    }

    @Download.onTaskFail
    fun onTaskFail(task: DownloadTask) {
        isDownloading.set(false)
        if(!NetWorkUtils.isNetworkAvailable(BaseApplication.CONTEXT)){
               BaseApplication
        }
        DLog.d(TAG, "onTaskFail")
    }
}