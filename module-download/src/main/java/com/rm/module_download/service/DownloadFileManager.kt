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
import com.rm.business_lib.bean.download.BaseDownloadFileBean
import com.rm.business_lib.bean.download.DownloadProgressUpdateBean
import com.rm.business_lib.bean.download.DownloadStatusChangedBean
import com.rm.business_lib.bean.download.DownloadUIStatus
import com.rm.business_lib.downloadProgress
import com.rm.business_lib.downloadStatus
import com.rm.module_download.util.TagUtil
import com.rm.module_download.util.deleteDir
import com.rm.module_download.util.getParentFile
import java.io.File

class DownloadFileManager private constructor() : DownloadListener4WithSpeed() {

    private val taskList = ArrayMap<String, DownloadTask>()
    private val context = BaseApplication.CONTEXT

    private val cacheFile: File by lazy { File(getParentFile(context), cacheFileName) }

    private val queueListener: DownloadListener by lazy { this@DownloadFileManager }



    companion object {
        @JvmStatic
        val INSTANCE: DownloadFileManager by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { DownloadFileManager() }
        const val minIntervalMillisCallbackProcess = 200  //回掉间隔毫秒数
        const val maxParallelRunningCount = 1  //最大并行数
        const val cacheFileName = "download_cache"  //缓存文件名称
        const val TAG = "DownloadFileManager"
    }

    init {
        //并行下载，并发下载数设置为1，来实现串行下载
        DownloadDispatcher.setMaxParallelRunningCount(maxParallelRunningCount)
    }

    private inline fun createTask(url: String, audioName: String,fileName: String): DownloadTask {
        return DownloadTask.Builder(url, createFileWithAudio(audioName).absolutePath, fileName)
            .setMinIntervalMillisCallbackProcess(minIntervalMillisCallbackProcess)
            .setAutoCallbackToUIThread(true)//是否在UI线程回掉
            .build()
    }

    private fun createFileWithAudio(audioName :String ) : File{
        return File(getParentFile(context), audioName)
    }

    private inline fun createFinder(url: String,audioName: String,fileName: String): DownloadTask {
        return if (taskList.contains(url)) {
            taskList[url]!!
        } else {
            DownloadTask.Builder(url, createFileWithAudio(audioName).absolutePath, fileName).build()
        }
    }


    fun download(baseBean: BaseDownloadFileBean) {
        createTask(baseBean.url, baseBean.parentFileDir,baseBean.fileName).also {
            TagUtil.saveHolder(it, baseBean)
            taskList[baseBean.url] = it
            downloadStatus.value=(DownloadStatusChangedBean(baseBean.url, DownloadUIStatus.DOWNLOAD_PENDING))
        }.run {
            enqueue(queueListener)
        }
    }

    fun download(audioList: List<BaseDownloadFileBean>) {
        var taskList = Array(audioList.size) { index ->
            createTask(audioList[index].url, audioList[index].parentFileDir,audioList[index].fileName).also {
                TagUtil.saveHolder(it, audioList[index])
                taskList[audioList[index].url] = it
                downloadStatus.value=(DownloadStatusChangedBean(audioList[index].url, DownloadUIStatus.DOWNLOAD_PENDING))
            }
        }
        DownloadTask.enqueue(taskList, queueListener)
    }

    fun stopDownload(baseBean: BaseDownloadFileBean): Boolean {
        return OkDownload.with().downloadDispatcher().cancel(createFinder(baseBean.url, baseBean.parentFileDir,baseBean.fileName))
    }

    fun stopDownload(baseBean: List<BaseDownloadFileBean>) {
        var cancelList = arrayListOf<DownloadTask>()
        baseBean.forEach {
            taskList[it.url]?.run { cancelList.add(this) }
        }
        OkDownload.with().downloadDispatcher().cancel(cancelList.toTypedArray())
    }

    fun delete(baseBean: BaseDownloadFileBean) {
        createFinder(baseBean.url, baseBean.parentFileDir,baseBean.fileName).run {
            OkDownload.with().downloadDispatcher().cancel(this)
            OkDownload.with().breakpointStore().remove(id)
            file?.delete()
        }.also {
            taskList.remove(baseBean.url)
        }
    }

    fun delete(list: List<BaseDownloadFileBean>) {
        list.forEach {
            delete(it)
        }
    }

    fun getTaskStatus(baseBean: BaseDownloadFileBean): DownloadUIStatus {
        return when (StatusUtil.getStatus(createFinder(baseBean.url, baseBean.parentFileDir,baseBean.fileName))) {
            StatusUtil.Status.COMPLETED -> DownloadUIStatus.DOWNLOAD_COMPLETED
            StatusUtil.Status.PENDING -> DownloadUIStatus.DOWNLOAD_PENDING
            StatusUtil.Status.RUNNING -> DownloadUIStatus.DOWNLOAD_IN_PROGRESS
            StatusUtil.Status.IDLE -> DownloadUIStatus.DOWNLOAD_PAUSED
            else -> DownloadUIStatus.DOWNLOAD_UNKNOWN
        }
    }

    fun getTaskBreakpointInfo(baseBean: BaseDownloadFileBean): DownloadProgressUpdateBean? {
        var breakpointInfo = createFinder(baseBean.url, baseBean.parentFileDir,baseBean.fileName).info
        return if (breakpointInfo == null) {
            null
        } else {
            DownloadProgressUpdateBean(baseBean.url, breakpointInfo.totalOffset, "0/s")
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
            deleteDir(cacheFile.absolutePath)
        }
    }

    override fun taskStart(task: DownloadTask) {
        DLog.d(TAG," taskStart name = ${task.filename}")
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
            EndCause.COMPLETED -> downloadStatus.value=(DownloadStatusChangedBean(task.url, DownloadUIStatus.DOWNLOAD_COMPLETED))
            else -> downloadStatus.value=(DownloadStatusChangedBean(task.url, DownloadUIStatus.DOWNLOAD_PAUSED))
        }
    }

    override fun progress(task: DownloadTask, currentOffset: Long, taskSpeed: SpeedCalculator) {
        DLog.d(TAG," progress name = ${task.filename} --- currentOffset = $currentOffset --- taskSpeed = ${taskSpeed.speed()}" )
        downloadProgress.value=(DownloadProgressUpdateBean(task.url, currentOffset, taskSpeed.speed()))
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