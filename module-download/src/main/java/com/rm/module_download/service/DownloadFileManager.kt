package com.rm.module_download.service

import android.util.ArrayMap
import com.liulishuo.okdownload.*
import com.liulishuo.okdownload.core.breakpoint.BlockInfo
import com.liulishuo.okdownload.core.breakpoint.BreakpointInfo
import com.liulishuo.okdownload.core.cause.EndCause
import com.liulishuo.okdownload.core.dispatcher.DownloadDispatcher
import com.liulishuo.okdownload.core.listener.DownloadListener4WithSpeed
import com.liulishuo.okdownload.core.listener.assist.Listener4SpeedAssistExtend
import com.rm.baselisten.BaseApplication
import com.rm.baselisten.util.DLog
import com.rm.business_lib.DownloadMemoryCache
import com.rm.business_lib.bean.download.DownloadChapterStatusModel
import com.rm.business_lib.bean.download.DownloadProgressUpdateBean
import com.rm.business_lib.bean.download.DownloadStatusChangedBean
import com.rm.business_lib.bean.download.DownloadUIStatus
import com.rm.business_lib.downloadProgress
import com.rm.business_lib.downloadStatus
import com.rm.module_download.util.TagUtil
import com.rm.module_download.util.getParentFile
import java.io.File

class DownloadFileManager private constructor() : DownloadListener4WithSpeed() {

    private val taskList = ArrayMap<String, DownloadTask>()
    private val context = BaseApplication.CONTEXT

    private val queueListener: DownloadListener by lazy { this@DownloadFileManager }



    companion object {
        @JvmStatic
        val INSTANCE: DownloadFileManager by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { DownloadFileManager() }
        const val minIntervalMillisCallbackProcess = 200  //回掉间隔毫秒数
        const val maxParallelRunningCount = 1  //最大并行数
        const val TAG = "DownloadFileManager"
    }

    init {
        //并行下载，并发下载数设置为1，来实现串行下载
        DownloadDispatcher.setMaxParallelRunningCount(maxParallelRunningCount)
    }

    private fun createTask(url: String, parentPath: String, fileName: String): DownloadTask {
        return DownloadTask.Builder(url, createFileWithAudio(parentPath).absolutePath, fileName)
            .setMinIntervalMillisCallbackProcess(minIntervalMillisCallbackProcess)
            .setAutoCallbackToUIThread(true)//是否在UI线程回掉
            .build()
    }

    private fun createFileWithAudio(audioName :String ) : File{
        return File(getParentFile(context), audioName)
    }

    private fun createFinder(url: String, audioName: String, fileName: String): DownloadTask {
        return if (taskList.contains(url)) {
            taskList[url]!!
        } else {
            DownloadTask.Builder(url, createFileWithAudio(audioName).absolutePath, fileName).build()
        }
    }


    fun download(model: DownloadChapterStatusModel) {
        createTask(model.chapter.path_url, model.chapter.audio_id.toString(),model.chapter.chapter_name).also {
            TagUtil.saveHolder(it, model.chapter)
            taskList[model.chapter.path_url] = it
            downloadStatus.value=(DownloadStatusChangedBean(model.chapter.path_url, DownloadUIStatus.DOWNLOAD_PENDING))
        }.run {
            enqueue(queueListener)
        }
    }

    fun download(modelList: List<DownloadChapterStatusModel>) {
        var taskList = Array(modelList.size) { index ->
            createTask(modelList[index].chapter.path_url, modelList[index].chapter.audio_id.toString(),modelList[index].chapter.chapter_name).also {
                TagUtil.saveHolder(it, modelList[index])
                taskList[modelList[index].chapter.path_url] = it
                downloadStatus.value=(DownloadStatusChangedBean(modelList[index].chapter.path_url, DownloadUIStatus.DOWNLOAD_PENDING))
            }
        }
        DownloadTask.enqueue(taskList, queueListener)
    }

    fun stopDownload(model: DownloadChapterStatusModel): Boolean {
        return OkDownload.with().downloadDispatcher().cancel(createFinder(model.chapter.path_url, model.chapter.audio_id.toString(),model.chapter.chapter_name))
    }

    fun stopDownload(modelList: List<DownloadChapterStatusModel>) {
        val cancelList = arrayListOf<DownloadTask>()
        modelList.forEach {
            taskList[it.chapter.path_url]?.run { cancelList.add(this) }
        }
        OkDownload.with().downloadDispatcher().cancel(cancelList.toTypedArray())
    }

