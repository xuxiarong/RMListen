package com.rm.module_play.viewmodel

import android.content.Context
import android.text.TextUtils
import android.widget.ImageView
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.fragment.app.FragmentActivity
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.rm.baselisten.BaseApplication
import com.rm.baselisten.BaseConstance
import com.rm.baselisten.adapter.single.CommonBindVMAdapter
import com.rm.baselisten.net.checkResult
import com.rm.baselisten.util.DLog
import com.rm.baselisten.util.getBooleanMMKV
import com.rm.baselisten.util.putMMKV
import com.rm.baselisten.utilExt.String
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.business_lib.*
import com.rm.business_lib.PlayGlobalData.chapterRefreshModel
import com.rm.business_lib.base.dialog.CustomTipsFragmentDialog
import com.rm.baselisten.dialog.TipsFragmentDialog
import com.rm.business_lib.PlayGlobalData.playChapterTotal
import com.rm.business_lib.bean.AudioRecommend
import com.rm.business_lib.bean.BusinessAdModel
import com.rm.business_lib.db.download.DownloadChapter
import com.rm.business_lib.insertpoint.BusinessInsertConstance
import com.rm.business_lib.insertpoint.BusinessInsertManager
import com.rm.business_lib.wedgit.smartrefresh.model.SmartRefreshLayoutStatusModel
import com.rm.component_comm.home.HomeService
import com.rm.component_comm.listen.ListenService
import com.rm.component_comm.login.LoginService
import com.rm.component_comm.mine.MineService
import com.rm.component_comm.router.RouterHelper
import com.rm.component_comm.utils.BannerJumpUtils
import com.rm.module_play.BR
import com.rm.module_play.R
import com.rm.module_play.adapter.PlayDetailCommentAdapter
import com.rm.module_play.dialog.showMusicPlayMoreDialog
import com.rm.module_play.dialog.showMusicPlaySpeedDialog
import com.rm.module_play.dialog.showMusicPlayTimeSettingDialog
import com.rm.module_play.dialog.showPlayBookListDialog
import com.rm.module_play.model.AudioCommentsModel
import com.rm.module_play.repository.BookPlayRepository
import com.rm.music_exoplayer_lib.bean.BaseAudioInfo
import com.rm.music_exoplayer_lib.manager.MusicPlayerManager
import com.rm.music_exoplayer_lib.manager.MusicPlayerManager.Companion.musicPlayerManger
import com.rm.music_exoplayer_lib.utils.ExoplayerLogger
import kotlin.random.Random

/**
 *
 * @des:
 * @data: 8/24/20 10:44 AM
 * @Version: 1.0.0
 */
open class PlayViewModel(private val repository: BookPlayRepository) : BaseVMViewModel() {

    /**
     * 是否本地下载传过来的数据
     */
    var isLocalChapterList = ObservableBoolean(false)

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
     * 订阅数量
     */
    val subscriptionCount = ObservableField<String>()

    /**
     * 评论缺省
     */
    var commentEmptyVisible = ObservableField<Boolean>(false)

    /**
     * 评论 SmartRefreshLayout的状态变化
     */
    val commentRefreshModel = SmartRefreshLayoutStatusModel()
    val commentRvScrollY = ObservableField(0)

    val commentContentRvId = R.id.play_comment_rv

    private var lastCommentLikeTime = 0L
    private var lastCommentUnLikeTime = 0L

