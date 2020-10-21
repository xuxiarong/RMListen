package com.rm.business_lib

import androidx.annotation.IntDef
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.rm.baselisten.ktx.add
import com.rm.baselisten.ktx.remove
import com.rm.business_lib.bean.LoginUserBean
import com.rm.business_lib.bean.download.DownloadAudioStatusModel
import com.rm.business_lib.bean.download.DownloadChapterStatusModel
import com.rm.business_lib.bean.download.DownloadProgressUpdateBean
import com.rm.business_lib.bean.download.DownloadStatusChangedBean
import com.rm.business_lib.db.DaoUtil
import com.rm.business_lib.db.download.DownloadAudio
import com.rm.business_lib.db.download.DownloadChapter

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


// ******** download ********
val downloadStatus = MutableLiveData<DownloadStatusChangedBean>()
val downloadProgress = MutableLiveData<DownloadProgressUpdateBean>()

object DownloadMemoryCache{
    var downloadingAudioList = MutableLiveData<MutableList<DownloadAudioStatusModel>>()
    var downloadingChapterList = MutableLiveData<MutableList<DownloadChapterStatusModel>>()
    var downloadingChapter = MutableLiveData<DownloadChapterStatusModel>()
    var downloadFinishChapterList = MutableLiveData<MutableList<DownloadChapterStatusModel>>()

    fun addAudioToDownloadMemoryCache(model: DownloadAudioStatusModel){
        if(null!=downloadingAudioList.value){
            if(!downloadingAudioList.value!!.contains(element = model)){
                downloadingAudioList.add(element = model)
                DaoUtil(DownloadAudio::class.java, "").saveOrUpdate(model.audio)
            }
        }else{
            downloadingAudioList.add(element = model)
            DaoUtil(DownloadAudio::class.java, "").saveOrUpdate(model.audio)
        }
    }


    fun addChapterToDownloadMemoryCache(url : String,speed : Long){
        if(downloadingChapter.value == null){
            updateDownloadingChapter(url)
        }else{
            val chapter = downloadingChapter.value
            chapter?.speed = speed
            downloadingChapter.value = chapter
        }
    }

    fun updateDownloadingChapter(url: String){
        downloadingChapterList.value?.forEach {
            if(it.chapter.path_url == url){
                downloadingChapter.value = it
            }
        }
    }

    fun setDownloadFinishChapter(){
        if(downloadingChapter.value!=null){
            downloadingChapterList.remove(downloadingChapter.value!!)
            downloadFinishChapterList.add(downloadingChapter.value!!)
            DaoUtil(DownloadChapter::class.java, "").saveOrUpdate(downloadingChapter.value!!.chapter)
        }
    }
}

