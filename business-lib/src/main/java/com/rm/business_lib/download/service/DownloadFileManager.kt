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
import com.rm.business_lib.download.DownloadMemoryCache
import com.rm.business_lib.download.file.DownLoadFileUtils.getParentFile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File

class DownloadFileManager private constructor() {

    private val taskList = HashMap<String, DownloadTask>()

    private val context = BaseApplication.CONTEXT

    companion object {
        @JvmStatic
        val INSTANCE: DownloadFileManager by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { DownloadFileManager() }
        var downloadingTask : DownloadTask? = null
        const val minIntervalMillisCallbackProcess = 1000  //回掉间隔毫秒数
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
                taskList[chapter.path_url] = it
            }.run {
                DownloadMemoryCache.downloadingChapter.set(chapter)
                GlobalScope.launch(Dispatchers.IO){
                    DLog.d(TAG,"name = ${chapter.chapter_name}  开始下载")
                    execute(object : DownloadListener4WithSpeed(){
                        override fun taskStart(task: DownloadTask) {
                        }

                        override fun blockEnd(task: DownloadTask, blockIndex: Int, info: BlockInfo?, blockSpeed: SpeedCalculator) {
                            DLog.d(TAG,"name = ${chapter.chapter_name}  blockEnd")
//                            retryDownCurrentFailedChapter()

                        }

                        override fun taskEnd(task: DownloadTask, cause: EndCause, realCause: java.lang.Exception?, taskSpeed: SpeedCalculator) {
                            when (cause) {
                                EndCause.COMPLETED -> {
                                    DLog.d(TAG, "suolong下载 taskEnd name = ${task.filename}下载完成")
                                    DownloadMemoryCache.setDownloadFinishChapter(task.file?.absolutePath!!)
                                }
                                EndCause.CANCELED -> { DLog.d(TAG, "taskEnd name = ${task.filename} 下载取消")
                                    DownloadMemoryCache.pauseDownloadingChapter()
                                }
                                EndCause.SAME_TASK_BUSY->{
                                    retryDownCurrentFailedChapter()
                                }
                                EndCause.FILE_BUSY ->{
                                    retryDownCurrentFailedChapter()
                                    DLog.d(TAG, "taskEnd name = ${task.filename} 下载失败,原因是 EndCause.FILE_BUSY ${realCause?.message}")
                                }
                                else -> {
                                    retryDownCurrentFailedChapter()
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

                        override fun connectEnd(task: DownloadTask, blockIndex: Int, responseCode: Int, responseHeaderFields: MutableMap<String, MutableList<String>>) {
                            DLog.d(TAG,"name = ${chapter.chapter_name}  connectEnd")
                        }

                        override fun connectStart(task: DownloadTask, blockIndex: Int, requestHeaderFields: MutableMap<String, MutableList<String>>) {
                            DLog.d(TAG,"name = ${chapter.chapter_name}  connectStart")
                        }

                        override fun infoReady(task: DownloadTask, info: BreakpointInfo, fromBreakpoint: Boolean, model: Listener4SpeedAssistExtend.Listener4SpeedModel) {
                            DLog.d(TAG,"name = ${chapter.chapter_name}  infoReady")
                        }

                        override fun progressBlock(task: DownloadTask, blockIndex: Int, currentBlockOffset: Long, blockSpeed: SpeedCalculator) {
                            DLog.d(TAG,"name = ${chapter.chapter_name}  progressBlock")
                        }
                    })
                }
            }
        }catch (e : Exception){
            DLog.d(TAG,"name = ${chapter.chapter_name}  下载异常: ${e.message?:"异常信息为空"}")
        }

    }

    fun startDownloadWithCache(modelList: List<DownloadChapter>) {
        if(modelList.isNotEmpty()){
            val taskList = Array(modelList.size) { index ->
                createFinder(
                        modelList[index].path_url,
                        modelList[index].audio_id.toString(),
                        modelList[index].chapter_name
                ).also {
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

    fun retryDownCurrentFailedChapter(){
        val currentChapter = DownloadMemoryCache.downloadingChapter.get()
        if(currentChapter!=null && currentChapter.chapter_id>0){
            if(NetWorkUtils.isNetworkAvailable(BaseApplication.CONTEXT)){
                DLog.d(TAG,"retryDownCurrentFailedChapter = currentChapter name = ${currentChapter.chapter_name}")
                startDownloadWithCache(currentChapter)
            }
        }
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
}