    fun delete(model: DownloadChapterStatusModel) {
        createFinder(model.chapter.path_url, model.chapter.audio_id.toString(),model.chapter.chapter_name).run {
            OkDownload.with().downloadDispatcher().cancel(this)
            OkDownload.with().breakpointStore().remove(id)
            this.file?.delete()
        }.also {
            taskList.remove(model.chapter.path_url)
        }
    }

    fun delete(list: List<DownloadChapterStatusModel>) {
        list.forEach {
            delete(it)
        }
    }

    fun getTaskStatus(model: DownloadChapterStatusModel): DownloadUIStatus {
        return when (StatusUtil.getStatus(createFinder(model.chapter.path_url, model.chapter.audio_id.toString(),model.chapter.chapter_name))) {
            StatusUtil.Status.COMPLETED -> DownloadUIStatus.DOWNLOAD_COMPLETED
            StatusUtil.Status.PENDING -> DownloadUIStatus.DOWNLOAD_PENDING
            StatusUtil.Status.RUNNING -> DownloadUIStatus.DOWNLOAD_IN_PROGRESS
            StatusUtil.Status.IDLE -> DownloadUIStatus.DOWNLOAD_PAUSED
            else -> DownloadUIStatus.DOWNLOAD_UNKNOWN
        }
    }

    fun getTaskBreakpointInfo(model: DownloadChapterStatusModel): DownloadProgressUpdateBean? {
        var breakpointInfo = createFinder(model.chapter.path_url, model.chapter.audio_id.toString(),model.chapter.chapter_name).info
        return if (breakpointInfo == null) {
            null
        } else {
            DownloadProgressUpdateBean(model.chapter.path_url, breakpointInfo.totalOffset, "0/s")
        }
    }

    fun stopAll() {
        OkDownload.with().downloadDispatcher().cancelAll()
    }

    fun deleteAll() {
        OkDownload.with().downloadDispatcher().cancelAll()
        taskList.forEach {
            OkDownload.with().breakpointStore().remove(it.value.id)
        }.also {
            taskList.clear()
        }
    }

    override fun taskStart(task: DownloadTask) {
        DLog.d(TAG," taskStart name = ${task.filename}")
        DownloadMemoryCache.updateDownloadingChapter(task.url)
    }

    override fun blockEnd(task: DownloadTask, blockIndex: Int, info: BlockInfo?, blockSpeed: SpeedCalculator) {
        DLog.d(TAG," blockEnd name = ${task.filename} --- blockIndex = $blockIndex --- blockSpeed = ${blockSpeed.speed()}" )
    }

    /**
     * 这里已经是UI线程，所以不用postValue，postValue在并发情况下会把之前的值覆盖，导致数据丢失
     */
    override fun taskEnd(task: DownloadTask, cause: EndCause, realCause: Exception?, taskSpeed: SpeedCalculator) {
        DLog.d(TAG," taskEnd name = ${task.filename} --- cause = $cause --- taskSpeed = ${taskSpeed.speed()}" )
        when (cause) {
            EndCause.COMPLETED -> DownloadMemoryCache.setDownloadFinishChapter()
            else -> downloadStatus.value=(DownloadStatusChangedBean(task.url, DownloadUIStatus.DOWNLOAD_PAUSED))
        }
    }

    override fun progress(task: DownloadTask, currentOffset: Long, taskSpeed: SpeedCalculator) {
        DLog.d(TAG," progress name = ${task.filename} --- currentOffset = $currentOffset --- taskSpeed = ${taskSpeed.speed()}" )
        downloadProgress.value=(DownloadProgressUpdateBean(task.url, currentOffset, taskSpeed.speed()))
        DownloadMemoryCache.addChapterToDownloadMemoryCache(url = task.url,speed = currentOffset)
    }

    override fun connectEnd(task: DownloadTask, blockIndex: Int, responseCode: Int, responseHeaderFields: MutableMap<String, MutableList<String>>) {
        DLog.d(TAG," connectEnd name = ${task.filename}")
        downloadStatus.value=(DownloadStatusChangedBean(task.url, DownloadUIStatus.DOWNLOAD_IN_PROGRESS))
    }

    override fun connectStart(task: DownloadTask, blockIndex: Int, requestHeaderFields: MutableMap<String, MutableList<String>>) {
        DLog.d(TAG," connectStart name = ${task.filename}")
    }

    override fun infoReady(task: DownloadTask, info: BreakpointInfo, fromBreakpoint: Boolean, model: Listener4SpeedAssistExtend.Listener4SpeedModel) {
        DLog.d(TAG," infoReady name = ${task.filename}")

    }

    override fun progressBlock(task: DownloadTask, blockIndex: Int, currentBlockOffset: Long, blockSpeed: SpeedCalculator) {
        DLog.d(TAG," progressBlock name = ${task.filename}")
    }

}