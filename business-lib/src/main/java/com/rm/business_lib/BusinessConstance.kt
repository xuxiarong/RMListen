package com.rm.business_lib

import android.content.Context
import androidx.annotation.IntDef
import androidx.databinding.*
import androidx.lifecycle.MutableLiveData
import com.rm.baselisten.BaseConstance
import com.rm.baselisten.ktx.addAll
import com.rm.baselisten.util.DLog
import com.rm.baselisten.util.TimeUtils
import com.rm.business_lib.bean.LoginUserBean
import com.rm.business_lib.db.DaoUtil
import com.rm.business_lib.db.converter.BusinessConvert
import com.rm.business_lib.db.download.DownloadAudio
import com.rm.business_lib.db.download.DownloadChapter
import com.rm.business_lib.db.listen.ListenAudioEntity
import com.rm.business_lib.db.listen.ListenChapterEntity
import com.rm.business_lib.play.PlayState
import java.lang.Exception

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

object HomeGlobalData {
    var isHomeDouClick = MutableLiveData(false)
    var homeGlobalSelectTab = ObservableInt(0)
}


object PlayGlobalData {

    /**
     * 以下状态是Google的播放器原生状态，请不要随意修改
     */
    //空闲
    var STATE_IDLE = 1

    //缓冲
    var STATE_BUFFERING = 2

    //播放准备好
    var STATE_READY = 3

    //全部播放完毕
    var STATE_ENDED = 4

    //章节加载开始页
    const val PLAY_FIRST_PAGE = 1

    //章节每页加载数量
    const val PLAY_PAGE_SIZE = 20


    /**
     * 当前音频id
     */
    val playAudioId = ObservableField<String>("")

    /**
     * 播放的书籍详情
     */
    val playAudioModel = ObservableField<DownloadAudio>()

    /**
     * 播放的章节id
     */
    val playChapterId = ObservableField<String>("")

    /**
     * 播放的章节
     */
    var playChapter = ObservableField<DownloadChapter>(DownloadChapter())

    /**
     * 播放的章节列表
     */
    val playChapterList = MutableLiveData<MutableList<DownloadChapter>>()

    /**
     * 章节列表倒叙逆序
     */
    var playChapterListSort = ObservableField<String>(AudioSortType.SORT_ASC)

    /**
     * 播放的进度
     */
    val process = ObservableFloat(0f)//进度条

    /**
     * 播放的最大进度
     */
    val maxProcess = ObservableFloat(0f)

    /**
     * 播放进度条上的文字
     */
    val updateThumbText = ObservableField<String>("00:00/00:00")

    //全局播放器定时时间
    var playTimerDuration = ObservableLong(0L)

    //全局播放器播放速度
    var playSpeed = ObservableFloat(1.0f)

    var playState = ObservableField<PlayState>(PlayState())

    /**
     * 是否有上一章
     */
    var hasPreChapter = ObservableBoolean(false)

    /**
     * 是否有下一章
     */
    var hasNextChapter = ObservableBoolean(false)

    /**
     * 书籍播放的数据库对象
     */
    private val playAudioDao = DaoUtil(ListenAudioEntity::class.java, "")

    /**
     * 章节播放的数据库对象
     */
    private val playChapterDao = DaoUtil(ListenChapterEntity::class.java, "")


    fun initPlayAudio(audio: DownloadAudio) {
        playAudioModel.set(audio)
        BaseConstance.updateBaseAudioId(
                audioId = audio.audio_id.toString(),
                playUrl = audio.audio_cover_url
        )
        audio.updateMillis = System.currentTimeMillis()
        playChapterListSort.get()?.let {
            audio.sortType = it
        }
        playChapterId.get()?.let {
            audio.listenChapterId = it
        }
        playAudioDao.saveOrUpdate(BusinessConvert.convertToListenAudio(audio))
    }

    fun initPlayChapter(chapter: DownloadChapter) {
        playChapter.set(chapter)
        process.set(chapter.listen_duration.toFloat())
        playChapterId.set(chapter.chapter_id.toString())
        playChapterDao.saveOrUpdate(BusinessConvert.convertToListenChapter(chapter))
    }

    fun updatePlayChapterProgress(
            currentDuration: Long = 0L,
            totalDuration: Long = 0L,
            isPlayFinish: Boolean = false
    ) {
        try {
            val chapter = playChapter.get()
            if (chapter != null) {

                 if (isPlayFinish) {
                     chapter.listen_duration =  chapter.realDuration
                } else {
                     chapter.listen_duration =  currentDuration
                }
                process.set(chapter.listen_duration.toFloat())
                updateThumbText.set(
                        "${TimeUtils.getPlayDuration(chapter.listen_duration)}/${
                        TimeUtils.getPlayDuration(chapter.realDuration)
                        }"
                )
                playChapter.set(chapter)
                playChapterId.set(chapter.chapter_id.toString())
                chapter.updateMillis = System.currentTimeMillis()
                chapter.duration = totalDuration
                playChapterDao.saveOrUpdate(BusinessConvert.convertToListenChapter(chapter))
                val audio = playAudioModel.get()
                if (audio != null) {
                    audio.updateMillis = System.currentTimeMillis()
                    audio.listenChapterId = chapter.chapter_id.toString()
                    playAudioDao.saveOrUpdate(BusinessConvert.convertToListenAudio(audio))
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun savePlayChapter(position: Int) {
        val playChapterList = playChapterList.value
        if (playChapterList != null && playChapterList.size > 0) {
            if (position <= playChapterList.size - 1) {
                val startChapter = playChapterList[position]
                playChapter.set(startChapter)
                playChapterId.set(startChapter.chapter_id.toString())
                process.set(startChapter.listen_duration.toFloat())
                startChapter.updateMillis = System.currentTimeMillis()
                playChapterDao.saveOrUpdate(BusinessConvert.convertToListenChapter(startChapter))
                val audio = playAudioModel.get()
                if (audio != null) {
                    audio.updateMillis = System.currentTimeMillis()
                    audio.listenChapterId = startChapter.chapter_id.toString()
                    playAudioDao.saveOrUpdate(BusinessConvert.convertToListenAudio(audio))
                }
            }
        }
    }
}

object AudioSortType {

    //倒序
    const val SORT_DESC = "desc"

    //正序
    const val SORT_ASC = "asc"
}

object LoginPhoneReminder {
    var phoneMap = hashMapOf<Int, String>()

    fun getCurrentActivityInputPhone(context: Context): String {
        if (phoneMap.size > 0 && phoneMap[context.applicationContext.hashCode()] != null) {
            return phoneMap[context.applicationContext.hashCode()]!!
        }
        return ""
    }

    fun putCurrentActivityInputPhone(context: Context, phone: String) {
        phoneMap.clear()
        phoneMap[context.applicationContext.hashCode()] = phone
    }

}

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


val SAVA_SPEED = "savaSpeed"
