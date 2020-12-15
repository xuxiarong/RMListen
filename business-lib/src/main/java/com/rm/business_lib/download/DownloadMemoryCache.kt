package com.rm.business_lib.download

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.lifecycle.MutableLiveData
import com.rm.baselisten.BaseApplication
import com.rm.baselisten.ktx.*
import com.rm.baselisten.util.DLog
import com.rm.baselisten.util.ToastUtil
import com.rm.business_lib.R
import com.rm.business_lib.aria.AriaDownloadManager
import com.rm.business_lib.db.DaoUtil
import com.rm.business_lib.db.download.DownloadAudio
import com.rm.business_lib.db.download.DownloadChapter
import com.rm.business_lib.download.service.DownloadFileManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * desc   :
 * date   : 2020/10/27
 * version: 1.0
 */
object DownloadMemoryCache {

    //正在下载的书籍列表
    var downloadingAudioList = MutableLiveData<MutableList<DownloadAudio>>(mutableListOf())
    //正在下载的章节列表
    var downloadingChapterList = MutableLiveData<MutableList<DownloadChapter>>(mutableListOf())
    //下载完成的章节列表
    var downloadFinishChapterList = MutableLiveData<MutableList<DownloadChapter>>(mutableListOf())
    //正在下载的章节
    var downloadingChapter = ObservableField<DownloadChapter>()
    var isPauseAll = ObservableBoolean(false)
    var isDownAll = ObservableBoolean(false)

    var downloadingNum = ObservableInt(0)
    val downloadService = DownloadFileManager.INSTANCE

    fun initDownloadingList(){
        downloadingChapterList.value?.forEach {
            it.chapter_edit_select = false
            it.down_edit_select = false
        }
    }

    fun initData(){
        initDownloadingList()
    }


    fun addAudioToDownloadMemoryCache(audio: DownloadAudio) {
        val cacheAudio =
            getAudioFromCache(
                audio
            )
        if (cacheAudio == null) {
            audio.download_num = 0
            audio.down_size = 0L
            downloadingAudioList.add(element = audio)
            DaoUtil(DownloadAudio::class.java, "").saveOrUpdate(audio)
        }
    }