    /**
     * 评论adapter
     */
    val mCommentAdapter by lazy {
        PlayDetailCommentAdapter(this@PlayViewModel, BR.viewModel, BR.item)
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

    val audioRecommendAdapter by lazy {
        CommonBindVMAdapter<AudioRecommend>(
            this,
            mutableListOf(),
            R.layout.play_item_audio_recommend,
            BR.viewModel,
            BR.item
        )
    }

    var playFloorAd = ObservableField<BusinessAdModel>()


    private fun initPlayChapter(chapter: DownloadChapter) {
        PlayGlobalData.initPlayChapter(chapter)
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


    //点赞
    fun playLikeBook(context: Context, bean: PlayDetailCommentAdapter.PlayDetailCommentItemEntity) {
        if (isLogin.get()) {
            if (bean.data.is_liked) {
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
            RouterHelper.createRouter(HomeService::class.java).startDetailActivity(context, audioId)
            finish()
        }
    }

    /**
     * 关闭评论广告
     */
    fun clickDelCommentAd(bean: PlayDetailCommentAdapter.PlayDetailCommentAdvertiseItemEntity) {
        mCommentAdapter.remove(bean)
        BusinessInsertManager.doInsertKeyAndAd(
            BusinessInsertConstance.INSERT_TYPE_AD_CLOSE,
            bean.data.ad_id.toString()
        )
    }

    /**
     * 评论广告跳转
     */
    fun clickJumpAd(
        context: Context,
        bean: PlayDetailCommentAdapter.PlayDetailCommentAdvertiseItemEntity
    ) {
        BannerJumpUtils.onBannerClick(context, bean.data.jump_url, bean.data.ad_id.toString())
    }

    /**
     * 快捷登陆
     */
    private fun quicklyLogin(it: FragmentActivity) {
        RouterHelper.createRouter(LoginService::class.java)
            .quicklyLogin(it, loginSuccess = {
                commentPage = 1
                getCommentList()
            })
    }

    /**
     * 通过音频ID直接获取下一页的章节列表，从第一页开始，这里成功后需要记录chapterId
     */
    fun getNextPageChapterList() {
        val audioId = PlayGlobalData.playAudioId.get()
        if (TextUtils.isEmpty(audioId)) {
            return
        }
        var chapterId = 0L
        PlayGlobalData.playChapterList.value?.let {
            if (it.isNotEmpty()) {
                chapterId = it.last().chapter_id
            }
        }

        launchOnIO {
            repository.getNextPage(
                audioId = audioId!!,
                chapterId = chapterId.toString(),
                page = 1,
                page_size = PlayGlobalData.playChapterPageSize,
                sort = PlayGlobalData.playChapterListSort.get() ?: AudioSortType.SORT_ASC,
                direction = if (PlayGlobalData.isSortAsc()) {
                    "backward"
                } else {
                    "forward"
                }
            ).checkResult(onSuccess = {
                val chapterList = it.list
                if (chapterList != null && chapterList.size >= 0) {
                    chapterRefreshModel.noMoreData.set(chapterList.size < pageSize)
                    chapterRefreshModel.finishLoadMore(true)
                    if (chapterList.size > 0) {
                        PlayGlobalData.setNextPagePlayData(chapterList)
                    }
                } else {
                    chapterRefreshModel.noMoreData.set(true)
                    chapterRefreshModel.finishLoadMore(true)
                }
            }, onError = { it, _ ->
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
        var chapterId = 0L
        PlayGlobalData.playChapterList.value?.let {
            if (it.isNotEmpty()) {
                chapterId = it.first().chapter_id
            }
        }
        launchOnIO {
            repository.getPrePage(
                audioId = audioId!!,
                chapterId = chapterId.toString(),
                page = 1,
                page_size = PlayGlobalData.playChapterPageSize,
                sort = PlayGlobalData.playChapterListSort.get() ?: AudioSortType.SORT_ASC,
                direction = if (PlayGlobalData.isSortAsc()) {
                    "forward"
                } else {
                    "backward"
                }
            ).checkResult(onSuccess = {
                val chapterList = it.list
                if (chapterList != null && chapterList.size >= 0) {
                    chapterRefreshModel.canRefresh.set(chapterList.size >= pageSize)
                    if (chapterList.size > 0) {
                        PlayGlobalData.setPrePagePlayData(chapterList)
                    }
                }
                chapterRefreshModel.finishRefresh(true)
            }, onError = { it, _ ->
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

        playChapterTotal = 1
        chapterRefreshModel.noMoreData.set(false)
        chapterRefreshModel.canRefresh.set(true)
        launchOnIO {
            repository.getChapterListWithId(
                audioId = audioId,
                chapterId = chapterId,
                page_size = PlayGlobalData.playChapterPageSize,
                sort = PlayGlobalData.playChapterListSort.get() ?: AudioSortType.SORT_ASC

            ).checkResult(onSuccess = {
                val chapterList = it.list
                showContentView()
                if (chapterList != null) {
                    chapterList.forEach { chapter ->
                        if (chapter.chapter_id.toString() == BaseConstance.basePlayInfoModel.get()?.playChapterId) {
                            initPlayChapter(chapter)
                            PlayGlobalData.setNextPagePlayData(chapterList)
                            return@forEach
                        }
                    }
                } else {
                    PlayGlobalData.setNextPagePlayData(mutableListOf())
                }
            }, onError = { it, _ ->
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
    fun getAudioInfo(audioID: String) {
        launchOnIO {
            repository.getDetailInfo(audioID).checkResult(
                onSuccess = {
                    isSubscribe.set(it.list.is_subscribe)
                    subscriptionCount.set(it.list.subscription_count)
                    isAttention.set(it.list.anchor.status)
                    PlayGlobalData.initPlayAudio(it.list)
                    getAudioFloorAd()
                },
                onError = { it, _ ->
                    it?.let {
                        showToast(it, R.color.business_color_ff5e5e)
                    }
                }
            )
        }
    }

    fun getAudioRecommend(audioId: String) {
        if (audioId.isEmpty()) {
            return
        }
        launchOnIO {
            repository.getAudioRecommend(audioId.toLong()).checkResult(
                onSuccess = {
                    audioRecommendAdapter.setList(it.list)
                },
                onError = { it, _ ->

                }
            )
        }
    }

    /**
     * 获取章节的广告
     */
    fun getChapterAd(actionPlayAd: () -> Unit) {
        launchOnIO(block = {
            repository.getChapterAd().checkResult(
                onSuccess = {
                    val result = arrayListOf<BaseAudioInfo>()
                    if (it.ad_player_voice != null && it.ad_player_voice.isNotEmpty()) {
                        val position = Random.nextInt(it.ad_player_voice.size)
                        result.add(
                            BaseAudioInfo(
                                audioPath = it.ad_player_voice[position].audio_url,
                                isAd = true
                            )
                        )
                        PlayGlobalData.playAdIsPlaying.set(true)
                        PlayGlobalData.playVoiceAdClose.set(false)
                        PlayGlobalData.playVoiceImgAd.set(it.ad_player_voice[position])
                    } else {
                        PlayGlobalData.playAdIsPlaying.set(false)
                        PlayGlobalData.playVoiceAdClose.set(true)
                        PlayGlobalData.playVoiceImgAd.set(null)
                    }
                    musicPlayerManger.setAdPath(result)
                    actionPlayAd()
                    it.ad_player_audio_cover?.let { audioImgAdList ->
                        if (audioImgAdList.isNotEmpty()) {
                            PlayGlobalData.playAudioImgAd.set(
                                audioImgAdList[Random.nextInt(
                                    audioImgAdList.size
                                )]
                            )
                        }
                    }
                },
                onError = { it, _ ->
                    PlayGlobalData.playAdIsPlaying.set(false)
                    PlayGlobalData.playVoiceAdClose.set(true)
                    PlayGlobalData.playVoiceImgAd.set(null)
                    musicPlayerManger.setAdPath(arrayListOf())
                    actionPlayAd()
                    DLog.d("suolong", "error = ${it ?: ""}")
                })
        }, netErrorBlock = { actionPlayAd() }
        )
    }

    /**
     * 获取楼层广告
     */

    fun getAudioFloorAd() {
        launchOnIO {
            repository.getAudioFloorAd().checkResult(
                onSuccess = {
                    it.ad_player_streamer?.let { audioImgAdList ->
                        if (audioImgAdList.isNotEmpty()) {
                            playFloorAd.set(audioImgAdList[Random.nextInt(audioImgAdList.size)])
                        }
                    }
                    it.ad_player_audio_cover?.let { audioImgAdList ->
                        if (audioImgAdList.isNotEmpty()) {
                            PlayGlobalData.playAudioImgAd.set(
                                audioImgAdList[Random.nextInt(
                                    audioImgAdList.size
                                )]
                            )
                        }
                    }
                },
                onError = { it, _ ->
                })
        }
    }

    /**
     * 取消评论点赞
     */
    private fun unLikeComment(bean: PlayDetailCommentAdapter.PlayDetailCommentItemEntity) {
        if (System.currentTimeMillis() - lastCommentLikeTime < 1000) {
            return
        }
        launchOnIO {
            repository.homeUnLikeComment(bean.data.id).checkResult(
                onSuccess = {
                    lastCommentLikeTime = System.currentTimeMillis()
                    val indexOf = mCommentAdapter.data.indexOf(bean)
                    if (indexOf != -1) {
                        bean.data.is_liked = false
                        bean.data.likes = bean.data.likes - 1
                        val headerLayoutCount = mCommentAdapter.headerLayoutCount
                        mCommentAdapter.notifyItemChanged(indexOf + headerLayoutCount)
                    }

                },
                onError = { it, _ ->
                    lastCommentLikeTime = System.currentTimeMillis()
                    DLog.i("----->", "评论点赞:$it")
                }
            )
        }
    }

    /**
     * 评论点赞
     */
    private fun likeComment(bean: PlayDetailCommentAdapter.PlayDetailCommentItemEntity) {
        if (System.currentTimeMillis() - lastCommentUnLikeTime < 1000) {
            return
        }
        launchOnIO {
            repository.homeLikeComment(bean.data.id).checkResult(
                onSuccess = {
                    lastCommentUnLikeTime = System.currentTimeMillis()
                    val indexOf = mCommentAdapter.data.indexOf(bean)
                    if (indexOf != -1) {
                        bean.data.is_liked = true
                        bean.data.likes = bean.data.likes + 1
                        //记得加上头部的个数，不然会报错  https://github.com/CymChad/BaseRecyclerViewAdapterHelper/issues/871
                        val headerLayoutCount = mCommentAdapter.headerLayoutCount
                        mCommentAdapter.notifyItemChanged(indexOf + headerLayoutCount)
                    }
                },
                onError = { it, _ ->
                    lastCommentUnLikeTime = System.currentTimeMillis()
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
                    PlayGlobalData.playAudioModel.get()?.let {
                        it.is_subscribe = true
                        val count = "${it.subscription_count.toInt() + 1}"
                        it.subscription_count = count
                        subscriptionCount.set(count)
                        PlayGlobalData.playAudioModel.set(it)
                    }
                    subscribeSuccess(context)
                },
                onError = { it, _ ->
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
                    PlayGlobalData.playAudioModel.get()?.let {
                        it.is_subscribe = false
                        val count = "${it.subscription_count.toInt() - 1}"
                        it.subscription_count = count
                        subscriptionCount.set(count)
                        PlayGlobalData.playAudioModel.set(it)
                    }
                    isSubscribe.set(false)
                    showTip("取消订阅成功")
                },
                onError = { it, _ ->
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
            repository.commentAudioComments(
                PlayGlobalData.playAudioId.get()!!,
                commentPage,
                pageSize
            ).checkResult(onSuccess = {
                processCommentSuccessData(it)
                commentRvScrollY.set(-1)
                commentRvScrollY.notifyChange()

            }, onError = { it, _ ->
                processCommentFailureData(it)
            })
        }
    }

    /**
     * 处理评论列表成功数据
     */
    private fun processCommentSuccessData(bean: AudioCommentsModel) {
        showContentView()
        val list = mutableListOf<MultiItemEntity>()
        commentTotal.set(bean.total)
        if (commentPage == 1) {
            bean.list?.let {
                it.forEach { bean ->
                    list.add(PlayDetailCommentAdapter.PlayDetailCommentItemEntity(bean))
                }
            }
            if (list.size > 0) {
                mCommentAdapter.setList(list)
                if (list.size > 4) {
                    getAdInfo()
                }
                commentEmptyVisible.set(false)
            } else {
                commentEmptyVisible.set(true)
            }
            commentRefreshModel.finishRefresh(true)
        } else {
            bean.list?.let {
                it.forEach { bean ->
                    list.add(PlayDetailCommentAdapter.PlayDetailCommentItemEntity(bean))
                }
                mCommentAdapter.addData(list)
            }
            commentRefreshModel.finishLoadMore(true)
        }

        if (mCommentAdapter.data.size >= bean.total || bean.list?.size ?: 0 < pageSize) {
            if (mCommentAdapter.data.size > 0) {
                commentRefreshModel.setNoHasMore(true)
            } else {
                commentRefreshModel.canCanLoadMore.set(false)
            }
        } else {
            if (commentRefreshModel.canCanLoadMore.get() == false) {
                commentRefreshModel.canCanLoadMore.set(true)
            }
            commentPage++
        }
    }

    /**
     * 获取广告信息
     */
    private fun getAdInfo() {
        launchOnIO {
            repository.getCommentAd().checkResult(onSuccess = {
                it.ad_player_comment?.let { bean ->
                    if (bean.isNotEmpty()) {
                        mCommentAdapter.addData(
                            3,
                            mutableListOf(
                                PlayDetailCommentAdapter.PlayDetailCommentAdvertiseItemEntity(bean[0])
                            )
                        )
                    }
                }
            }, onError = { it, _ ->
                DLog.i("===>getAdInfo", "$it")
            })
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
                PlayGlobalData.playAudioModel.get()?.let { playAudio ->
                    RouterHelper.createRouter(HomeService::class.java)
                        .showCommentDialog(
                            it,
                            playAudio.audio_id.toString(),
                            playAudio.anchor_id
                        ) {
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
            PlayGlobalData.playNeedQueryChapterProgress.set(false)
            getChapterAd {
                playManger.playLastMusic()
            }
        }
    }

    fun playNextClick() {
        if (PlayGlobalData.hasNextChapter.get()) {
            PlayGlobalData.playNeedQueryChapterProgress.set(false)
            getChapterAd {
                playManger.playNextMusic()
            }
        }
    }

    private fun seekTextChange(changeText: String) {
        this.seekText.set(changeText)
    }

    /**
     * 推荐点击
     */
    fun onAudioRecommendClick(context: Context, model: AudioRecommend) {
        if (!TextUtils.isEmpty(model.audio_id)) {
            RouterHelper.createRouter(HomeService::class.java)
                .startDetailActivity(context, model.audio_id)
        }
    }

    /**
     * 评论头像点击事件
     */
    fun commentAvatarClick(context: Context, memberId: String) {
        RouterHelper.createRouter(MineService::class.java)
            .toMineMember(context = context, memberId = memberId)
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
                        showUnAttentionDialog(context, followId!!)
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
                    PlayGlobalData.playAudioModel.get()?.let {
                        it.anchor?.let { anchor ->
                            anchor.status = true
                            PlayGlobalData.playAudioModel.set(it)
                        }
                    }
                    isAttention.set(true)
                    showTip(BaseApplication.getContext().getString(R.string.business_attention_success))
                },
                onError = { it, _ ->
                    showContentView()
                    showTip("$it", R.color.business_color_ff5e5e)
                })
        }
    }

    /**
     * 取消关注二次确认弹窗
     */
    private fun showUnAttentionDialog(context: Context, followId: String) {
        getActivity(context)?.let { activity ->
            TipsFragmentDialog().apply {
                titleText = context.String(R.string.business_attention_title)
                contentText = context.String(R.string.business_attention_content)
                leftBtnText = context.String(R.string.business_cancel)
                rightBtnText = context.String(R.string.business_sure)
                rightBtnTextColor = R.color.business_color_ff5e5e
                leftBtnClick = {
                    dismiss()
                }
                rightBtnClick = {
                    unAttentionAnchor(followId)
                    dismiss()
                }
            }.show(activity)
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
                    PlayGlobalData.playAudioModel.get()?.let {
                        it.anchor?.let { anchor ->
                            anchor.status = false
                            PlayGlobalData.playAudioModel.set(it)
                        }
                    }
                    isAttention.set(false)
                    showTip( BaseApplication.getContext().getString(R.string.business_cancel_attention_success))
                },
                onError = { it, _ ->
                    showContentView()
                    showTip("$it", R.color.business_color_ff5e5e)
                })
        }
    }

    /**
     * 主播头像点击事件
     */
    fun clickMemberFun(context: Context) {
        PlayGlobalData.playAudioModel.get()?.let {
            RouterHelper.createRouter(MineService::class.java)
                .toMineMember(context, it.anchor_id)
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

    /**
     * 加入听单
     */
    fun joinListenList(context: Context) {
        if (context is FragmentActivity) {
            if (!isLogin.get()) {
                RouterHelper.createRouter(LoginService::class.java)
                    .quicklyLogin(context)
            } else {
                RouterHelper.createRouter(ListenService::class.java)
                    .showMySheetListDialog(
                        context,
                        PlayGlobalData.playAudioId.get()!!
                    ) { showTip("在“我听-听单”中查看") }
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
                contentText = context.getString(R.string.business_subscribe_success_content)
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

    /**
     * 关闭音频封面广告
     */
    fun closeAudioImgAd(businessAdModel: BusinessAdModel?) {
        PlayGlobalData.playAudioImgAd.set(null)
        BusinessInsertManager.doInsertKeyAndAd(
            BusinessInsertConstance.INSERT_TYPE_AD_CLOSE,
            businessAdModel?.ad_id.toString()
        )
    }

    /**
     * 关闭音频封面广告
     */
    fun closeVoiceImgAd(businessAdModel: BusinessAdModel?) {
        PlayGlobalData.playAdIsPlaying.set(false)
        PlayGlobalData.playVoiceAdClose.set(true)
        PlayGlobalData.playVoiceImgAd.set(null)
        PlayGlobalData.playChapterId.get()?.let {
            musicPlayerManger.setAdPath(arrayListOf())
            musicPlayerManger.startPlayMusic(it)
        }
        BusinessInsertManager.doInsertKeyAndAd(
            BusinessInsertConstance.INSERT_TYPE_AD_CLOSE,
            businessAdModel?.ad_id.toString()
        )
    }

    /**
     * 关闭音频楼层广告
     */
    fun closeAudioFloorAd(businessAdModel: BusinessAdModel?) {
        playFloorAd.set(null)
        BusinessInsertManager.doInsertKeyAndAd(
            BusinessInsertConstance.INSERT_TYPE_AD_CLOSE,
            businessAdModel?.ad_id.toString()
        )
    }

    /**
     * 广告点击事件
     */
    fun audioAdClick(context: Context, businessAdModel: BusinessAdModel?) {
        businessAdModel?.let {
            BannerJumpUtils.onBannerClick(context, it.jump_url, it.ad_id.toString())
        }
    }

    /**
     * 章节点击事件
     */
    fun chapterClick(context: Context, item: DownloadChapter) {
        if (item.chapter_id == playManger.getCurrentPlayerID()) {
            if (playManger.isPlaying()) {
                playManger.pause()
            } else {
                playManger.play()
            }
        } else {
            PlayGlobalData.playNeedQueryChapterProgress.set(true)
            getChapterAd { playManger.startPlayMusic(item.chapter_id.toString()) }
        }
    }

}