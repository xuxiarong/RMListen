package com.rm.module_play.viewmodel

import android.content.Context
import android.text.TextUtils
import android.widget.ImageView
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableFloat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.rm.baselisten.BaseConstance
import com.rm.baselisten.adapter.single.CommonBindVMAdapter
import com.rm.baselisten.ktx.addAll
import com.rm.baselisten.mvvm.BaseActivity
import com.rm.baselisten.net.checkResult
import com.rm.baselisten.util.DLog
import com.rm.baselisten.util.getBooleanMMKV
import com.rm.baselisten.util.putMMKV
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.business_lib.AudioSortType
import com.rm.business_lib.IS_FIRST_SUBSCRIBE
import com.rm.business_lib.PlayGlobalData
import com.rm.business_lib.base.dialog.CustomTipsFragmentDialog
import com.rm.business_lib.db.DaoUtil
import com.rm.business_lib.db.converter.BusinessConvert
import com.rm.business_lib.db.download.DownloadAudio
import com.rm.business_lib.db.download.DownloadChapter
import com.rm.business_lib.db.listen.ListenAudioEntity
import com.rm.business_lib.db.listen.ListenChapterEntity
import com.rm.business_lib.isLogin
import com.rm.business_lib.play.PlayState
import com.rm.business_lib.utils.mmSS
import com.rm.business_lib.utils.time2format
import com.rm.business_lib.wedgit.smartrefresh.model.SmartRefreshLayoutStatusModel
import com.rm.component_comm.home.HomeService
import com.rm.component_comm.listen.ListenService
import com.rm.component_comm.login.LoginService
import com.rm.component_comm.mine.MineService
import com.rm.component_comm.router.RouterHelper
import com.rm.module_play.BR
import com.rm.module_play.R
import com.rm.module_play.model.AudioCommentsModel
import com.rm.module_play.model.Comments
import com.rm.module_play.repository.BookPlayRepository
import com.rm.music_exoplayer_lib.bean.BaseAudioInfo
import com.rm.music_exoplayer_lib.ext.formatTimeInMillisToString
import com.rm.music_exoplayer_lib.manager.MusicPlayerManager
import com.rm.music_exoplayer_lib.manager.MusicPlayerManager.Companion.musicPlayerManger
import com.rm.music_exoplayer_lib.utils.ExoplayerLogger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 *
 * @des:
 * @data: 8/24/20 10:44 AM
 * @Version: 1.0.0
 */
open class PlayViewModel(private val repository: BookPlayRepository) : BaseVMViewModel() {
    /**
     * 当前音频id
     */
    val playAudioId = ObservableField<String>("")

    /**
     * 播放的书籍详情
     */
    val playAudioModel = ObservableField<DownloadAudio>()

    /**
     * 当前播放的列表
     */
    val playPath = MutableLiveData<List<BaseAudioInfo>>()

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
     * 章节的当前分页
     */
    var playChapterPage = PlayGlobalData.PLAY_FIRST_PAGE

    /**
     * 章节每页数量
     */
    var playChapterPageSize = PlayGlobalData.PLAY_PAGE_SIZE

    /**
     * 是否还有更多章节列表
     */
    var playHasMoreChapter = true

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
     * 绘制进度条的时间文字
     */
    val updateThumbText = ObservableField<String>("00:00/00:00")
    var playControlAction = ObservableField<String>()

    /**
     * 播放器实例对象
     */
    val playManger: MusicPlayerManager = musicPlayerManger

    /**
     * 书籍播放的数据库对象
     */
    private val playAudioDao = DaoUtil(ListenAudioEntity::class.java, "")

    /**
     * 章节播放的数据库对象
     */
    private val playChapterDao = DaoUtil(ListenChapterEntity::class.java, "")

    /**
     * 播放状态进度条，0是播放2是加载中1是暂停,false是暂停
     */
    var playStatusBean = ObservableField<PlayState>(
        PlayState()
    )

    /**
     * 是否有上一章
     */
    var hasPreChapter = ObservableBoolean(false)

    /**
     * 是否有下一章
     */
    var hasNextChapter = ObservableBoolean(false)

    /**
     * 拖动进度条时显示的文案
     */
    var seekText = ObservableField<String>("")

    /**
     * 拖动进度条时回调的方法
     */
    var seekChangeVar: (String) -> Unit = { seekTextChange(it) }

    /**
     * 播放速度
     */
    var playSpeed = PlayGlobalData.playSpeed

    val curTime = System.currentTimeMillis()

    init {
        updateThumbText.set("0/0")
    }

    /**
     * 章节当前的页码
     */
    private var commentPage = 1

