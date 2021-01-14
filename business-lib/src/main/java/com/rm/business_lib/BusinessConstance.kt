package com.rm.business_lib

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.os.Message
import android.text.TextUtils
import androidx.annotation.IntDef
import androidx.databinding.*
import androidx.lifecycle.MutableLiveData
import com.rm.baselisten.BaseConstance
import com.rm.baselisten.ktx.addAll
import com.rm.baselisten.ktx.toLongSafe
import com.rm.baselisten.util.DLog
import com.rm.baselisten.util.TimeUtils
import com.rm.baselisten.util.getBooleanMMKV
import com.rm.business_lib.bean.BusinessAdModel
import com.rm.business_lib.bean.LoginUserBean
import com.rm.business_lib.db.DaoUtil
import com.rm.business_lib.db.converter.BusinessConvert
import com.rm.business_lib.db.download.DownloadAudio
import com.rm.business_lib.db.download.DownloadChapter
import com.rm.business_lib.db.listen.ListenAudioEntity
import com.rm.business_lib.db.listen.ListenChapterEntity
import com.rm.business_lib.db.listen.ListenDaoUtils
import com.rm.business_lib.download.file.DownLoadFileUtils
import com.rm.business_lib.wedgit.smartrefresh.model.SmartRefreshLayoutStatusModel
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

//记录提示更新时间
const val UPLOAD_APP_TIME = "upload_app_time"

//记录用户第一次打开app
const val FIRST_OPEN_APP = "first_open_app"

// 当前是否登陆
var isLogin = ObservableBoolean(false)

// 当前登陆的用户信息
var loginUser = ObservableField<LoginUserBean>()

object HomeGlobalData {

    const val HOME_SELECT = 0
    const val SEARCH_SELECT = 1
    const val LISTEN_SELECT = 2
    const val MINE_SELECT = 3

    var isHomeDouClick = MutableLiveData(false)
    var homeGlobalSelectTab = ObservableInt(HOME_SELECT)
    var isShowSubsRedPoint = ObservableBoolean(false)
    var isListenAppBarInTop = ObservableBoolean(false)

    var LISTEN_SELECT_MY_LISTEN = 0
    var LISTEN_SELECT_SUBS_UPDATE = 1
    var myListenSelectTab = ObservableInt(LISTEN_SELECT_MY_LISTEN)

    const val HOME_IS_AGREE_PRIVATE_PROTOCOL = "home_is_agree_private_protocol"

}

object PlaySettingData {
    const val PLAY_NETWORK_234G_ALERT = "play_network_234g_alert"
    const val PLAY_AUTO_PLAY_NEXT = "play_auto_play_next"
    const val PLAY_CONTINUE_LAST_PLAY = "play_continue_last_play"

    fun getNetwork234GAlert(): Boolean {
        return PLAY_NETWORK_234G_ALERT.getBooleanMMKV(true)
    }

    fun getAutoPlayNext(): Boolean {
        return PLAY_AUTO_PLAY_NEXT.getBooleanMMKV(true)
    }

    fun getContinueLastPlay(): Boolean {
        return PLAY_CONTINUE_LAST_PLAY.getBooleanMMKV(false)
    }
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

    //进度条消息
    @SuppressLint("HandlerLeak")
    private val playTimerHandler = object : Handler() {
        override fun handleMessage(msg: Message) {
            DLog.d("suolong", "playTimerHandler")
            val currentTimerSecond = playCountDownSecond.get()
            if (currentTimerSecond > 0) {
                playCountDownSecond.set(currentTimerSecond - 1000L)
                if (currentTimerSecond - 1000L == 0L) {
                    playCountSelectPosition.set(-1)
                }
                sendEmptyMessageDelayed(0, 1000L)
            } else {
                playCountDownSecond.set(-1000L)
                playCountSelectPosition.set(-1)
                removeMessages(0)
            }
        }
    }

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
     * 评论 SmartRefreshLayout的状态变化
     */
    val chapterRefreshModel = SmartRefreshLayoutStatusModel()

    /**
     * 章节列表倒叙逆序
     */
    var playChapterListSort = ObservableField<String>(AudioSortType.SORT_ASC)

    /**
     * 加载下一页的当前页码
     */
    var playNextPage = PLAY_FIRST_PAGE

