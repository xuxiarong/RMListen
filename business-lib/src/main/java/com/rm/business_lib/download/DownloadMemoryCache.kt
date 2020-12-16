package com.rm.business_lib.download

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.rm.baselisten.BaseApplication
import com.rm.baselisten.ktx.*
import com.rm.baselisten.util.DLog
import com.rm.baselisten.util.ToastUtil
import com.rm.baselisten.util.getBooleanMMKV
import com.rm.baselisten.util.putMMKV
import com.rm.business_lib.R
import com.rm.business_lib.aria.AriaDownloadManager
import com.rm.business_lib.db.DaoUtil
import com.rm.business_lib.db.download.DownloadAudio
import com.rm.business_lib.db.download.DownloadChapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * desc   :
 * date   : 2020/10/27
 * version: 1.0
 */
object DownloadMemoryCache {

    private const val DOWNLOAD_LAST_DOWN_STATUS = "download_last_down_status"

    //正在下载的书籍列表
    var downloadingAudioList = MutableLiveData<MutableList<DownloadAudio>>(mutableListOf())

    //正在下载的章节列表
    var downloadingChapterList = MutableLiveData<MutableList<DownloadChapter>>(mutableListOf())

    //下载完成的章节列表
    var downloadFinishChapterList = MutableLiveData<MutableList<DownloadChapter>>(mutableListOf())

    //正在下载的章节
    var downloadingChapter = ObservableField<DownloadChapter>()

    //当前是否是下载全部
    var isDownAll = ObservableBoolean(false)

    private fun initDownloadingList() {
        downloadingChapterList.value?.forEach {
            it.chapter_edit_select = false
            it.down_edit_select = false
        }
    }

    fun initData() {
        initDownloadingList()
    }


    fun addAudioToDownloadMemoryCache(audio: DownloadAudio) {
        val cacheAudio = getAudioFromCache(audio)
        if (cacheAudio == null) {
            audio.download_num = 0
            audio.down_size = 0L
            downloadingAudioList.add(element = audio)
            DaoUtil(DownloadAudio::class.java, "").saveOrUpdate(audio)
        }
    }

    private fun getAudioFromCache(audio: DownloadAudio): DownloadAudio? {
        if (null != downloadingAudioList.value) {
            val audioList = downloadingAudioList.value!!
            audioList.forEach {
                if (it.audio_id == audio.audio_id) {
                    return it
                }
            }
        }
        return null
    }

    private fun updateDownloadingAudio(chapter: DownloadChapter): Boolean {
        if (null != downloadingAudioList.value) {
            val audioList = downloadingAudioList.value!!
            audioList.forEach {
                if (it.audio_id == chapter.audio_id) {
                    it.download_num += 1
                    it.down_size += chapter.size
                    DaoUtil(DownloadAudio::class.java, "").saveOrUpdate(it)
                    downloadingAudioList.postValue(audioList)
                    DLog.d("suolong", "name = ${chapter.chapter_name}" + it.down_size + "audioDownNum = ${it.download_num}")
                    return true
                }
            }
        }
        return false
    }

    fun deleteAudioToDownloadMemoryCache(audioList: List<DownloadAudio>) {
        audioList.forEach { deleteAudioToDownloadMemoryCache(it) }
    }

    fun deleteAudioToDownloadMemoryCache(audio: DownloadAudio) {
        val cacheAudio = getAudioFromCache(audio)
        if (cacheAudio != null) {
            downloadingAudioList.remove(cacheAudio)
            DaoUtil(DownloadAudio::class.java, "").delete(audio)
            audio.chapterList.forEach { DaoUtil(DownloadChapter::class.java, "").delete(it) }
        }
    }