    /**
     * 每次加载数据的条数
     */
    private val pageSize = 12

    /**
     * 评论数量
     */
    var commentTotal = ObservableField(0)

    /**
     * 关注按钮是否显示
     */
    val attentionVisibility = ObservableField<Boolean>(true)

    /**
     * 主播是否关注
     */
    var isAttention = ObservableBoolean(false)

    /**
     * 是否订阅过
     */
    var isSubscribe = ObservableBoolean(false)

    /**
     * 评论 SmartRefreshLayout的状态变化
     */
    val commentRefreshModel = SmartRefreshLayoutStatusModel()

    /**
     * 评论adapter
     */
    val mCommentAdapter by lazy {
        CommonBindVMAdapter<Comments>(
            this,
            mutableListOf(),
            R.layout.play_item_comment,
            BR.commentViewModel,
            BR.commentItem
        )
    }


    companion object {
        const val ACTION_PLAY_QUEUE = "ACTION_PLAY_QUEUE"//播放列表
        const val ACTION_PLAY_OPERATING = "ACTION_PLAY_OPERATING"//播放操作
        const val ACTION_GET_PLAYINFO_LIST = "ACTION_GET_PLAYINFO_LIST"//播放列表
        const val ACTION_JOIN_LISTEN = "ACTION_JOIN_LISTEN"//加入听单
        const val ACTION_MORE_COMMENT = "ACTION_MORE_COMMENT"//更多评论
        const val ACTION_MORE_FINSH = "ACTION_MORE_FINSH"//关闭
    }

    /**
     * 设置播放路径
     */
    fun setAudioPlayPath(chapterList: MutableList<DownloadChapter>) {
        val tempList = mutableListOf<BaseAudioInfo>()
        chapterList.forEach {
            tempList.add(
                BaseAudioInfo(
                    audioPath = it.path_url,
                    audioName = it.chapter_name,
                    filename = it.chapter_name,
                    audioId = it.audio_id.toString(),
                    chapterId = it.chapter_id.toString(),
                    duration = it.duration,
                    playCount = it.play_count.toString()
                )
            )
        }
        playChapterList.addAll(chapterList)
        playPath.postValue(tempList)

    }

    fun initPlayAudio(audio: DownloadAudio) {
        playAudioModel.set(audio)
        isAttention.set(audio.anchor.status)
        isSubscribe.set(audio.is_subscribe)
        BaseConstance.updateBaseAudioId(
            audioId = audio.audio_id.toString(),
            playUrl = audio.audio_cover_url
        )
        playAudioDao.saveOrUpdate(BusinessConvert.convertToListenAudio(audio))
    }

    fun initPlayChapter(chapter: DownloadChapter) {
        playChapter.set(chapter)
        maxProcess.set(chapter.duration.toFloat())
        process.set(chapter.listen_duration.toFloat())
        playChapterId.set(chapter.chapter_id.toString())
        playChapterDao.saveOrUpdate(BusinessConvert.convertToListenChapter(chapter))

        playChapterId.get()?.let {
            playReport(playAudioId.get()!!, it)
        }
    }

