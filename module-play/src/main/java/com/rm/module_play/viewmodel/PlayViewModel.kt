package com.rm.module_play.viewmodel

import android.content.Context
import android.text.TextUtils
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.fragment.app.FragmentActivity
import com.airbnb.lottie.LottieAnimationView
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
import com.rm.business_lib.binding.getPlayCount
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
     * ????????????????????????????????????
     */
    var isLocalChapterList = ObservableBoolean(false)

    /**
     * ?????????????????????
     */
    val playManger: MusicPlayerManager = musicPlayerManger

    /**
     * ?????????????????????????????????
     */
    var seekText = ObservableField<String>("")

    /**
     * ?????????????????????????????????
     */
    var seekChangeVar: (String) -> Unit = { seekTextChange(it) }

    /**
     * ?????????????????????
     */
    private var commentPage = 1

    /**
     * ???????????????????????????
     */
    private val pageSize = 12

    /**
     * ????????????
     */
    var commentTotal = ObservableField(0)

    /**
     * ????????????????????????
     */
    val attentionVisibility = ObservableField<Boolean>(true)

    /**
     * ??????????????????
     */
    var isAttention = ObservableBoolean(false)

    /**
     * ???????????????
     */
    var isSubscribe = ObservableBoolean(false)

    /**
     * ????????????
     */
    val subscriptionCount = ObservableField<String>()

    /**
     * ????????????
     */
    var commentEmptyVisible = ObservableField<Boolean>(false)

    /**
     * ?????? SmartRefreshLayout???????????????
     */
    val commentRefreshModel = SmartRefreshLayoutStatusModel()
    val commentRvScrollY = ObservableField(0)

    val commentContentRvId = R.id.play_comment_rv

    private var lastCommentLikeTime = 0L
    private var lastCommentUnLikeTime = 0L

    /**
     * ??????adapter
     */
    val mCommentAdapter by lazy {
        PlayDetailCommentAdapter(this@PlayViewModel, BR.viewModel, BR.item).apply {
            setOnCommentLikeClickListener(object :
                PlayDetailCommentAdapter.OnCommentLikeClickListener {
                override fun onClickLike(
                    lottieView: LottieAnimationView,
                    textView: AppCompatTextView,
                    bean: PlayDetailCommentAdapter.PlayDetailCommentItemEntity
                ) {
                    playLikeBook(lottieView, textView, bean)
                }
            })
        }
    }

    /**
     * ???????????????Adapter
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


    //??????
    fun playLikeBook(
        lottieView: LottieAnimationView,
        textView: AppCompatTextView, bean: PlayDetailCommentAdapter.PlayDetailCommentItemEntity
    ) {
        if (isLogin.get()) {
            if (bean.data.is_liked) {
                unLikeComment(lottieView, textView, bean)
            } else {
                likeComment(lottieView, textView, bean)
            }
        } else {
            getActivity(lottieView.context)?.let { quicklyLogin(it) }
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
     * ??????????????????
     */
    fun clickDelCommentAd(bean: PlayDetailCommentAdapter.PlayDetailCommentAdvertiseItemEntity) {
        mCommentAdapter.remove(bean)
        BusinessInsertManager.doInsertKeyAndAd(
            BusinessInsertConstance.INSERT_TYPE_AD_CLOSE,
            bean.data.ad_id.toString()
        )
    }

    /**
     * ??????????????????
     */
    fun clickJumpAd(
        context: Context,
        bean: PlayDetailCommentAdapter.PlayDetailCommentAdvertiseItemEntity
    ) {
        BannerJumpUtils.onBannerClick(context, bean.data.jump_url, bean.data.ad_id.toString())
    }

    /**
     * ????????????
     */
    private fun quicklyLogin(it: FragmentActivity) {
        RouterHelper.createRouter(LoginService::class.java)
            .quicklyLogin(it, loginSuccess = {
                commentPage = 1
                getCommentList()
            })
    }

    /**
     * ????????????ID???????????????????????????????????????????????????????????????????????????????????????chapterId
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
     * ????????????ID????????????????????????????????????
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
     * ????????????ID?????????ID???????????????????????????????????????????????????????????????????????????page?????????????????????
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
     * ????????????????????????
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
     * ?????????????????????
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
     * ??????????????????
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
     * ??????????????????
     */
    private fun unLikeComment(
        lottieView: LottieAnimationView,
        textView: AppCompatTextView, bean: PlayDetailCommentAdapter.PlayDetailCommentItemEntity
    ) {
        if (System.currentTimeMillis() - lastCommentLikeTime < 1000) {
            return
        }
        launchOnIO {
            repository.homeUnLikeComment(bean.data.id).checkResult(
                onSuccess = {
                    lastCommentLikeTime = System.currentTimeMillis()
                    val indexOf = mCommentAdapter.data.indexOf(bean)
                    if (indexOf != -1) {
                        textView.text = getPlayCount(bean.data.likes - 1)
                        bean.data.is_liked = false
                        bean.data.likes = bean.data.likes - 1
                        startLottieAnim(lottieView, false)
                    }
                },
                onError = { it, _ ->
                    lastCommentLikeTime = System.currentTimeMillis()
                    DLog.i("----->", "????????????:$it")
                }
            )
        }
    }

    /**
     * ????????????
     */
    private fun likeComment(
        lottieView: LottieAnimationView,
        textView: AppCompatTextView, bean: PlayDetailCommentAdapter.PlayDetailCommentItemEntity
    ) {
        if (System.currentTimeMillis() - lastCommentUnLikeTime < 1000) {
            return
        }
        launchOnIO {
            repository.homeLikeComment(bean.data.id).checkResult(
                onSuccess = {
                    lastCommentUnLikeTime = System.currentTimeMillis()
                    val indexOf = mCommentAdapter.data.indexOf(bean)
                    if (indexOf != -1) {
                        textView.text = getPlayCount(bean.data.likes + 1)
                        bean.data.is_liked = true
                        bean.data.likes = bean.data.likes + 1
                        startLottieAnim(lottieView, true)
                    }
                },
                onError = { it, _ ->
                    lastCommentUnLikeTime = System.currentTimeMillis()
                    DLog.i("----->", "????????????:$it")
                })
        }
    }

    private fun startLottieAnim(lottieView: LottieAnimationView, isLike: Boolean) {
        lottieView.setImageDrawable(null)
        lottieView.clearAnimation()
        if (isLike) {
            lottieView.setAnimation("business_like.json")
        } else {
            lottieView.setAnimation("business_unlike.json")
        }
        lottieView.playAnimation()
    }

    /**
     * ??????
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
                    DLog.i("------->", "????????????  $it")
                    showTip("$it", R.color.business_color_ff5e5e)
                }
            )
        }
    }

    /**
     * ????????????
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
                    showTip("??????????????????")
                },
                onError = { it, _ ->
                    DLog.i("------->", "????????????  $it")
                    showTip("$it", R.color.business_color_ff5e5e)
                }
            )
        }
    }


    /**
     *????????????
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
     * ??????????????????????????????
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
     * ??????????????????
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
     * ??????????????????????????????
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
     * ??????????????????
     */
    fun commentLoadData() {
        getCommentList()
    }

    /**
     * ??????????????????
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
                            showTip("????????????")
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
     * ????????????
     */
    fun onAudioRecommendClick(context: Context, model: AudioRecommend) {
        if (!TextUtils.isEmpty(model.audio_id)) {
            RouterHelper.createRouter(HomeService::class.java)
                .startDetailActivity(context, model.audio_id)
        }
    }

    /**
     * ????????????????????????
     */
    fun commentAvatarClick(context: Context, memberId: String) {
        RouterHelper.createRouter(MineService::class.java)
            .toMineMember(context = context, memberId = memberId)
    }

    /**
     * ??????????????????
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
     * ????????????
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
                    showTip(
                        BaseApplication.getContext().getString(R.string.business_attention_success)
                    )
                },
                onError = { it, _ ->
                    showContentView()
                    showTip("$it", R.color.business_color_ff5e5e)
                })
        }
    }

    /**
     * ??????????????????????????????
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
     * ??????????????????
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
                    showTip(
                        BaseApplication.getContext()
                            .getString(R.string.business_cancel_attention_success)
                    )
                },
                onError = { it, _ ->
                    showContentView()
                    showTip("$it", R.color.business_color_ff5e5e)
                })
        }
    }

    /**
     * ????????????????????????
     */
    fun clickMemberFun(context: Context) {
        PlayGlobalData.playAudioModel.get()?.let {
            RouterHelper.createRouter(MineService::class.java)
                .toMineMember(context, it.anchor_id)
        }
    }

    /**
     * ??????????????????
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
     * ????????????
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
                    ) { showTip("????????????-??????????????????") }
            }
        }
    }

    /**
     * ????????????
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
     * ????????????????????????
     */
    fun closeAudioImgAd(businessAdModel: BusinessAdModel?) {
        PlayGlobalData.playAudioImgAd.set(null)
        BusinessInsertManager.doInsertKeyAndAd(
            BusinessInsertConstance.INSERT_TYPE_AD_CLOSE,
            businessAdModel?.ad_id.toString()
        )
    }

    /**
     * ????????????????????????
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
     * ????????????????????????
     */
    fun closeAudioFloorAd(businessAdModel: BusinessAdModel?) {
        playFloorAd.set(null)
        BusinessInsertManager.doInsertKeyAndAd(
            BusinessInsertConstance.INSERT_TYPE_AD_CLOSE,
            businessAdModel?.ad_id.toString()
        )
    }

    /**
     * ??????????????????
     */
    fun audioAdClick(context: Context, businessAdModel: BusinessAdModel?) {
        businessAdModel?.let {
            BannerJumpUtils.onBannerClick(context, it.jump_url, it.ad_id.toString())
        }
    }

    /**
     * ??????????????????
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