    /**
     * 加载上一页的当前页码
     */
    var playPrePage = PLAY_FIRST_PAGE

    /**
     * 播放章节总共的页码
     */
    var playChapterTotal = 1

    /**
     * 章节每页数量
     */
    var playChapterPageSize = PLAY_PAGE_SIZE

    /**
     * 播放的进度
     */
    val process = ObservableFloat(0f)//进度条

    /**
     * 播放的最大进度
     */
    val maxProcess = ObservableFloat(0f)

    /**
     * 是否需要重新查询播放章节的进度
     */
    var playNeedQueryChapterProgress = ObservableBoolean(true)

    /**
     * 上一次的播放记录
     */
    var playLastPlayProcess = ObservableLong(-1L)

    /**
     * 播放进度条上的文字
     */
    val updateThumbText = ObservableField<String>("00:00/00:00")

    //全局播放器播放速度
    var playSpeed = ObservableFloat(1.0f)

    /**
     * 播放是否出错了
     */
    var playIsError = ObservableBoolean(false)

    /**
     * 是否有上一章
     */
    var hasPreChapter = ObservableBoolean(false)

    /**
     * 是否有下一章
     */
    var hasNextChapter = ObservableBoolean(false)

    /**
     * 剩余倒计时秒数
     */
    var playCountDownSecond = ObservableLong(-10000L)

    /**
     *  剩余倒计时集数
     */
    var playCountDownChapterSize = ObservableInt(-5)

    /**
     *选择倒计时的position
     */
    var playCountSelectPosition = ObservableInt(-1)

    /**
     * 倒计时一共十个选项 其中前五项为时间秒数倒计时，后五项为集数倒计时
     * ["10", "20", "30", "40", "60", "1", "2", "3", "4", "5"]
     */
    val playCountTimerList by lazy {
        arrayListOf(10, 20, 30, 40, 60, 1, 2, 3, 4, 5)
    }


    /**
     * 书籍播放的数据库对象
     */
    private val playAudioDao = DaoUtil(ListenAudioEntity::class.java, "")

    /**
     * 下载书籍的数据库对象
     */
    private val playDownloadDao = DaoUtil(DownloadChapter::class.java, "")

    /**
     * 章节播放的数据库对象
     */
    private val playChapterDao = DaoUtil(ListenChapterEntity::class.java, "")

    /**
     * 音频封面广告
     */
    var playAudioImgAd = ObservableField<BusinessAdModel>()


    /**
     * 是否正在播放广告
     */
    var playAdIsPlaying = ObservableBoolean(false)
    var playVoiceImgAd = ObservableField<BusinessAdModel>()
    var playVoiceAdClose = ObservableField(true)


    fun initPlayAudio(audio: DownloadAudio) {
        playAudioModel.set(audio)
        BaseConstance.updateBaseAudioId(
            audioId = audio.audio_id.toString(),
            playUrl = audio.audio_cover_url,
            playSort = playChapterListSort.get()?:"asc"
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


    /**
     * 设置播放路径
     */
    fun setNextPagePlayData(chapterList: MutableList<DownloadChapter>) {
        DLog.d("music-exoplayer-lib", "设置公共数据 setNextPagePlayData")
        playChapterList.addAll(chapterList)
    }

    fun setPrePagePlayData(chapterList: MutableList<DownloadChapter>) {
        DLog.d("music-exoplayer-lib", "设置公共数据 setPrePagePlayData")
        val tempChapterList = mutableListOf<DownloadChapter>()
        tempChapterList.addAll(chapterList)
        val currentChapterList = playChapterList.value
        if (currentChapterList != null && currentChapterList.isNotEmpty()) {
            tempChapterList.addAll(currentChapterList)
        }
        playChapterList.value = tempChapterList
    }

    fun initPlayChapter(chapter: DownloadChapter) {
        playChapter.set(chapter)
        process.set(chapter.listen_duration.toFloat())
        playChapterId.set(chapter.chapter_id.toString())
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
                    chapter.listen_duration = chapter.realDuration
                } else {
                    chapter.listen_duration = currentDuration
                }
                process.set(chapter.listen_duration.toFloat())
                if (chapter.listen_duration > chapter.realDuration) {
                    updateThumbText.set(
                        "${TimeUtils.getPlayDuration(chapter.realDuration)}/${
                        TimeUtils.getPlayDuration(chapter.realDuration)
                        }"
                    )
                } else {
                    updateThumbText.set(
                        "${TimeUtils.getPlayDuration(chapter.listen_duration)}/${
                        TimeUtils.getPlayDuration(chapter.realDuration)
                        }"
                    )
                }
                playChapter.set(chapter)
                playChapterId.set(chapter.chapter_id.toString())
                chapter.updateMillis = System.currentTimeMillis()
                chapter.duration = totalDuration
                playChapterDao.saveOrUpdate(BusinessConvert.convertToListenChapter(chapter))
                if (DownLoadFileUtils.checkChapterDownFinish(chapter)) {
                    if(TextUtils.isEmpty(chapter.file_path)){
                        chapter.file_path = DownLoadFileUtils.getPlayChapterFilePath(chapter)
                    }
                    playDownloadDao.saveOrUpdate(chapter)
                }
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

