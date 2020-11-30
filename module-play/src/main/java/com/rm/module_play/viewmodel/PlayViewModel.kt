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
import com.rm.baselisten.net.checkResult
import com.rm.baselisten.util.DLog
import com.rm.baselisten.util.TimeUtils
import com.rm.baselisten.util.getBooleanMMKV
import com.rm.baselisten.util.putMMKV
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.business_lib.*
import com.rm.business_lib.base.dialog.CustomTipsFragmentDialog
import com.rm.business_lib.db.DaoUtil
import com.rm.business_lib.db.converter.BusinessConvert
import com.rm.business_lib.db.download.DownloadAudio
import com.rm.business_lib.db.download.DownloadChapter
import com.rm.business_lib.db.listen.ListenAudioEntity
import com.rm.business_lib.db.listen.ListenChapterEntity
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
import com.rm.module_play.dialog.showMusicPlayMoreDialog
import com.rm.module_play.dialog.showMusicPlaySpeedDialog
import com.rm.module_play.dialog.showMusicPlayTimeSettingDialog
import com.rm.module_play.dialog.showPlayBookListDialog
import com.rm.module_play.model.AudioCommentsModel
import com.rm.module_play.model.Comments
import com.rm.module_play.repository.BookPlayRepository
import com.rm.music_exoplayer_lib.bean.BaseAudioInfo
import com.rm.music_exoplayer_lib.manager.MusicPlayerManager
import com.rm.music_exoplayer_lib.manager.MusicPlayerManager.Companion.musicPlayerManger
import com.rm.music_exoplayer_lib.utils.ExoplayerLogger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.lang.Exception

/**
 *
 * @des:
 * @data: 8/24/20 10:44 AM
 * @Version: 1.0.0
 */
open class PlayViewModel(private val repository: BookPlayRepository) : BaseVMViewModel() {
    
    /**
     * 当前播放的列表
     */
    val playPath = MutableLiveData<List<BaseAudioInfo>>()

    /**
     * 加载下一页的当前页码
     */
    var playNextPage = PlayGlobalData.PLAY_FIRST_PAGE
    
    /**
     * 加载上一页的当前页码
     */
    var playPrePage = PlayGlobalData.PLAY_FIRST_PAGE

    /**
     * 章节每页数量
     */
    var playChapterPageSize = PlayGlobalData.PLAY_PAGE_SIZE

    /**
     * 播放器实例对象
     */
    val playManger: MusicPlayerManager = musicPlayerManger
    
    /**
     * 拖动进度条时显示的文案
     */
    var seekText = ObservableField<String>("")