    fun addDownloadingChapter(chapterList: MutableList<DownloadChapter>) {
        if (chapterList.isEmpty()) {
            return
        }
        val iterator = chapterList.iterator()
        while (iterator.hasNext()) {
            val nextChapter = iterator.next()
            nextChapter.down_status = DownloadConstant.CHAPTER_STATUS_DOWNLOAD_WAIT
            downloadingChapterList.value?.forEach {
                if (it.path_url == nextChapter.path_url) {
                    iterator.remove()
                }
            }
        }
        if (chapterList.size == 0) {
            ToastUtil.show(BaseApplication.CONTEXT, BaseApplication.CONTEXT.getString(R.string.business_download_all_exist))
        } else {
            isDownAll.set(true)
            ToastUtil.show(BaseApplication.CONTEXT, BaseApplication.CONTEXT.getString(R.string.business_download_add_cache))
            downloadingChapterList.addAll(chapterList)
            DaoUtil(DownloadChapter::class.java, "").saveOrUpdate(chapterList)
            AriaDownloadManager.startDownload(chapterList[0])
        }
    }


    fun addDownloadingChapter(chapter: DownloadChapter) {
        isDownAll.set(false)
        if (downloadingChapterList.value != null) {
            val downloadList = downloadingChapterList.value!!
            downloadList.forEach {
                if (it.path_url == chapter.path_url) {
                    ToastUtil.show(BaseApplication.CONTEXT, BaseApplication.CONTEXT.getString(R.string.business_download_all_exist))
                    return
                }
            }
        }
        ToastUtil.show(BaseApplication.CONTEXT, BaseApplication.CONTEXT.getString(R.string.business_download_add_cache))
        downloadingChapterList.add(chapter)
        AriaDownloadManager.startDownload(chapter)
    }

    /**
     * 下载完成，自动下载下一章节
     */
    fun downFinishAndAutoDownNext() {
        if (downloadingChapterList.value == null) {
            DLog.d("suolong下载 自动下载下一章", "下载队列为空")
            return
        }

        if (downloadingChapterList.value != null) {
            val downList = downloadingChapterList.value!!
            when (downList.size) {
                0 -> {
                    DLog.d("suolong下载 自动下载下一章", "下载队列大小为0")
                    return
                }
                1 -> {
                    if (downloadingChapter.get() != null) {
                        if (downList[0].chapter_id == downloadingChapter.get()!!.chapter_id) {
                            val tempChapter = DownloadChapter()
                            tempChapter.audio_id = 0L
                            tempChapter.chapter_id = 0L
                            tempChapter.path_url = ""
                            downloadingChapter.set(tempChapter)
                            downloadingChapter.notifyChange()
                            downloadingChapterList.clear()
                        } else {
                            DLog.d("suolong下载 自动下载下一章", "downList[0] != downloadingChapter")
                        }
                    } else {
                        DLog.d("suolong下载 自动下载下一章", "downloadingChapter == null")
                    }
                }
                else -> {
                    if (downloadingChapter.get() != null) {
                        val downloadChapter = downloadingChapter.get()!!
                        for (i in 0 until downList.size) {
                            if (downList[i].chapter_id == downloadChapter.chapter_id) {
                                if (i == downList.size - 1) {
                                    DLog.d("suolong下载 自动下载下一章", "开始下载downList[0] + name = ${downList[0].chapter_name}")
                                    AriaDownloadManager.startDownload(downList[0])
                                } else {
                                    DLog.d("suolong下载 自动下载下一章", "开始下载downList[i+1] + name = ${downList[i + 1].chapter_name}")
                                    AriaDownloadManager.startDownload(downList[i + 1])
                                }
                                return
                            }
                        }
                    }
                }
            }
        }
    }


    fun downloadingChapterClick(clickChapter: DownloadChapter) {
        val downloadChapter = downloadingChapter.get()
        //如果是下载全部，则停止当前
        if (isDownAll.get()) {
            if (downloadChapter != null && clickChapter.chapter_id == downloadChapter.chapter_id) {
                clickChapter.down_status = DownloadConstant.CHAPTER_STATUS_DOWNLOAD_PAUSE
                pauseCurrentAndDownNextChapter(downloadChapter)
            } else {
                AriaDownloadManager.stopAll()
                AriaDownloadManager.startDownload(clickChapter)
            }
        } else {
            clickChapter.down_status = DownloadConstant.CHAPTER_STATUS_DOWNLOADING
            AriaDownloadManager.startDownload(clickChapter)
        }
    }

