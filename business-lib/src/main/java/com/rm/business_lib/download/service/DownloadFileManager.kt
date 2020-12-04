package com.rm.business_lib.download.service

import com.liulishuo.okdownload.DownloadTask
import com.liulishuo.okdownload.OkDownload
import com.liulishuo.okdownload.SpeedCalculator
import com.liulishuo.okdownload.StatusUtil
import com.liulishuo.okdownload.core.breakpoint.BlockInfo
import com.liulishuo.okdownload.core.breakpoint.BreakpointInfo
import com.liulishuo.okdownload.core.cause.EndCause
import com.liulishuo.okdownload.core.dispatcher.DownloadDispatcher
import com.liulishuo.okdownload.core.listener.DownloadListener4WithSpeed
import com.liulishuo.okdownload.core.listener.assist.Listener4SpeedAssistExtend
import com.rm.baselisten.BaseApplication
import com.rm.baselisten.util.DLog
import com.rm.baselisten.util.NetWorkUtils
import com.rm.business_lib.bean.download.DownloadProgressUpdateBean
import com.rm.business_lib.bean.download.DownloadUIStatus
import com.rm.business_lib.db.download.DownloadChapter
import com.rm.business_lib.download.DownloadConstant
import com.rm.business_lib.download.DownloadMemoryCache
import com.rm.business_lib.download.file.DownLoadFileUtils.getParentFile
import com.rm.business_lib.download.util.TagUtil
import java.io.File

class DownloadFileManager private constructor() : DownloadListener4WithSpeed() {

    private val taskList = HashMap<String, DownloadTask>()

    private val context = BaseApplication.CONTEXT

    private val queueListener: DownloadListener4WithSpeed by lazy { this@DownloadFileManager }


    companion object {
        @JvmStatic
        val INSTANCE: DownloadFileManager by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { DownloadFileManager() }
        var downloadingTask : DownloadTask? = null
        const val minIntervalMillisCallbackProcess = 800  //回掉间隔毫秒数
        const val maxParallelRunningCount = 1  //最大并行数
        const val TAG = "DownloadFileManager_suolong"
    }

    init {
        //并行下载，并发下载数设置为1，来实现串行下载
        DownloadDispatcher.setMaxParallelRunningCount(maxParallelRunningCount)
    }

    private fun createTask(url: String, parentPath: String, fileName: String): DownloadTask {
        return DownloadTask.Builder(url, createFileWithAudio(parentPath).absolutePath, fileName)
            .setMinIntervalMillisCallbackProcess(minIntervalMillisCallbackProcess)
            .setPreAllocateLength(false)
            .setAutoCallbackToUIThread(true)//是否在UI线程回掉
            .build()
    }

    private fun createFileWithAudio(audioName: String): File {
        return File(getParentFile(context), audioName)
    }

    private fun createFinder(url: String, audioName: String, fileName: String): DownloadTask {
        return if (taskList.contains(url)) {
            taskList[url]!!
        } else {
            DownloadTask.Builder(url, createFileWithAudio(audioName).absolutePath, fileName).build()
        }
    }


    fun startDownloadWithCache(chapter: DownloadChapter) {
        try {
            createTask(chapter.path_url, chapter.audio_id.toString(), chapter.chapter_name).also {
                TagUtil.saveHolder(it, chapter)
                taskList[chapter.path_url] = it
            }.run {
                downloadingTask = this
                enqueue(queueListener)
            }
        }catch (e : Exception){

        }

    }

    fun startDownloadWithCache(modelList: List<DownloadChapter>) {
        if(modelList.isNotEmpty()){
            val taskList = Array(modelList.size) { index ->
                createTask(
                        modelList[index].path_url,
                        modelList[index].audio_id.toString(),
                        modelList[index].chapter_name
                ).also {
                    TagUtil.saveHolder(it, modelList[index])
                    taskList[modelList[index].path_url] = it
                }
            }
            if(getTaskStatus(modelList[0])!=DownloadUIStatus.DOWNLOAD_IN_PROGRESS && getTaskStatus(modelList[0])!=DownloadUIStatus.DOWNLOAD_COMPLETED){
                startDownloadWithCache(modelList[0])
            }
        }
    }

    fun pauseDownload(chapter: DownloadChapter): Boolean {
        return OkDownload.with().downloadDispatcher().cancel(
            createFinder(
                chapter.path_url,
                chapter.audio_id.toString(),
                chapter.chapter_name
            )
        )
    }

    fun pauseDownload(modelList: List<DownloadChapter>) {
        val cancelList = arrayListOf<DownloadTask>()
        modelList.forEach {
            taskList[it.path_url]?.run { cancelList.add(this) }
        }
        OkDownload.with().downloadDispatcher().cancel(cancelList.toTypedArray())
    }

    fun deleteDownload(chapter: DownloadChapter) {
        createFinder(chapter.path_url, chapter.audio_id.toString(), chapter.chapter_name).run {
            OkDownload.with().downloadDispatcher().cancel(this)
            OkDownload.with().breakpointStore().remove(id)
            this.file?.delete()
        }.also {
            taskList.remove(chapter.path_url)
        }
    }