    /**
     * 拖动进度条时回调的方法
     */
    var seekChangeVar: (String) -> Unit = { seekTextChange(it) }

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
                BR.viewModel,
                BR.item
        )
    }

    /**
     * 章节列表的Adapter
     */
    val chapterListAdapter by lazy {
        CommonBindVMAdapter<DownloadChapter>(
                this,
                mutableListOf(),
                R.layout.play_dialog_item_chapter,
                BR.viewModel,
                BR.item
        )
    }

    /**
     * 评论 SmartRefreshLayout的状态变化
     */
    val chapterRefreshModel = SmartRefreshLayoutStatusModel()


    /**
     * 设置播放路径
     */
    private fun setAudioPlayPath(chapterList: MutableList<DownloadChapter>) {
        val tempList = mutableListOf<BaseAudioInfo>()
        chapterList.forEach {
            tempList.add(
                    BaseAudioInfo(
                            audioPath = it.path_url,
                            audioName = it.chapter_name,
                            filename = it.chapter_name,
                            audioId = it.audio_id.toString(),
                            chapterId = it.chapter_id.toString(),
                            duration = it.realDuration,
                            playCount = it.play_count.toString()
                    )
            )
        }
        PlayGlobalData.playChapterList.addAll(chapterList)
        chapterListAdapter.setList(PlayGlobalData.playChapterList.value)
        playPath.postValue(tempList)
    }

    fun insertPlayPath(chapterList: MutableList<DownloadChapter>) {
        val tempChapterList = mutableListOf<DownloadChapter>()
        val tempPathList = mutableListOf<BaseAudioInfo>()
        tempChapterList.addAll(chapterList)
        chapterList.forEach {
            tempPathList.add(
                    BaseAudioInfo(
                            audioPath = it.path_url,
                            audioName = it.chapter_name,
                            filename = it.chapter_name,
                            audioId = it.audio_id.toString(),
                            chapterId = it.chapter_id.toString(),
                            duration = it.realDuration,
                            playCount = it.play_count.toString()
                    )
            )
        }
        val currentPathList = playPath.value
        if (currentPathList != null && currentPathList.isNotEmpty()) {
            tempPathList.addAll(currentPathList)
        }
        val currentChapterList = PlayGlobalData.playChapterList.value
        if (currentChapterList != null && currentChapterList.isNotEmpty()) {
            tempChapterList.addAll(currentChapterList)
        }
        PlayGlobalData.playChapterList.postValue(tempChapterList)
        chapterListAdapter.setList(tempChapterList)
        playPath.postValue(tempPathList)
    }

    fun initPlayChapter(chapter: DownloadChapter) {
        PlayGlobalData.initPlayChapter(chapter)
        PlayGlobalData.playChapterId.get()?.let {
            playReport(PlayGlobalData.playAudioId.get()!!, it)
        }
    }

    fun showChapterListDialog(context: Context) {
        if (context is FragmentActivity) {
            context.showPlayBookListDialog(this)
        }
    }

    fun showPlaySettingDialog(context: Context) {
        if (context is FragmentActivity) {
            context.showMusicPlayMoreDialog(this)
        }
    }

    fun showPlayTimerDialog(context: Context) {
        if (context is FragmentActivity) {
            context.showMusicPlayTimeSettingDialog(this)
        }
    }

    fun showPlaySpeedDialog(context: Context) {
        if (context is FragmentActivity) {
            context.showMusicPlaySpeedDialog(this)
        }
    }

    fun finishActivity(context: Context) {
        if(context is FragmentActivity){
            finish()
        }
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
        val audioId = PlayGlobalData.playAudioId.get()
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
    fun getNextPageChapterList() {
        launchOnIO {
            repository.chapterList(
                    PlayGlobalData.playAudioId.get()!!,
                    playNextPage,
                    playChapterPageSize,
                    PlayGlobalData.playChapterListSort.get()!!
            ).checkResult(onSuccess = {
                val chapterList = it.list
                if (chapterList != null && chapterList.size > 0) {
                    chapterRefreshModel.noMoreData.set(chapterList.size < playChapterPageSize)
                    chapterRefreshModel.finishLoadMore(true)
                    //是第一页，那么取第一条作为播放
                    if (playNextPage == PlayGlobalData.PLAY_FIRST_PAGE) {
                        initPlayChapter(chapterList[0])
                        chapterRefreshModel.canRefresh.set(false)
                        PlayGlobalData.playChapterList.value?.clear()
                    }
                    playNextPage++
                    setAudioPlayPath(chapterList)
                } else {
                    chapterRefreshModel.noMoreData.set(false)
                    chapterRefreshModel.finishLoadMore(false)
                    setAudioPlayPath(mutableListOf())
                }
            }, onError = {
                it?.let {
                    chapterRefreshModel.finishLoadMore(false)
                    showTip(it)
                }
            })
        }

    }

    /**
     * 通过音频ID直接获取上一页的章节列表
     */
    fun getPrePageChapterList() {
        val audioId = PlayGlobalData.playAudioId.get()
        if (TextUtils.isEmpty(audioId)) {
            return
        }
        if (playPrePage <= PlayGlobalData.PLAY_FIRST_PAGE) {
            return
        }
        playPrePage--
        launchOnIO {
            repository.chapterList(
                    audioId!!,
                    playPrePage,
                    playChapterPageSize,
                    PlayGlobalData.playChapterListSort.get()!!
            ).checkResult(onSuccess = {
                val chapterList = it.list
                if (chapterList != null && chapterList.size > 0) {
                    insertPlayPath(chapterList)
                    chapterRefreshModel.canRefresh.set(playPrePage > 1)
                }
                chapterRefreshModel.finishRefresh(true)
            }, onError = {
                playPrePage++
                chapterRefreshModel.finishRefresh(false)
                it?.let {
                    showTip(it)
                }
            })
        }
    }

    /**
     * 通过音频ID，章节ID获取指定的章节列表，该接口会返回一个用于下次分页的page字段，需要记录
     */
    fun getChapterListWithId(
            audioId: String,
            chapterId: String
    ) {
        launchOnIO {
            repository.chapterPageList(
                    audioId = audioId,
                    chapterId = chapterId,
                    page_size = playChapterPageSize,
                    sort = PlayGlobalData.playChapterListSort.get()!!
            ).checkResult(onSuccess = {
                val chapterList = it.list
                playNextPage = it.page
                playPrePage = it.page
                showContentView()
                if (chapterList != null && chapterList.size > 0) {
                    chapterRefreshModel.noMoreData.set(chapterList.size < playChapterPageSize)
                    chapterList.forEach { chapter ->
                        if (chapter.chapter_id.toString() == PlayGlobalData.playChapterId.get()) {
                            initPlayChapter(chapter)
                            setAudioPlayPath(chapterList)
                            return@forEach
                        }
                    }
                    playNextPage++
                } else {
                    setAudioPlayPath(mutableListOf())
                }
            }, onError = {
                chapterRefreshModel.finishLoadMore(false)
                showContentView()
                it?.let {
                    showTip(it)
                }
            })
        }
    }

    /**
     * 获取书籍详情信息
     */
    fun getDetailInfo(audioID: String) {
        launchOnIO {
            repository.getDetailInfo(audioID).checkResult(
                    onSuccess = {
                        PlayGlobalData.initPlayAudio(it.list)
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
            repository.commentAudioComments(PlayGlobalData.playAudioId.get()!!, commentPage, pageSize)
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

    /**
     * 评论点击事件
     */
    fun clickCommentFun(context: Context) {
        getActivity(context)?.let {
            if (isLogin.get()) {
                PlayGlobalData.playAudioId.get()?.let { audioId ->
                    RouterHelper.createRouter(HomeService::class.java)
                            .showCommentDialog(it, audioId) {
                                showTip("评论成功")
                                commentPage = 1
                                getCommentList()
                            }
                }

            } else {
                quicklyLogin(it)
            }
        }
    }

    fun playPreClick() {
        if (PlayGlobalData.hasPreChapter.get()) {
            playManger.playLastMusic()
        }
    }

    fun playNextClick() {
        if (PlayGlobalData.hasNextChapter.get()) {
            playManger.playNextMusic()
        }
    }

    fun seekTextChange(changeText: String) {
        this.seekText.set(changeText)
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
            PlayGlobalData.playAudioModel.get()?.let {
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
                    PlayGlobalData.playAudioId.get()?.let { audioId -> unSubscribe(audioId) }
                } else {
                    PlayGlobalData.playAudioId.get()?.let { audioId -> subscribe(context, audioId) }
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
     * 加入听单
     */
    fun joinListenList(context: Context) {
        if (context is FragmentActivity) {
            if (!isLogin.get()) {
                RouterHelper.createRouter(LoginService::class.java)
                        .quicklyLogin(this, context)
            } else {
                RouterHelper.createRouter(ListenService::class.java).showMySheetListDialog(
                        context,
                        PlayGlobalData.playAudioId.get()!!
                ) {
                    showTip("添加成功")
                }
            }
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