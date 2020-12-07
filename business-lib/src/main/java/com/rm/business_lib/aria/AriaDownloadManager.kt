package com.rm.business_lib.aria

import com.arialyy.annotations.Download
import com.arialyy.aria.core.Aria
import com.arialyy.aria.core.task.DownloadTask
import com.rm.baselisten.BaseApplication
import com.rm.baselisten.util.DLog
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

    lateinit var currentChapter: DownloadChapter
    const val TAG = "suolong_AriaDownloadManager"   
    
    init {
        BaseApplication.CONTEXT
    }

    fun startDownload(chapter: DownloadChapter) {
        val file = File(
            DownLoadFileUtils.createFileWithAudio(chapter.audio_id.toString()).absolutePath,
            chapter.chapter_name
        )
        Aria.download(this).register()
        DLog.d(TAG, "register filepath = ${file.absolutePath}")
        val taskId: Long = Aria.download(this)
            .load(chapter.path_url) //读取下载地址
            .setFilePath(file.absolutePath) //设置文件保存的完整路径
            .create() //创建并启动下载
        currentChapter = chapter
        DownloadMemoryCache.downloadingChapter.set(chapter)
    }

    fun pauseDownloadChapter(chapter: DownloadChapter) {
        Aria.download(this).stopAllTask()
    }

    fun resumeDownloadChapter(){
        Aria.download(this).resumeAllTask()
    }

    //在这里处理任务执行中的状态，如进度进度条的刷新
    @Download.onTaskRunning
    fun taskRunning(task: DownloadTask) {
        DLog.d(TAG, "taskRunning")
        if (task.key == (currentChapter.path_url)) {
            val percent = task.percent    //任务进度百分比
            val convertSpeed = task.convertSpeed    //转换单位后的下载速度，单位转换需要在配置文件中打开
            val speed = task.speed //原始byte长度速度
            val size = task.fileSize
            DLog.d(TAG, "taskRunning  percent = $percent convertSpeed = $convertSpeed speed = $speed")
        }
    }

    @Download.onTaskComplete
    fun taskComplete(task: DownloadTask) {
        DLog.d(TAG, "taskComplete")
        if (task.key == (currentChapter.path_url)) {
            DownloadMemoryCache.setDownloadFinishChapter(task.key)
        }
    }

    @Download.onTaskStart
    fun taskStart(task: DownloadTask) {
        DLog.d(TAG, "taskStart")
    }

}