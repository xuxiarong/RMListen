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
import com.rm.business_lib.db.download.DownloadDaoUtils
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
        return DownloadDaoUtils.queryAudioById(audio.audio_id)
    }

    private fun updateDownloadingAudio(chapter: DownloadChapter): Boolean {
        if (null != downloadingAudioList.value) {
            val audioList = downloadingAudioList.value!!
            audioList.forEach {
                if (it.audio_id == chapter.audio_id) {
                    it.download_num += 1
                    it.down_size += chapter.size
                    it.updateMillis = System.currentTimeMillis()
                    DaoUtil(DownloadAudio::class.java, "").saveOrUpdate(it)
                    downloadingAudioList.postValue(audioList)
                    DLog.d(
                        "suolong updateDownloadingAudio",
                        "name = ${chapter.chapter_name}" + it.down_size + "audioDownNum = ${it.download_num}"
                    )
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
            if (nextChapter.isNotDown) {
                nextChapter.down_status = DownloadConstant.CHAPTER_STATUS_DOWNLOAD_WAIT
                downloadingChapterList.value?.forEach {
                    if (it.path_url == nextChapter.path_url) {
                        iterator.remove()
                    }
                }
            } else {
                iterator.remove()
            }
        }
        if (chapterList.size == 0) {
            ToastUtil.show(
                BaseApplication.CONTEXT,
                BaseApplication.CONTEXT.getString(R.string.business_download_all_exist)
            )
        } else {
            ToastUtil.show(
                BaseApplication.CONTEXT,
                BaseApplication.CONTEXT.getString(R.string.business_download_add_cache)
            )
            downloadingChapterList.addAll(chapterList)
            DaoUtil(DownloadChapter::class.java, "").saveOrUpdate(chapterList)
            downloadingChapter.get().let {
                if (it != null) {
                    if (!isDownAll.get()) {
                        isDownAll.set(true)
                        AriaDownloadManager.startDownload(chapterList[0])
                    } else {
                        if (!it.isDownloading) {
                            isDownAll.set(true)
                            AriaDownloadManager.startDownload(it)
                        }
                    }
                } else {
                    isDownAll.set(true)
                    AriaDownloadManager.startDownload(chapterList[0])
                }
            }
        }
    }


    fun addDownloadingChapter(chapter: DownloadChapter) {

        val downChapter = downloadingChapter.get()
        if (downloadingChapterList.value != null) {
            val downloadList = downloadingChapterList.value!!
            downloadList.forEach {
                if (it.chapter_id == chapter.chapter_id) {
                    if(downChapter!=null && downChapter.isDownloading){
                        chapter.down_status = DownloadConstant.CHAPTER_STATUS_DOWNLOAD_WAIT
                        setCurrentChapter(status = DownloadConstant.CHAPTER_STATUS_NOT_DOWNLOAD)
                    }else{
                        AriaDownloadManager.startDownload(chapter)
                    }
                    return
                }
            }
        }
        ToastUtil.show(BaseApplication.CONTEXT, BaseApplication.CONTEXT.getString(R.string.business_download_add_cache))
        downloadingChapterList.add(chapter)
        if(downChapter!=null && downChapter.isDownloading){
            chapter.down_status = DownloadConstant.CHAPTER_STATUS_DOWNLOAD_WAIT
            setCurrentChapter(status = DownloadConstant.CHAPTER_STATUS_DOWNLOADING)
        }else{
            AriaDownloadManager.startDownload(chapter)
        }
    }

    fun downloadingChapterClick(clickChapter: DownloadChapter) {
        //如果是下载全部，则停止当前

        val downloadingChapter = downloadingChapter.get()


        downloadingChapterList.value?.let {
            //改变下载队列中章节的状态为暂停
            it.forEach { listChild ->
                if(listChild.chapter_id == clickChapter.chapter_id){
                    if(listChild.isDownloading ){
                        pauseDownloadingChapter()
                        clickChapter.down_status = DownloadConstant.CHAPTER_STATUS_DOWNLOAD_PAUSE
                        listChild.down_status = DownloadConstant.CHAPTER_STATUS_DOWNLOAD_PAUSE
                    }else if(listChild.isDownWait){
                        clickChapter.down_status = DownloadConstant.CHAPTER_STATUS_DOWNLOAD_PAUSE
                        listChild.down_status = DownloadConstant.CHAPTER_STATUS_DOWNLOAD_PAUSE
                    }else{
                        pauseDownloadingChapter()
                        clickChapter.down_status = DownloadConstant.CHAPTER_STATUS_DOWNLOAD_WAIT
                        listChild.down_status = DownloadConstant.CHAPTER_STATUS_DOWNLOAD_WAIT
                    }
                    return@forEach
                }
            }
            //查找是否还有等待下载的章节
            var hasWaitChapter = false
            it.forEach { downloadChapter ->
                if(downloadChapter.down_status == DownloadConstant.CHAPTER_STATUS_DOWNLOAD_WAIT){
                    hasWaitChapter = true
                    return@forEach
                }
            }
            //有等待的章节，则下载
            if(hasWaitChapter){
                isDownAll.set(true)
                AriaDownloadManager.startDownload(clickChapter)
            }else{
                isDownAll.set(false)
                pauseDownloadingChapter()
            }
        }
//
//
//
//        downloadingChapter.get()?.let { downloadChapter ->
//            if (isDownAll.get()) {
//                clickChapter.down_status = DownloadConstant.CHAPTER_STATUS_DOWNLOAD_PAUSE
//                setCurrentChapter(status = DownloadConstant.CHAPTER_STATUS_DOWNLOAD_PAUSE)
//                if (clickChapter.chapter_id == downloadChapter.chapter_id) {
//                    if(downloadChapter.isDownloading){
//                        downloadingChapterList.value?.let { downList ->
//                            var hasWaitChapter = false
//                            if(downList.isNotEmpty()){
//                                downList.forEach {
//                                    if(it.chapter_id == clickChapter.chapter_id){
//                                        it.down_status = DownloadConstant.CHAPTER_STATUS_DOWNLOAD_PAUSE
//                                    }else{
//                                        if(it.down_status == DownloadConstant.CHAPTER_STATUS_DOWNLOAD_WAIT){
//                                            hasWaitChapter = true
//                                        }
//                                    }
//                                }
//                                if(!hasWaitChapter){
//                                    AriaDownloadManager.pauseDownloadChapter(downloadChapter)
//                                    isDownAll.set(false)
//                                }else{
//                                    downloadNextWaitChapter(clickChapter)
//
//                                }
//
//                            }
////                            if (downList.size <= 1) {
////                                AriaDownloadManager.pauseDownloadChapter(downloadChapter)
////                                isDownAll.set(false)
////                            } else {
////                                downloadNextWaitChapter(clickChapter)
////                            }
//                        }
//                    } else{
//                        clickChapter.down_status = DownloadConstant.CHAPTER_STATUS_DOWNLOADING
//                        setCurrentChapter(status = DownloadConstant.CHAPTER_STATUS_DOWNLOADING)
//                        AriaDownloadManager.startDownload(clickChapter)
//                    }
//                } else {
//                    setCurrentChapter(status = DownloadConstant.CHAPTER_STATUS_DOWNLOAD_PAUSE)
//                    AriaDownloadManager.startDownload(clickChapter)
//                }
//            } else {
//                clickChapter.down_status = DownloadConstant.CHAPTER_STATUS_DOWNLOADING
//                setCurrentChapter(status = DownloadConstant.CHAPTER_STATUS_DOWNLOAD_PAUSE)
//                AriaDownloadManager.startDownload(chapter = clickChapter)
//            }
//        }
    }

    fun setCurrentChapter(
        status: Int = DownloadConstant.CHAPTER_STATUS_DOWNLOAD_WAIT,
        filePath: String = "",
        currentOffset: Long = -1L,
        speed: String = ""
    ) {
        if(status == DownloadConstant.CHAPTER_STATUS_DOWNLOADING){
            isDownAll.set(true)
        }
        downloadingChapter.get()?.let {
            it.down_status = status
            it.file_path = filePath
            it.down_speed = speed
            if (currentOffset > 0) {
                it.current_offset = currentOffset
            }
            downloadingChapter.set(it)
            downloadingChapter.notifyChange()
            DaoUtil(DownloadChapter::class.java, "").saveOrUpdate(it)
        }
    }


    /**
     * 开始下载下一个等待中的任务
     */
    private fun downloadNextWaitChapter(currentChapter: DownloadChapter) {
        downloadingChapterList.value?.let {
            val downList = it
            if (downloadingChapter.get() != null) {
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
//                    if(isDownAll.get()){
//                        if (downList.size >= 2) {
//                            AriaDownloadManager.startDownload(downList[1])
//                            return
//                        }
//                    }
                }
                //如果下载任务是队列的最后一个任务
                else if (firstDownIndex == downList.size - 1) {
                    if(isDownAll.get()){
                        for (j in 0 until downList.size - 1) {
                            if (downList[j].down_status != DownloadConstant.CHAPTER_STATUS_DOWNLOAD_PAUSE) {
                                AriaDownloadManager.startDownload(downList[j])
                                return
                            }
                        }
//                        if (downList.size >= 2) {
//                            AriaDownloadManager.startDownload(downList[0])
//                            return
//                        }
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
                    if(isDownAll.get()){
                        for (k in 0 until firstDownIndex - 1) {
                            if (downList[k].down_status != DownloadConstant.CHAPTER_STATUS_DOWNLOAD_PAUSE) {
                                AriaDownloadManager.startDownload(downList[k])
                                return
                            }
                        }
//                        AriaDownloadManager.startDownload(downList[firstDownIndex + 1])
                    }
                }
            }
        }
    }

    fun updateDownloadingSpeed(url: String, speed: String, currentOffset: Long) {
        setCurrentChapter(
            status = DownloadConstant.CHAPTER_STATUS_DOWNLOADING,
            currentOffset = currentOffset,
            speed = speed
        )
    }

    fun pauseDownloadingChapter() {
        downloadingChapter.get()?.let {
            setCurrentChapter(status = DownloadConstant.CHAPTER_STATUS_DOWNLOAD_PAUSE)
            AriaDownloadManager.pauseDownloadChapter(it)
        }
    }

    fun resumeDownloadingChapter() {
        downloadingChapter.get()?.let {
            setCurrentChapter(status = DownloadConstant.CHAPTER_STATUS_DOWNLOADING)
            AriaDownloadManager.startDownload(it)
        }
    }

    fun operatingAll() {
        isDownAll.set(isDownAll.get().not())
        isDownAll.get().let { downAll ->
            if (downAll) {
                DOWNLOAD_LAST_DOWN_STATUS.putMMKV(true)
                setCurrentChapter(status = DownloadConstant.CHAPTER_STATUS_DOWNLOADING)
                downloadingChapterList.value?.let { downloadingList ->
                    if (downloadingList.isNotEmpty()) {
                        downloadingList.forEach {
                            it.down_status = DownloadConstant.CHAPTER_STATUS_DOWNLOAD_WAIT
                        }
                        downloadingChapterList.value = downloadingList
                        AriaDownloadManager.startDownload(downloadingList[0])
                    }
                }
            } else {
                setCurrentChapter(status = DownloadConstant.CHAPTER_STATUS_DOWNLOAD_PAUSE)
                AriaDownloadManager.stopAll()
                DOWNLOAD_LAST_DOWN_STATUS.putMMKV(false)
                downloadingChapterList.value?.let { downloadingList ->
                    if (downloadingList.isNotEmpty()) {
                        downloadingList.forEach {
                            it.down_status = DownloadConstant.CHAPTER_STATUS_DOWNLOAD_PAUSE
                        }
                        downloadingChapterList.value = downloadingList
                    }
                }
            }
        }
    }


    fun setDownloadFinishChapter(filePath: String) {
        val finishChapter = downloadingChapter.get()
        if (finishChapter != null) {
            downloadFinishChapterList.add(finishChapter)
            setCurrentChapter(
                status = DownloadConstant.CHAPTER_STATUS_DOWNLOAD_FINISH,
                currentOffset = finishChapter.size,
                filePath = filePath
            )
            updateDownloadingAudio(chapter = finishChapter)
            downloadingChapterList.value?.let {
                if (it.size > 0) {
                    if (it.size >= 2) {
                        if (isDownAll.get()) {
                            downloadNextWaitChapter(finishChapter)
                        }
                    }
                    downloadingChapterList.remove(finishChapter)
                    downloadingChapterList.value?.let {
                        if(it.isEmpty()){
                            val downloadChapter = DownloadChapter()
                            downloadChapter.chapter_id = 0L
                            downloadChapter.audio_id = 0L
                            downloadChapter.path_url = ""
                            downloadingChapter.set(downloadChapter)
                        }
                    }
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
//                if (audioList.size >= 2) {
//                    audioList[1].listen_finish = true
//                }
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