    /**
     * 停止当前章节，开始下载下一个任务
     */
    fun pauseCurrentAndDownNextChapter(currentChapter: DownloadChapter) {
        downloadingChapterList.value?.let {
            val downList = it
            when (downList.size) {
                0 -> {
                    return
                }
                1 -> {
                    pauseDownloadingChapter()
                    isDownAll.set(false)
                }
                else -> {
                    if (downloadingChapter.get() != null) {
                        currentChapter.down_status = DownloadConstant.CHAPTER_STATUS_DOWNLOAD_PAUSE
                        downloadingChapter.set(currentChapter)
                        downloadingChapter.notifyChange()
                        var firstDownIndex = -1

                        //找到当前的下载任务的index
                        for (i in 0 until downList.size) {
                            if (downList[i].chapter_id == currentChapter.chapter_id) {
                                firstDownIndex = i
                                break
                            }
                        }
                        //如果下载任务是队列的第一个任务
                        if (firstDownIndex == 0) {
                            for (j in 1 until downList.size) {
                                if (downList[j].down_status != DownloadConstant.CHAPTER_STATUS_DOWNLOAD_PAUSE) {
                                    AriaDownloadManager.startDownload(downList[j])
                                    return
                                }
                            }
                            if (downList.size >= 2) {
                                AriaDownloadManager.startDownload(downList[1])
                                return
                            }
                        }
                        //如果下载任务是队列的最后一个任务
                        else if (firstDownIndex == downList.size - 1) {
                            for (j in 0 until downList.size -1) {
                                if (downList[j].down_status != DownloadConstant.CHAPTER_STATUS_DOWNLOAD_PAUSE) {
                                    AriaDownloadManager.startDownload(downList[j])
                                    return
                                }
                            }
                            if (downList.size >= 2) {
                                AriaDownloadManager.startDownload(downList[0])
                                return
                            }
                        }
                        //如果下载任务处于第二个到倒数第二个
                        else if (firstDownIndex > 0 && firstDownIndex < (downList.size - 1)) {

                            //寻找当前任务后面的队列是否有不为暂停的任务，有就下载第一个不为暂停的任务
                            for (j in firstDownIndex + 1 until downList.size) {
                                if (downList[j].down_status != DownloadConstant.CHAPTER_STATUS_DOWNLOAD_PAUSE) {
                                    AriaDownloadManager.startDownload(downList[j])
                                    return
                                }
                            }
                            for (k in 0 until firstDownIndex-1) {
                                if (downList[k].down_status != DownloadConstant.CHAPTER_STATUS_DOWNLOAD_PAUSE) {
                                    AriaDownloadManager.startDownload(downList[k])
                                    return
                                }
                            }
                            AriaDownloadManager.startDownload(downList[firstDownIndex+1])
                        }
                    }
                }
            }
        }
    }

    fun updateDownloadingSpeed(url: String, speed: String, currentOffset: Long) {
        if (downloadingChapter.get() != null) {
            val chapter = downloadingChapter.get()!!
            chapter.down_status = DownloadConstant.CHAPTER_STATUS_DOWNLOADING
            chapter.current_offset = currentOffset
            chapter.down_speed = speed
            downloadingChapter.set(chapter)
            downloadingChapter.notifyChange()
            DaoUtil(DownloadChapter::class.java, "").saveOrUpdate(chapter)
        }
    }

    fun pauseDownloadingChapter() {
        if (downloadingChapter.get() != null) {
            val chapter = downloadingChapter.get()!!
            chapter.down_status = DownloadConstant.CHAPTER_STATUS_DOWNLOAD_PAUSE
            downloadingChapter.set(chapter)
            downloadingChapter.notifyChange()
            AriaDownloadManager.pauseDownloadChapter(chapter)
        }
    }

