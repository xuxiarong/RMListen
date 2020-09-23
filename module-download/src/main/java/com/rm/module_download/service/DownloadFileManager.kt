package com.rm.module_download.service

import android.app.DownloadManager
import com.liulishuo.okdownload.*
import com.liulishuo.okdownload.core.cause.EndCause
import com.rm.baselisten.BaseApplication
import com.rm.business_lib.bean.download.DownloadAudioBean
import com.rm.business_lib.bean.download.DownloadUIStatus
import com.rm.component_comm.download.DownloadService
import com.rm.module_download.DownloadApplicationDelegate
import com.rm.module_download.util.getParentFile
import org.koin.dsl.koinApplication
import java.io.File
import java.lang.Exception

class DownloadFileManager private constructor() : DownloadContextListener {
    private val taskList = arrayListOf<DownloadTask>()

    private val cacheFile: File by lazy { File(getParentFile(BaseApplication.CONTEXT), "download_cache") }

    private val queueListener: DownloadListener by lazy { QueueListener() }

    private var downloadContext: DownloadContext? = null

    companion object {
        @JvmStatic
        val INSTANCE: DownloadFileManager by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { DownloadFileManager() }
        private const val callbackTimeSchedule = 200
        private const val isSerial = true
    }

    private fun getDownloadBuilder(): DownloadContext.Builder {
        return DownloadContext.QueueSet().apply {
            setParentPathFile(cacheFile)
            minIntervalMillisCallbackProcess = callbackTimeSchedule
        }.commit().setListener(this@DownloadFileManager)
    }

    suspend fun download(url: String, fileName: String) {
        if (downloadContext == null) {
            downloadContext = getDownloadBuilder().apply {
                bind(DownloadTask.Builder(url, cacheFile.absolutePath, fileName))
            }.build()
        } else {
            downloadContext = downloadContext!!.toBuilder().apply { bind(DownloadTask.Builder(url, cacheFile.absolutePath, fileName)) }.build()
        }
    }


    fun startDownloadAudio(audioList: List<DownloadAudioBean>) {

    }

    fun stopDownload(url: String) {
    }

    fun stopDownload(urlList: List<String>) {
    }

    fun delete(url: String) {
    }

    fun delete(urlList: List<String>) {
    }

    fun getDownloadAudioInfo(url: String): DownloadAudioBean {
        return DownloadAudioBean("","","","",DownloadUIStatus.DOWNLOAD_IN_PROGRESS)
    }

    override fun taskEnd(context: DownloadContext, task: DownloadTask, cause: EndCause, realCause: Exception?, remainCount: Int) {
    }

    override fun queueEnd(context: DownloadContext) {
    }


}