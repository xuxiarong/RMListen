package com.rm.business_lib

import androidx.annotation.IntDef
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableLong
import androidx.lifecycle.MutableLiveData
import com.rm.baselisten.BaseApplication
import com.rm.baselisten.ktx.add
import com.rm.baselisten.ktx.addAll
import com.rm.baselisten.ktx.remove
import com.rm.baselisten.ktx.removeAll
import com.rm.baselisten.util.DLog
import com.rm.baselisten.util.ToastUtil
import com.rm.business_lib.bean.LoginUserBean
import com.rm.business_lib.db.DaoUtil
import com.rm.business_lib.db.download.DownloadAudio
import com.rm.business_lib.db.download.DownloadChapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * desc   : 基础业务常量类
 * date   : 2020/09/01
 * version: 1.0
 */

// ******** login ********
// 访问令牌(token)
const val ACCESS_TOKEN = "accessToken"

// 刷新令牌(token)
const val REFRESH_TOKEN = "refreshToken"

// 当前访问token失效时间
const val ACCESS_TOKEN_INVALID_TIMESTAMP = "accessTokenInvalidTimestamp"

// 当前登陆用户信息
const val LOGIN_USER_INFO = "loginUserInfo"

// 当前是否登陆
var isLogin = ObservableBoolean(false)

// 当前登陆的用户信息
var loginUser = ObservableField<LoginUserBean>()

var isHomeDouClick = MutableLiveData(false)


// ******** home ********
//是否是第首次收藏
const val IS_FIRST_FAVORITES = "is_first_favorites"

//是否是首次添加听单
const val IS_FIRST_ADD_SHEET = "is_first_add_sheet"

//是否是首次订阅
const val IS_FIRST_SUBSCRIBE = "is_first_subscribe"


// ******** 我听 ********
@IntDef(LISTEN_SHEET_LIST_MY_LIST, LISTEN_SHEET_LIST_COLLECTED_LIST)
annotation class ListenSheetListType(val type: Int = LISTEN_SHEET_LIST_MY_LIST)

const val LISTEN_SHEET_LIST_MY_LIST = 0 //我的听单
const val LISTEN_SHEET_LIST_COLLECTED_LIST = 1 //收藏听单


object DownloadMemoryCache {

    var downloadingAudioList = MutableLiveData<MutableList<DownloadAudio>>(mutableListOf())
    var downloadingChapterList = MutableLiveData<MutableList<DownloadChapter>>(mutableListOf())
    var downloadingChapter = MutableLiveData<DownloadChapter>()
    var downloadFinishChapterList = MutableLiveData<MutableList<DownloadChapter>>(mutableListOf())
    var downloadingChapterSpeed = ObservableLong(0L)

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
            ToastUtil.show(BaseApplication.CONTEXT,BaseApplication.CONTEXT.getString(R.string.business_download_all_exist))
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

    fun updateDownloadingSpeed(speed: Long) {
        downloadingChapterSpeed.set(speed)
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

