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
import com.rm.baselisten.adapter.single.CommonBindVMAdapter
import com.rm.baselisten.mvvm.BaseActivity
import com.rm.baselisten.net.checkResult
import com.rm.baselisten.util.DLog
import com.rm.baselisten.util.getBooleanMMKV
import com.rm.baselisten.util.putMMKV
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.business_lib.IS_FIRST_SUBSCRIBE
import com.rm.business_lib.PlayGlobalData
import com.rm.business_lib.base.dialog.CustomTipsFragmentDialog
import com.rm.business_lib.db.download.DownloadAudio
import com.rm.business_lib.db.download.DownloadChapter
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
import com.rm.music_exoplayer_lib.manager.MusicPlayerManager
import com.rm.music_exoplayer_lib.manager.MusicPlayerManager.Companion.musicPlayerManger
import com.rm.music_exoplayer_lib.utils.ExoplayerLogger
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

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
    val playAudioId = ObservableField<String>()

    /**
     * 播放的书籍详情
     */
    val playAudioModel = ObservableField<DownloadAudio>()
    /**
     * 当前播放的列表
     */
    val playPath = MutableLiveData<List<BaseAudioInfo>>()
    val playChapterList = MutableLiveData<MutableList<DownloadChapter>>()


    val process = ObservableFloat(0f)//进度条
    val maxProcess = ObservableFloat(0f)//最大进度
    val updateThumbText = ObservableField<String>()//更改文字
    var playControlAction = ObservableField<String>()
    val playManger: MusicPlayerManager = musicPlayerManger
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


    fun setPlayPath(chapterList: List<DownloadChapter>) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
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
                playPath.postValue(tempList)
            }
        }

    }


    /**
     * 初始化数据
     */
    fun initPlayerAdapterModel() {
    }

    //播放器操作行为
    fun playControlAction(action: String) {
        playControlAction.set(action)
    }

    fun finshActivity(action: String) {
        playControlAction.set(action)
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
     * 章节列表
     */
    fun chapterList(audioId: String, page: Int, page_size: Int, sort: String) {
        launchOnUI {
            repository.chapterList(audioId, page, page_size, sort).checkResult(onSuccess = {
                it.list?.let { list -> setPlayPath(list) }
                showContentView()
            }, onError = {
                showContentView()
            })
        }
    }

    /**
     * 章节列表
     */
    fun chapterPageList(
        audioId: String,
        chapterId: String,
        sort: String
    ) {
        launchOnUI {
            repository.chapterPageList(audioId, chapterId, sort).checkResult(onSuccess = {
                showContentView()
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
                    it.list.apply {
                        playAudioModel.set(this)
                        isAttention.set(anchor.status)
                        isSubscribe.set(is_subscribe)
                    }

                    if (TextUtils.isEmpty(it.list.audio_cover_url)) {
                        it.list.audio_cover_url = ""
                    }

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
        commentRefreshModel.setHasMore(bean.list.size >= pageSize)
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
            commentPage--
        }
    }

    /**
     * 评论加载更多
     */
    fun commentLoadData() {
        commentPage++
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

    /**
     * 评论列表点赞点击事件
     */
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