    fun deleteDownload(list: List<DownloadChapter>) {
        list.forEach {
            deleteDownload(it)
        }
    }

    fun getTaskStatus(chapter: DownloadChapter): DownloadUIStatus {
        return when (StatusUtil.getStatus(
            createFinder(
                chapter.path_url,
                chapter.audio_id.toString(),
                chapter.chapter_name
            )
        )) {
            StatusUtil.Status.COMPLETED -> DownloadUIStatus.DOWNLOAD_COMPLETED
            StatusUtil.Status.PENDING -> DownloadUIStatus.DOWNLOAD_PENDING
            StatusUtil.Status.RUNNING -> DownloadUIStatus.DOWNLOAD_IN_PROGRESS
            StatusUtil.Status.IDLE -> DownloadUIStatus.DOWNLOAD_PAUSED
            else -> DownloadUIStatus.DOWNLOAD_UNKNOWN
        }
    }

    fun getTaskBreakpointInfo(chapter: DownloadChapter): DownloadProgressUpdateBean? {
        val breakpointInfo =
            createFinder(chapter.path_url, chapter.audio_id.toString(), chapter.chapter_name).info
        return if (breakpointInfo == null) {
            null
        } else {
            DownloadProgressUpdateBean(chapter.path_url, breakpointInfo.totalOffset, "0/s")
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
        DLog.d(TAG, " taskStart name = ${task.filename}")
        DownloadMemoryCache.updateDownloadingChapter(
            task.url,
            DownloadConstant.CHAPTER_STATUS_DOWNLOADING
        )
    }

    override fun blockEnd(
        task: DownloadTask,
        blockIndex: Int,
        info: BlockInfo?,
        blockSpeed: SpeedCalculator
    ) {
//        DLog.d(
//            TAG,
//            " blockEnd name = ${task.filename} --- blockIndex = $blockIndex --- blockSpeed = ${blockSpeed.speed()}"
//        )
    }

    /**
     * 这里已经是UI线程，所以不用postValue，postValue在并发情况下会把之前的值覆盖，导致数据丢失
     */
    override fun taskEnd(
        task: DownloadTask,
        cause: EndCause,
        realCause: Exception?,
        taskSpeed: SpeedCalculator
    ) {
        DLog.d(TAG, " taskEnd name = ${task.filename} --- cause = $cause --- taskSpeed = ${taskSpeed.speed()}")
        when (cause) {
            EndCause.COMPLETED -> {
                DLog.d(TAG, "taskEnd name = ${task.filename}下载完成")
                DownloadMemoryCache.setDownloadFinishChapter(task.file?.absolutePath!!)
            }
            EndCause.CANCELED -> { DLog.d(TAG, "taskEnd name = ${task.filename} 下载取消")
                DownloadMemoryCache.pauseDownloadingChapter()
            }
            EndCause.SAME_TASK_BUSY->{
                DLog.d(TAG, "taskEnd name = ${task.filename} 下载失败,原因是 EndCause.SAME_TASK_BUSY ${realCause?.message}")
            }
            EndCause.FILE_BUSY ->{
                DLog.d(TAG, "taskEnd name = ${task.filename} 下载失败,原因是 EndCause.FILE_BUSY ${realCause?.message}")
            }
            else -> {
                if(NetWorkUtils.isNetworkAvailable(BaseApplication.CONTEXT)){
                    task.enqueue(queueListener)
                }
                DLog.d(TAG, "taskEnd name = ${task.filename} 下载失败,原因是${cause.ordinal.toString()} ${realCause?.message}")
            }
        }
    }

    override fun progress(task: DownloadTask, currentOffset: Long, taskSpeed: SpeedCalculator) {
        DownloadMemoryCache.updateDownloadingSpeed(
            speed = taskSpeed.speed(),
            currentOffset = currentOffset,
            url = task.url
        )
    }

    override fun connectEnd(
        task: DownloadTask,
        blockIndex: Int,
        responseCode: Int,
        responseHeaderFields: MutableMap<String, MutableList<String>>
    ) {
        DLog.d(TAG, " connectEnd name = ${task.filename}")
    }

    override fun connectStart(
        task: DownloadTask,
        blockIndex: Int,
        requestHeaderFields: MutableMap<String, MutableList<String>>
    ) {
        DLog.d(TAG, " connectStart name = ${task.filename}")
    }

    override fun infoReady(
        task: DownloadTask,
        info: BreakpointInfo,
        fromBreakpoint: Boolean,
        model: Listener4SpeedAssistExtend.Listener4SpeedModel
    ) {
        DLog.d(TAG, " infoReady name = ${task.filename}")

    }

    override fun progressBlock(
        task: DownloadTask,
        blockIndex: Int,
        currentBlockOffset: Long,
        blockSpeed: SpeedCalculator
    ) {
//        DLog.d(TAG, " progressBlock name = ${task.filename}")
    }

}