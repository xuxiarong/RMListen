package com.rm.module_download

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.rm.baselisten.BaseApplication
import com.rm.baselisten.ktx.add
import com.rm.baselisten.ktx.addAll
import com.rm.baselisten.ktx.remove
import com.rm.baselisten.ktx.removeAll
import com.rm.baselisten.util.DLog
import com.rm.baselisten.util.ToastUtil
import com.rm.business_lib.DownloadConstant
import com.rm.business_lib.R
import com.rm.business_lib.db.DaoUtil
import com.rm.business_lib.db.download.DownloadAudio
import com.rm.business_lib.db.download.DownloadChapter
import com.rm.component_comm.download.DownloadService
import com.rm.component_comm.router.RouterHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * desc   :
 * date   : 2020/10/27
 * version: 1.0
 */
object DownloadMemoryCache {

    var downloadingAudioList = MutableLiveData<MutableList<DownloadAudio>>(mutableListOf())
    var downloadingChapterList = MutableLiveData<MutableList<DownloadChapter>>(mutableListOf())
    var downloadingChapter = ObservableField<DownloadChapter>()
    var downloadFinishChapterList = MutableLiveData<MutableList<DownloadChapter>>(mutableListOf())
    val downloadService = RouterHelper.createRouter(DownloadService::class.java)

    fun addAudioToDownloadMemoryCache(audio: DownloadAudio) {
        if (!isDownloadingAudio(audio)) {
            downloadingAudioList.add(element = audio)
            DaoUtil(DownloadAudio::class.java, "").saveOrUpdate(audio)
        }
    }

    fun isDownloadingAudio(audio: DownloadAudio): Boolean {
        if (null != downloadingAudioList.value) {
            val audioList = downloadingAudioList.value!!
            audioList.forEach {
                if (it.audio_id == audio.audio_id) {
                    return true
                }
            }
        }
        return false
    }

    fun updateDownloadingAudio(chapter: DownloadChapter): Boolean {
        if (null != downloadingAudioList.value) {
            val audioList = downloadingAudioList.value!!
            audioList.forEach {
                if (it.audio_id == chapter.audio_id) {
                    it.download_num += 1
                    it.down_size += chapter.size
                    downloadingAudioList.addAll(audioList)
                    DLog.d("suolong","name = ${chapter.audio_name}" + it.down_size)
                    return true
                }
            }
        }
        return false
    }

    fun addAudioToDownloadMemoryCache(audioList: List<DownloadAudio>) {
        audioList.forEach {
            addAudioToDownloadMemoryCache(it)
        }
    }

    fun deleteAudioToDownloadMemoryCache(audioList: List<DownloadAudio>) {
        audioList.forEach {
            deleteAudioToDownloadMemoryCache(it)
        }
    }

    fun deleteAudioToDownloadMemoryCache(audio: DownloadAudio) {
        if (isDownloadingAudio(audio)) {
            downloadingAudioList.remove(audio)
            DaoUtil(DownloadAudio::class.java, "").delete(audio)
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
                    }
                }
            }
        }
        if(chapterList.size == 0){
            ToastUtil.show(BaseApplication.CONTEXT, BaseApplication.CONTEXT.getString(R.string.business_download_all_exist))
        }else{
            ToastUtil.show(BaseApplication.CONTEXT, BaseApplication.CONTEXT.getString(R.string.business_download_add_cache))
            downloadingChapterList.addAll(chapterList)
        }
    }


    fun addDownloadingChapter(chapter: DownloadChapter) {
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
    }


    /**
     * 停止当前章节，开始下载下一个任务
     */
    fun pauseCurrentAndDownNextChapter(){

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
        }
    }


    fun editDownloading() {
        pauseDownloadingChapter()
    }

    fun quitEditDownloading() {
        resumeDownloadingChapter()
    }


    fun setDownloadFinishChapter() {
        val finishChapter = downloadingChapter.get()
        if ( finishChapter!= null) {
            downloadingChapterList.remove(finishChapter)
            downloadFinishChapterList.add(finishChapter)
            finishChapter.down_status = DownloadConstant.CHAPTER_STATUS_DOWNLOAD_FINISH
            DaoUtil(DownloadChapter::class.java, "").saveOrUpdate(finishChapter)
            updateDownloadingAudio(chapter = finishChapter)
        }
    }

    fun deleteDownloadingChapter(chapter: DownloadChapter) {
        downloadingChapterList.remove(chapter)
        DaoUtil(DownloadChapter::class.java, "").delete(chapter)
    }

    fun deleteDownloadingChapter(chapterList: List<DownloadChapter>) {
        downloadingChapterList.removeAll(chapterList)
        DaoUtil(DownloadChapter::class.java, "").delete(chapterList)
    }

    fun getDownAudioOnAppCreate() {
        GlobalScope.launch(Dispatchers.IO) {
            val audioList = DaoUtil(DownloadAudio::class.java, "").queryAll()
            val downChapterList = mutableListOf<DownloadChapter>()
            if (audioList != null) {
                downloadingAudioList.postValue(audioList.toMutableList())
                audioList.forEach{
                    val chapterList = it.chapterList
                    chapterList.forEach { chapter ->
                        if(!chapter.isDownloadFinish){
                           downChapterList.add(chapter)
                       }
                    }
                }
                if(downChapterList.size>0){
                    downloadService.startDownloadWithCache(downChapterList)
                    downloadingChapterList.postValue(downChapterList)
                }
            }
        }
    }

}