    fun updatePlayChapterProgress(
        currentDuration: Long = 0L,
        totalDuration: Long = 0L,
        isPlayFinish: Boolean = false
    ) {
        val chapter = playChapter.get()
        if (chapter != null) {
            maxProcess.set(totalDuration.toFloat())
            process.set(currentDuration.toFloat())
            updateThumbText.set(
                "${formatTimeInMillisToString(currentDuration)}/${
                    formatTimeInMillisToString(
                        totalDuration
                    )
                }"
            )
            chapter.listen_duration = if (isPlayFinish) {
                100
            } else {
                (currentDuration / 10 / chapter.duration).toInt()
            }
            playChapter.set(chapter)
            playChapterId.set(chapter.chapter_id.toString())
            playChapterDao.saveOrUpdate(BusinessConvert.convertToListenChapter(chapter))
            if (playAudioModel.get() != null) {
                playAudioDao.saveOrUpdate(BusinessConvert.convertToListenAudio(playAudioModel.get()!!))
            }
        }
    }

    fun savePlayChapter(baseAudioInfo: BaseAudioInfo, position: Int) {
        val playChapterList = playChapterList.value
        if (playChapterList != null && playChapterList.size > 0) {
            if (position <= playChapterList.size - 1) {
                val startChapter = playChapterList[position]
                playChapter.set(startChapter)
                playChapterId.set(startChapter.chapter_id.toString())
                maxProcess.set(startChapter.duration.toFloat())
                process.set(startChapter.listen_duration.toFloat())
                playChapterDao.saveOrUpdate(BusinessConvert.convertToListenChapter(playChapterList[position]))
            }
        }
    }


    //播放器操作行为
    fun playControlAction(action: String) {
        playControlAction.set(action)
    }

    fun finishActivity(action: String) {
        playControlAction.set(action)
    }

    //点赞
    fun playLikeBook(context: Context, bean: Comments) {
        if (isLogin.get()) {
            if (bean.is_liked) {
                unLikeComment(bean)
            } else {
                likeComment(bean)
            }
        } else {
            getActivity(context)?.let { quicklyLogin(it) }
        }
    }

    fun audioNameClick(context: Context) {
        val audioId = playAudioId.get()
        if (audioId != null) {
            RouterHelper.createRouter(HomeService::class.java).toDetailActivity(context, audioId)
            finish()
        }
    }

    /**
     * 快捷登陆
     */
    private fun quicklyLogin(it: FragmentActivity) {
        RouterHelper.createRouter(LoginService::class.java)
            .quicklyLogin(this, it, loginSuccess = {
                commentPage = 1
                getCommentList()
            })
    }

    /**
     * 上报
     */
    fun playReport(audioID: String, chapterId: String) {
        launchOnIO {
            repository.playerReport(audioID, chapterId).checkResult(onSuccess = {
                ExoplayerLogger.exoLog(it)
            }, onError = {
                ExoplayerLogger.exoLog(it ?: "")
            })
        }
    }

    /**
     * 通过音频ID直接获取下一页的章节列表，从第一页开始，这里成功后需要记录chapterId
     */
    fun getChapterList(
        audioId: String,
        page: Int = playChapterPage,
        page_size: Int = playChapterPageSize,
        sort: String = playChapterListSort.get()!!
    ) {
        if (playHasMoreChapter) {
            launchOnUI {
                repository.chapterList(audioId, page, page_size, sort).checkResult(onSuccess = {
                    val chapterList = it.list
                    if (chapterList != null && chapterList.size > 0) {
                        playHasMoreChapter = chapterList.size >= page_size
                        //是第一页，那么取第一条作为播放
                        if (page == PlayGlobalData.PLAY_FIRST_PAGE) {
                            initPlayChapter(chapterList[0])
                        }
                        playChapterPage++
                        setAudioPlayPath(chapterList)
                    } else {
                        playHasMoreChapter = false
                        setAudioPlayPath(mutableListOf())
                    }
                }, onError = {
                    it?.let {
                        showTip(it)
                    }
                })
            }
        }
    }

    /**
     * 通过音频ID，章节ID获取指定的章节列表，该接口会返回一个用于下次分页的page字段，需要记录
     */
    fun getChapterListWithId(
        audioId: String,
        chapterId: String,
        page: Int = playChapterPage,
        page_size: Int = playChapterPageSize,
        sort: String = playChapterListSort.get()!!
    ) {
        launchOnUI {
            repository.chapterPageList(
                audioId = audioId,
                chapterId = chapterId,
//                page = page,
//                page_size = page_size,
                sort = sort
            ).checkResult(onSuccess = {
                val chapterList = it.list
                playChapterPage = it.page
                showContentView()
                if (chapterList != null && chapterList.size > 0) {
                    playHasMoreChapter = chapterList.size >= page_size
                    chapterList.forEach { chapter ->
                        if (chapter.chapter_id.toString() == playChapterId.get()) {
                            initPlayChapter(chapter)
                            setAudioPlayPath(chapterList)
                            return@forEach
                        }
                    }
                } else {
                    playHasMoreChapter = false
                    setAudioPlayPath(mutableListOf())
                }
            }, onError = {
                showContentView()
            })
        }
    }

    /**
     * 获取书籍详情信息
     */
    fun getDetailInfo(audioID: String) {
        launchOnUI {
            repository.getDetailInfo(audioID).checkResult(
                onSuccess = {
                    initPlayAudio(it.list)
                }, onError = {
                    it?.let { it1 -> ExoplayerLogger.exoLog(it1) }
                }
            )
        }
    }

    /**
     * 取消评论点赞
     */
    private fun unLikeComment(bean: Comments) {
        launchOnIO {
            repository.homeUnLikeComment(bean.id).checkResult(
                onSuccess = {

                    val indexOf = mCommentAdapter.data.indexOf(bean)
                    bean.is_liked = false
                    bean.likes = bean.likes - 1
                    val headerLayoutCount = mCommentAdapter.headerLayoutCount
                    mCommentAdapter.notifyItemChanged(indexOf + headerLayoutCount)

                },
                onError = {
                    DLog.i("----->", "评论点赞:$it")
                }
            )
        }
    }

    /**
     * 评论点赞
     */
    private fun likeComment(bean: Comments) {
        launchOnIO {
            repository.homeLikeComment(bean.id).checkResult(
                onSuccess = {

                    val indexOf = mCommentAdapter.data.indexOf(bean)
                    bean.is_liked = true
                    bean.likes = bean.likes + 1
                    //记得加上头部的个数，不然会报错  https://github.com/CymChad/BaseRecyclerViewAdapterHelper/issues/871
                    val headerLayoutCount = mCommentAdapter.headerLayoutCount
                    mCommentAdapter.notifyItemChanged(indexOf + headerLayoutCount)
                },
                onError = {
                    DLog.i("----->", "评论点赞:$it")
                })
        }
    }


    /**
     * 订阅
     */
    private fun subscribe(context: Context, audioId: String) {
        launchOnIO {
            repository.subscribe(audioId).checkResult(
                onSuccess = {
                    isSubscribe.set(true)
                    subscribeSuccess(context)
                },
                onError = {
                    DLog.i("------->", "订阅失败  $it")
                    showTip("$it", R.color.business_color_ff5e5e)
                }
            )

        }
    }

    /**
     * 取消订阅
     */
    private fun unSubscribe(audioId: String) {
        launchOnIO {
            repository.unSubscribe(audioId).checkResult(
                onSuccess = {
                    isSubscribe.set(false)
                    showTip("取消订阅成功")
                },
                onError = {
                    DLog.i("------->", "取消订阅  $it")
                    showTip("$it", R.color.business_color_ff5e5e)
                }
            )
        }
    }


    /**
     *评论列表
     */
    fun getCommentList() {
        launchOnIO {
            repository.commentAudioComments(playAudioId.get()!!, commentPage, pageSize)
                .checkResult(onSuccess = {
                    processCommentSuccessData(it)
                }, onError = {
                    processCommentFailureData(it)
                })
        }
    }

    /**
     * 处理评论列表成功数据
     */
    private fun processCommentSuccessData(bean: AudioCommentsModel) {
        showContentView()
        commentTotal.set(bean.total)
        if (commentPage == 1) {
            mCommentAdapter.setList(bean.list)
            commentRefreshModel.finishRefresh(true)
        } else {
            mCommentAdapter.addData(bean.list)
            commentRefreshModel.finishLoadMore(true)
        }

        if (mCommentAdapter.data.size >= bean.total || bean.list.size < pageSize) {
            commentRefreshModel.setNoHasMore(true)
        } else {
            commentPage++
        }
    }

    /**
     * 处理评论列表失败数据
     */
    private fun processCommentFailureData(it: String?) {
        if (commentPage == 1) {
            commentRefreshModel.finishRefresh(false)
            showTip("$it")
        } else {
            commentRefreshModel.finishLoadMore(false)
        }
    }

    /**
     * 评论加载更多
     */
    fun commentLoadData() {
        getCommentList()
    }