    fun savePlayChapter(position: Int,isAd : Boolean) {
        val playChapterList = playChapterList.value
        if (playChapterList != null && playChapterList.size > 0) {
            if (position <= playChapterList.size - 1) {
                val startChapter = playChapterList[position]
                playChapter.set(startChapter)
                playChapterId.set(startChapter.chapter_id.toString())

                if (playNeedQueryChapterProgress.get() ) {
                    DLog.d("suolong_PlayGlobalData", "需要重新查询进度 name = ${startChapter.chapter_name}")
                    playAudioId.get()?.let { audioId ->
                        playChapterId.get()?.let { chapterId ->
                            val listenChapter = ListenDaoUtils.queryChapterRecentUpdate(audioId.toLongSafe(), chapterId.toLongSafe())
                            if (listenChapter != null) {
                                if (listenChapter.listen_duration < startChapter.realDuration - 500L) {
                                    startChapter.listen_duration = listenChapter.listen_duration
                                }else{
                                    startChapter.listen_duration = 0L
                                }
                                playLastPlayProcess.set(listenChapter.listen_duration)
                            } else {
                                startChapter.listen_duration = 0L
                                playLastPlayProcess.set(-1L)
                            }
                        }
                    }
                } else {
                    DLog.d("PlayGlobalData", "不需要重新查询进度 name = ${startChapter.chapter_name}")
                    startChapter.listen_duration = 0L
                    playLastPlayProcess.set(-1L)
                }
                if(isAd){
                    process.set(0f)
                }else{
                    process.set(startChapter.listen_duration.toFloat())
                }
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

    fun setPlayHasNextAndPre(playList: List<*>?, position: Int) {
        if (playList != null && playList.isNotEmpty()) {
            val size = playList.size
            if (position == 0) {
                hasPreChapter.set(false)
            } else {
                hasPreChapter.set(true)
            }
            if (position == size - 1) {
                hasNextChapter.set(false)
            } else {
                hasNextChapter.set(true)
            }
        } else {
            hasNextChapter.set(false)
            hasNextChapter.set(false)
        }
    }

    fun checkCountChapterPlayEnd(hasNext : Boolean) : Boolean{
        if (playCountDownChapterSize.get() > 0) {
            if(!hasNext ){
                playCountDownChapterSize.set(0)
                playCountDownChapterSize.set(-1)
                playCountSelectPosition.set(-1)
                return true
            }
            playCountDownChapterSize.set(playCountDownChapterSize.get()-1)
            if(playCountDownChapterSize.get() == 0){
                playCountDownChapterSize.set(-1)
                playCountSelectPosition.set(-1)
                return true
            }
        }
        return false
    }

    fun setCountDownTimer(position: Int) {
        playCountSelectPosition.set(position)
        if (playCountTimerList[position] in 1..5) {
            playCountDownChapterSize.set(playCountTimerList[position])
            playCountDownSecond.set(-1000L)
        } else {
            playCountDownChapterSize.set(-1)
            playCountDownSecond.set(playCountTimerList[position] * 1000L)
            playTimerHandler.removeMessages(0)
            playTimerHandler.sendEmptyMessageDelayed(0, 1000)
        }
    }

    fun clearCountDownTimer() {
        playCountSelectPosition.set(-1)
        playCountDownChapterSize.set(-5)
        playCountDownSecond.set(-1000L)
    }


    fun isSortAsc(): Boolean {
        return playChapterListSort.get() == AudioSortType.SORT_ASC
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