    fun resumeDownloadingChapter() {
        if (downloadingChapter.get() != null) {
            val chapter = downloadingChapter.get()!!
            if (chapter.down_status == DownloadConstant.CHAPTER_STATUS_DOWNLOAD_PAUSE) {
                chapter.down_status = DownloadConstant.CHAPTER_STATUS_DOWNLOADING
            }
            downloadingChapter.set(chapter)
            downloadingChapter.notifyChange()
            AriaDownloadManager.startDownload(chapter)
        }
    }

    fun operatingAll() {
        isDownAll.set(isDownAll.get().not())
        isDownAll.get().let { downAll ->
            if (downAll) {
                DOWNLOAD_LAST_DOWN_STATUS.putMMKV(true)
                downloadingChapter.get()?.let { downloadChapter ->
                    downloadChapter.down_status = DownloadConstant.CHAPTER_STATUS_DOWNLOADING
                    downloadingChapter.set(downloadChapter)
                    downloadingChapter.notifyChange()
                    AriaDownloadManager.startDownload(downloadChapter)
                }
            } else {
                downloadingChapter.get()?.let {
                    it.down_status = DownloadConstant.CHAPTER_STATUS_DOWNLOAD_PAUSE
                    downloadingChapter.set(it)
                    downloadingChapter.notifyChange()
                }
                AriaDownloadManager.stopAll()
                DOWNLOAD_LAST_DOWN_STATUS.putMMKV(false)
            }
        }
    }


    fun setDownloadFinishChapter(filePath: String) {
        val finishChapter = downloadingChapter.get()
        if (finishChapter != null) {
            downloadFinishChapterList.add(finishChapter)
            finishChapter.down_status = DownloadConstant.CHAPTER_STATUS_DOWNLOAD_FINISH
            finishChapter.current_offset = finishChapter.size
            finishChapter.file_path = filePath
            downloadingChapter.set(finishChapter)
            downloadingChapter.notifyChange()
            DaoUtil(DownloadChapter::class.java, "").saveOrUpdate(finishChapter)
            updateDownloadingAudio(
                    chapter = finishChapter
            )
            if (isDownAll.get()) {
                DLog.d("suolong下载", "下载_开始寻找下一个下载任务")
                downFinishAndAutoDownNext()
            }

            if (downloadingChapterList.value != null) {
                val value = downloadingChapterList.value!!
                if (value.size > 0) {
                    downloadingChapterList.remove(finishChapter)
                }
            }
        }
    }

    fun deleteDownloadingChapter(chapterList: List<DownloadChapter>) {
        downloadingChapterList.removeAll(chapterList)
        DaoUtil(DownloadChapter::class.java, "").delete(chapterList)
        AriaDownloadManager.deleteAllDownload()
    }

    fun getDownAudioOnAppCreate() {
        GlobalScope.launch(Dispatchers.IO) {
            val audioList = DaoUtil(DownloadAudio::class.java, "").queryAll()
            val downChapterList = mutableListOf<DownloadChapter>()
            if (audioList != null) {
                downloadingAudioList.postValue(audioList.toMutableList())
                if (audioList.size >= 2) {
                    audioList[1].listen_finish = true
                }
                audioList.forEach { audio ->
                    audio.edit_select = false
                    val chapterList = audio.chapterList
                    chapterList.forEach { chapter ->
                        chapter.chapter_edit_select = false
                        chapter.down_edit_select = false
                        if (!chapter.isDownloadFinish) {
                            downChapterList.add(chapter)
                        }
                    }
                }
                if (downChapterList.size > 0) {
                    downloadingChapterList.postValue(downChapterList)
                    isDownAll.set(DOWNLOAD_LAST_DOWN_STATUS.getBooleanMMKV(false))
                    if (isDownAll.get()) {
                        AriaDownloadManager.startDownload(downChapterList[0])
                    }
                }
            }
        }
    }

}