//    /**
//     * 记录播放的章节
//     */
//    fun updatePlayBook(chapter: ChapterList?) {
//        chapter?.let {
//            repository.updatePlayBook(it)
//
//        }
//    }
//
//    /**
//     * 更新播放进度
//     */
//    fun updatePlayBookProcess(chapter: ChapterList?, progress: Long = 0L) {
//        chapter?.let {
//            repository.updatePlayBookProcess(chapter, progress)
//        }
//    }


    /**
     * 评论点击事件
     */
    fun clickCommentFun(context: Context) {
        getActivity(context)?.let {
            if (isLogin.get()) {
                playAudioId.get()?.let { audioId ->
                    RouterHelper.createRouter(HomeService::class.java)
                        .showCommentDialog(this, it, audioId) {
                            if (it is BaseActivity) {
                                it.tipView.showTipView(it, "评论成功")
                            }
                            commentPage = 1
                            getCommentList()
                        }
                }

            } else {
                quicklyLogin(it)
            }
        }
    }

//    /**
//     * 查询
//     */
//    fun queryPlayBookList(): List<HistoryPlayBook>? =
//        DaoUtil(HistoryPlayBook::class.java, "").queryAll()


    fun playPreClick() {
        if (hasPreChapter.get()) {
            playManger.playLastMusic()
        }
    }

    fun playNextClick() {
        if (hasNextChapter.get()) {
            playManger.playNextMusic()
        }
    }

    fun seekTextChange(changeText: String) {
        this.seekText.set(changeText)
    }


    val countdownTime = ObservableField<String>()

    //倒计时
    private var countdownScope: Job? = null
    fun countdown() {
        if (countdownScope?.isActive == true) {
            countdownScope?.cancel()
        }
        val times = (musicPlayerManger.getPlayerAlarmTime() - System.currentTimeMillis()) / 1000
        if (times > 0) {
            countdownScope = viewModelScope.launch {
                flow {
                    (times downTo 0).forEach { it ->
                        delay(1000)
                        emit(it)
                    }
                }.flowOn(Dispatchers.Default).onStart {
                    // 倒计时开始
                }.onCompletion {
                    // 倒计时结束
                    if (playManger.getRemainingSetInt() <= 0) {
                        countdownTime.set("")
                    }
                }.collect {
                    countdownTime.set(mmSS.time2format(it * 1000))
                }
            }
        }


    }

    /**
     * 评论头像点击事件
     */
    fun commentAvatarClick(context: Context, member_id: String) {
        RouterHelper.createRouter(MineService::class.java)
            .toMineCommentFragment(context = context, memberId = member_id)
    }

    /**
     * 关注点击事件
     */
    fun clickAttentionFun(context: Context, followId: String?) {
        getActivity(context)?.let {
            if (!isLogin.get()) {
                quicklyLogin(it)
            } else
                if (!TextUtils.isEmpty(followId)) {
                    if (isAttention.get()) {
                        unAttentionAnchor(followId!!)
                    } else {
                        attentionAnchor(followId!!)
                    }
                }
        }
    }

    /**
     * 关注主播
     */
    private fun attentionAnchor(followId: String) {
        showLoading()
        launchOnIO {
            repository.attentionAnchor(followId).checkResult(
                onSuccess = {
                    showContentView()
                    isAttention.set(true)
                    showTip("关注成功")
                },
                onError = {
                    showContentView()
                    showTip("$it", R.color.business_color_ff5e5e)
                })
        }
    }

    /**
     * 取消关注主播
     */
    private fun unAttentionAnchor(followId: String) {
        showLoading()
        launchOnIO {
            repository.unAttentionAnchor(followId).checkResult(
                onSuccess = {
                    showContentView()
                    isAttention.set(false)
                    showTip("取消关注成功")
                },
                onError = {
                    showContentView()
                    showTip("$it", R.color.business_color_ff5e5e)
                })
        }
    }

    /**
     * 主播头像点击事件
     */
    fun clickMemberFun(context: Context) {
        if (isLogin.get()) {
            playAudioModel.get()?.let {
                RouterHelper.createRouter(MineService::class.java)
                    .toMineMember(context, it.anchor_id)
            }
        } else {
            getActivity(context)?.let { quicklyLogin(it) }
        }
    }

    /**
     * 订阅点击事件
     */
    fun clickPlaySubAction(context: Context) {
        getActivity(context)?.let {
            if (!isLogin.get()) {
                quicklyLogin(it)
            } else {
                if (isSubscribe.get()) {
                    playAudioId.get()?.let { audioId -> unSubscribe(audioId) }
                } else {
                    playAudioId.get()?.let { audioId -> subscribe(context, audioId) }
                }
            }
        }
    }

