package com.rm.module_download

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.rm.baselisten.BaseApplication
import com.rm.baselisten.ktx.add
import com.rm.baselisten.ktx.addAll
import com.rm.baselisten.ktx.remove
import com.rm.baselisten.ktx.removeAll
import com.rm.baselisten.util.ConvertUtils
import com.rm.baselisten.util.DLog
import com.rm.baselisten.util.ToastUtil
import com.rm.business_lib.DownloadConstant
import com.rm.business_lib.R
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

    var downloadingAudioList = MutableLiveData<MutableList<DownloadAudio>>(mutableListOf())
    var downloadingChapterList = MutableLiveData<MutableList<DownloadChapter>>(mutableListOf())
    var downloadingChapter = MutableLiveData<DownloadChapter>()
    var downloadFinishChapterList = MutableLiveData<MutableList<DownloadChapter>>(mutableListOf())
    var downloadingChapterSpeed = ObservableField<String>("")
    var downloadingChapterCurrentSize = ObservableField<String>("")

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
            downloadingChapterList.addAll(chapterList)
        }
    }


    fun addDownloadingChapter(chapter: DownloadChapter) {
        if(downloadingChapterList.value!=null){
            val downloadList = downloadingChapterList.value!!
            downloadList.forEach {
                if(it.path_url == chapter.path_url){
                    DLog.d("suolong","${chapter.chapter_name}已存在下载队列中")
                    return
                }
            }
        }
        downloadingChapterList.add(chapter)
    }


    fun updateDownloadingChapter(url: String, status: Int) {
        if (downloadingChapterList.value == null) {
            downloadingChapterList.value = mutableListOf()
        }
        val tempList = downloadingChapterList.value
        tempList?.forEach {
            if (it.path_url == url) {
                downloadingChapter.value = it
                it.down_status = status
                downloadingChapterList.value = tempList
                return
            }
        }
    }

    fun updateDownloadingSpeed(url: String,speed: String,currentOffset : Long) {
        downloadingChapterSpeed.set(speed)
        downloadingChapterCurrentSize.set(ConvertUtils.byte2FitMemorySize(currentOffset,1))
//        updateDownloadingChapter(url,DownloadConstant.CHAPTER_STATUS_DOWNLOADING)
    }

    fun pauseDownloadingChapter() {
        if (downloadingChapter.value != null) {
            val chapter = downloadingChapter.value!!
            chapter.down_status = DownloadConstant.CHAPTER_STATUS_DOWNLOAD_PAUSE
            downloadingChapter.value = chapter
        }
    }

    fun resumeDownloadingChapter() {
        if (downloadingChapter.value != null) {
            val chapter = downloadingChapter.value!!
            if (chapter.down_status == DownloadConstant.CHAPTER_STATUS_DOWNLOAD_PAUSE) {
                chapter.down_status = DownloadConstant.CHAPTER_STATUS_DOWNLOADING
            }
            downloadingChapter.value = chapter
        }
    }


    fun editDownloading() {
        pauseDownloadingChapter()
    }

    fun quitEditDownloading() {
        resumeDownloadingChapter()
    }


    fun setDownloadFinishChapter() {
        if (downloadingChapter.value != null) {
            downloadingChapterList.remove(downloadingChapter.value!!)
            downloadFinishChapterList.add(downloadingChapter.value!!)
            DaoUtil(DownloadChapter::class.java, "").saveOrUpdate(downloadingChapter.value!!)
            updateDownloadingAudio(chapter = downloadingChapter.value!!)

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
            if (audioList != null) {
                downloadingAudioList.postValue(audioList.toMutableList())
            }
        }
    }

}