    fun getAudioFromCache(audio: DownloadAudio): DownloadAudio? {
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

    fun updateDownloadingAudio(chapter: DownloadChapter): Boolean {
        if (null != downloadingAudioList.value) {
            val audioList = downloadingAudioList.value!!
            audioList.forEach {
                if (it.audio_id == chapter.audio_id) {
                    it.download_num += 1
                    it.down_size += chapter.size
                    DaoUtil(DownloadAudio::class.java, "").saveOrUpdate(it)
                    downloadingAudioList.postValue(audioList)
                    DLog.d("suolong","name = ${chapter.chapter_name}" + it.down_size + "audioDownNum = ${it.download_num}")
                    return true
                }
            }
        }
        return false
    }

    fun addAudioToDownloadMemoryCache(audioList: List<DownloadAudio>) {
        audioList.forEach {
            addAudioToDownloadMemoryCache(
                it
            )
        }
    }

    fun deleteAudioToDownloadMemoryCache(audioList: List<DownloadAudio>) {
        audioList.forEach {
            deleteAudioToDownloadMemoryCache(
                it
            )
        }
    }

    fun deleteAudioToDownloadMemoryCache(audio: DownloadAudio) {
        val cacheAudio =
            getAudioFromCache(
                audio
            )
        if (cacheAudio!=null) {
            downloadingAudioList.remove(cacheAudio)
            DaoUtil(DownloadAudio::class.java, "").delete(audio)
            audio.chapterList.forEach {
                DaoUtil(DownloadChapter::class.java, "").delete(it)
            }
        }
    }

    fun addDownloadingChapter(chapterList: MutableList<DownloadChapter>){
        if(chapterList.isEmpty()){
            return
        }
        val iterator = chapterList.iterator()
        while (iterator.hasNext()){
            val ne = iterator.next()
            if(downloadingChapterList.value!=null){
                downloadingChapterList.value!!.forEach{
                    if(it.path_url == ne.path_url){
                        iterator.remove()
                    }else{
                        DaoUtil(DownloadChapter::class.java, "").saveOrUpdate(it)
                    }
                }
            }
        }
        if(chapterList.size == 0){
            ToastUtil.show(BaseApplication.CONTEXT, BaseApplication.CONTEXT.getString(R.string.business_download_all_exist))
        }else{
            isDownAll.set(true)
            ToastUtil.show(BaseApplication.CONTEXT, BaseApplication.CONTEXT.getString(R.string.business_download_add_cache))
            downloadingChapterList.addAll(chapterList)
            downloadingNum.set(downloadingNum.get()+chapterList.size)
//            downloadService.startDownloadWithCache(chapterList)
            AriaDownloadManager.startDownload(chapterList[0])
        }
    }


    fun addDownloadingChapter(chapter: DownloadChapter) {
        isDownAll.set(false)
        if(downloadingChapterList.value!=null){
            val downloadList = downloadingChapterList.value!!
            downloadList.forEach {
                if(it.path_url == chapter.path_url){
                    ToastUtil.show(BaseApplication.CONTEXT, BaseApplication.CONTEXT.getString(R.string.business_download_all_exist))
                    return
                }
            }
        }
        ToastUtil.show(BaseApplication.CONTEXT, BaseApplication.CONTEXT.getString(R.string.business_download_add_cache))
        downloadingChapterList.add(chapter)
        downloadingNum.set(downloadingNum.get()+1)
        downloadService.startDownloadWithCache(chapter)
    }

    /**
     * 下载完成，自动下载下一章节
     */
    fun downFinishAndAutoDownNext(){
        if(downloadingChapterList.value == null){
            DLog.d("suolong下载 自动下载下一章","下载队列为空")
            return
        }

        if(downloadingChapterList.value!= null){
            val downList = downloadingChapterList.value!!
            when (downList.size) {
                0 -> {
                    DLog.d("suolong下载 自动下载下一章","下载队列大小为0")
                    return
                }
                1 -> {
                    if(downloadingChapter.get()!=null){
                        if(downList[0].chapter_id == downloadingChapter.get()!!.chapter_id){
                            val tempChapter = DownloadChapter()
                            tempChapter.audio_id = 0L
                            tempChapter.chapter_id = 0L
                            tempChapter.path_url = ""
                            downloadingChapter.set(tempChapter)
                            downloadingChapter.notifyChange()
                            downloadingChapterList.clear()
                            downloadingNum.set(0)
                        }else{
                            DLog.d("suolong下载 自动下载下一章","downList[0] != downloadingChapter")
                        }
                    }else{
                        DLog.d("suolong下载 自动下载下一章","downloadingChapter == null")
                    }
                }
                else -> {
                    if(downloadingChapter.get()!=null){
                        val downloadChapter = downloadingChapter.get()!!
                        for (i in 0 until downList.size){
                            if(downList[i].chapter_id == downloadChapter.chapter_id){
                                if(i == downList.size -1){
                                    DLog.d("suolong下载 自动下载下一章","开始下载downList[0] + name = ${downList[0].chapter_name}")
//                                    downloadService.startDownloadWithCache(downList[0])
                                    AriaDownloadManager.startDownload(downList[0])
                                }else{
                                    DLog.d("suolong下载 自动下载下一章","开始下载downList[i+1] + name = ${downList[i+1].chapter_name}")
//                                    downloadService.startDownloadWithCache(downList[i+1])
                                    AriaDownloadManager.startDownload(downList[1])
                                }
                                return
                            }
                        }
                    }
                }
            }
        }
    }


    fun downloadingChapterClick(chapter: DownloadChapter){
        val downloadChapter = downloadingChapter.get()
        if(downloadChapter!=null){
            if(chapter.chapter_id == downloadChapter.chapter_id){
                if(downloadChapter.isDownloading){
                    isPauseAll.set(false)
                    pauseDownloadingChapter()
                }else{
                    isDownAll.set(false)
                    downloadService.startDownloadWithCache(chapter)
                }
            }else{
                isDownAll.set(false)
                pauseDownloadingChapter()
                downloadService.startDownloadWithCache(chapter)
            }
        }else{
            addDownloadingChapter(chapter)
        }
    }

    /**
     * 停止当前章节，开始下载下一个任务
     */
    fun pauseCurrentAndDownNextChapter(){
        DLog.d("suolong下载","停止当前章节，开始下载下一个任务")
        if(downloadingChapterList.value == null){
            return
        }

        if(downloadingChapterList.value!= null){
            val downList = downloadingChapterList.value!!
            when (downList.size) {
                0 -> {
                    return
                }
                1 -> {
                    pauseDownloadingChapter()
//                    downloadService.pauseDownload(downList[0])
                }
                else -> {
                    if(downloadingChapter.get()!=null){
                        val downloadChapter = downloadingChapter.get()!!
                        downloadChapter.down_status = DownloadConstant.CHAPTER_STATUS_DOWNLOAD_PAUSE
                        downloadingChapter.set(downloadChapter)
                        downloadingChapter.notifyChange()
                        downloadService.pauseDownload(downloadChapter)

                        for (i in 0 until downList.size){
                            if(downList[i].chapter_id == downloadChapter.chapter_id){
                                if(i == downList.size -1){
                                    downloadService.startDownloadWithCache(downList[0])
                                }else{
                                    downloadService.startDownloadWithCache(downList[i+1])
                                }
                                return
                            }
                        }
                    }
                }
            }
        }
    }

    fun updateDownloadingChapter(url: String, status: Int) {
        if (downloadingChapterList.value == null) {
            downloadingChapterList.value = mutableListOf()
        }
        val tempList = downloadingChapterList.value
        tempList?.forEach {
            if (it.path_url == url) {
                if(downloadingChapter.get()!=null){
                    downloadingChapter.set(it)
                    if(downloadingChapter.get()!!.path_url == it.path_url){
                        downloadingChapter.notifyChange()
                    }
                }else{
                    downloadingChapter.set(it)
                }
                it.down_status = status
                downloadingChapterList.value = tempList
                DaoUtil(DownloadChapter::class.java, "").saveOrUpdate(it)
                return
            }
        }
    }

    fun downloadClickChapter(clickChapter: DownloadChapter){
        if(downloadingChapter.get()!=null){
            val chapter = downloadingChapter.get()!!
            chapter.down_status = DownloadConstant.CHAPTER_STATUS_DOWNLOAD_PAUSE
            downloadingChapter.set(chapter)
            downloadService.pauseDownload(chapter)
            downloadService.startDownloadWithCache(clickChapter)
        }else{
            downloadingChapter.set(clickChapter)
            downloadService.startDownloadWithCache(clickChapter)
        }
    }

    fun updateDownloadingSpeed(url: String,speed: String,currentOffset : Long) {
        if(downloadingChapter.get()!=null){
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
        if(downloadingChapter.get()!=null){
            val chapter = downloadingChapter.get()!!
            chapter.down_status = DownloadConstant.CHAPTER_STATUS_DOWNLOAD_PAUSE
            downloadingChapter.set(chapter)
            downloadingChapter.notifyChange()
            downloadService.pauseDownload(chapter)
        }
    }

    fun pauseAllChapter() {
        if(downloadingChapter.get()!=null){
            val chapter = downloadingChapter.get()!!
            chapter.down_status = DownloadConstant.CHAPTER_STATUS_DOWNLOAD_PAUSE
            downloadingChapter.set(chapter)
            downloadingChapter.notifyChange()
            downloadService.stopAll()
        }
    }


    fun resumeDownloadingChapter() {
        if(downloadingChapter.get()!=null){
            val chapter = downloadingChapter.get()!!
            if (chapter.down_status == DownloadConstant.CHAPTER_STATUS_DOWNLOAD_PAUSE) {
                chapter.down_status = DownloadConstant.CHAPTER_STATUS_DOWNLOADING
            }
            downloadingChapter.set(chapter)
            downloadingChapter.notifyChange()
            downloadService.startDownloadWithCache(chapter)
        }
    }


    fun editDownloading() {
        pauseDownloadingChapter()
    }

    fun quitEditDownloading() {
        resumeDownloadingChapter()
    }


    fun pauseAllDownload(){

    }

    fun startDownAllAndAutoNext(){

    }


    fun operatingAll(){
        if(downloadingChapter.get()!=null){
            val chapter = downloadingChapter.get()!!
            if(chapter.isDownloading){
                isPauseAll.set(true)
                isDownAll.set(false)
                pauseAllChapter()
            }else{
                isPauseAll.set(false)
                isDownAll.set(true)
                resumeDownloadingChapter()
            }
        }else{
            if(downloadingChapterList.value!=null){
                val chapterList = downloadingChapterList.value!!
                if(chapterList.size>0){
                    isPauseAll.set(false)
                    downloadService.startDownloadWithCache(chapterList[0])
                }
            }
        }
    }


    fun setDownloadFinishChapter(filePath : String) {
        val finishChapter = downloadingChapter.get()
        if ( finishChapter!= null) {
            downloadFinishChapterList.add(finishChapter)
            finishChapter.down_status = DownloadConstant.CHAPTER_STATUS_DOWNLOAD_FINISH
            finishChapter.file_path = filePath
            downloadingChapter.set(finishChapter)
            downloadingChapter.notifyChange()
            DaoUtil(DownloadChapter::class.java, "").saveOrUpdate(finishChapter)
            updateDownloadingAudio(
                chapter = finishChapter
            )
            if(isDownAll.get()){
                DLog.d("suolong下载","下载_开始寻找下一个下载任务")
                downFinishAndAutoDownNext()
            }

            if(downloadingChapterList.value!=null){
                val value = downloadingChapterList.value!!
                if(value.size>0){
                    downloadingChapterList.remove(finishChapter)
                    downloadingNum.set(downloadingNum.get()-1)
                }
            }
        }
    }

    fun deleteDownloadingChapter(chapter: DownloadChapter) {
        downloadingChapterList.remove(chapter)
        downloadingNum.set(downloadingNum.get()-1)

        DaoUtil(DownloadChapter::class.java, "").delete(chapter)
        downloadService.deleteDownload(chapter)

    }

    fun deleteDownloadingChapter(chapterList: List<DownloadChapter>) {
        downloadingChapterList.removeAll(chapterList)
        downloadingNum.set(downloadingNum.get()-chapterList.size)
        DaoUtil(DownloadChapter::class.java, "").delete(chapterList)
        downloadService.deleteDownload(chapterList.toMutableList())
    }

    fun getDownAudioOnAppCreate() {
        GlobalScope.launch(Dispatchers.IO) {
            val audioList = DaoUtil(DownloadAudio::class.java, "").queryAll()
            val downChapterList = mutableListOf<DownloadChapter>()
            if (audioList != null) {
                downloadingAudioList.postValue(audioList.toMutableList())
                if (audioList.size >=2) {
                    audioList[1].listen_finish = true
                }
                audioList.forEach{audio ->
                    audio.edit_select = false
                    val chapterList = audio.chapterList
                    chapterList.forEach { chapter ->
                        chapter.chapter_edit_select = false
                        chapter.down_edit_select = false
                        if(!chapter.isDownloadFinish){
                           downChapterList.add(chapter)
                       }
                    }
                }
                if(downChapterList.size>0){
                    isDownAll.set(true)
                    downloadService.startDownloadWithCache(downChapterList)
                    downloadingChapterList.postValue(downChapterList)
                }
            }
        }
    }

}