//    /**
//     * 评论列表点赞点击事件
//     */
//    fun playLikeBook(context: Context, bean: Comments) {
//        if (isLogin.get()) {
//            if (bean.is_liked) {
//                unLikeComment(bean)
//            } else {
//                likeComment(bean)
//            }
//        } else {
//            getActivity(context)?.let { quicklyLogin(it) }
//        }
//    }


    /**
     * 订阅成功
     */
    private fun subscribeSuccess(context: Context) {
        val activity = getActivity(context)
        if (IS_FIRST_SUBSCRIBE.getBooleanMMKV(true) && activity != null) {
            CustomTipsFragmentDialog().apply {
                titleText = context.getString(R.string.business_favorites_success)
                contentText = context.getString(R.string.business_favorites_success_content)
                leftBtnText = context.getString(R.string.business_know)
                rightBtnText = context.getString(R.string.business_goto_look)
                leftBtnTextColor = R.color.business_text_color_333333
                rightBtnTextColor = R.color.business_color_ff5e5e
                leftBtnClick = {
                    dismiss()
                }
                rightBtnClick = {
                    RouterHelper.createRouter(ListenService::class.java).startSubscription(activity)
                    dismiss()
                }
                customView =
                    ImageView(activity).apply { setImageResource(R.mipmap.business_img_dycg) }
            }.show(activity)
        } else {
            showTip(context.getString(R.string.business_subscribe_success_tip))
        }
        IS_FIRST_SUBSCRIBE.putMMKV(false)
    }

}