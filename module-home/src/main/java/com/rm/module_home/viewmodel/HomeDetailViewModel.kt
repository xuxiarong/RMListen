package com.rm.module_home.viewmodel

import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.rm.baselisten.BaseApplication
import com.rm.baselisten.BaseConstance
import com.rm.baselisten.adapter.single.CommonBindAdapter
import com.rm.baselisten.adapter.single.CommonBindVMAdapter
import com.rm.baselisten.net.checkResult
import com.rm.baselisten.util.DLog
import com.rm.baselisten.util.getBooleanMMKV
import com.rm.baselisten.util.putMMKV
import com.rm.baselisten.utilExt.String
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.business_lib.*
import com.rm.business_lib.base.dialog.CustomTipsFragmentDialog
import com.rm.baselisten.dialog.TipsFragmentDialog
import com.rm.business_lib.bean.AudioDetailBean
import com.rm.business_lib.bean.ChapterListModel
import com.rm.business_lib.bean.DataStr
import com.rm.business_lib.bean.DetailTags
import com.rm.business_lib.binding.getPlayCount
import com.rm.business_lib.db.DaoUtil
import com.rm.business_lib.db.audiosort.DetailAudioSort
import com.rm.business_lib.db.download.DownloadAudio
import com.rm.business_lib.db.download.DownloadChapter
import com.rm.business_lib.db.listen.ListenAudioEntity
import com.rm.business_lib.db.listen.ListenDaoUtils
import com.rm.business_lib.download.file.DownLoadFileUtils
import com.rm.business_lib.insertpoint.BusinessInsertConstance
import com.rm.business_lib.insertpoint.BusinessInsertManager
import com.rm.business_lib.share.ShareManage
import com.rm.business_lib.wedgit.smartrefresh.model.SmartRefreshLayoutStatusModel
import com.rm.component_comm.download.DownloadService
import com.rm.component_comm.listen.ListenService
import com.rm.component_comm.login.LoginService
import com.rm.component_comm.mine.MineService
import com.rm.component_comm.play.PlayService
import com.rm.component_comm.router.RouterHelper
import com.rm.module_home.BR
import com.rm.module_home.R
import com.rm.module_home.adapter.HomeDetailChapterPageAdapter
import com.rm.module_home.adapter.HomeDetailCommentAdapter
import com.rm.module_home.databinding.HomeDetailHeaderBinding
import com.rm.module_home.model.home.detail.CommentList
import com.rm.module_home.model.home.detail.HomeCommentBean
import com.rm.module_home.repository.HomeRepository
import com.rm.module_home.util.HomeCommentDialogHelper
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.ceil


class HomeDetailViewModel(private val repository: HomeRepository) : BaseVMViewModel() {

    /**
     * ???????????????????????????
     */
    private val chapterPageSize = 20

    /**
     * ???????????????
     */
    private var nextChapterPage = 1

    /**
     * ???????????????
     */
    private var upChapterPage = 1

    private var maxChapterPage = -1

    /**
     * ????????????
     */
    private val chapterList = mutableListOf<DownloadChapter>()

    /**
     * ??????????????????????????????
     */
    private var oldChapterPage = -1

    /**
     * ?????????????????????
     */
    private var commentPage = 1

    /**
     * ?????????????????????????????????
     */
    private val mPageSize = 12

    /**
     * ???????????????????????????
     */
    var hideOr = ObservableBoolean(false)

    /**
     * ??????????????????
     */
    var isAttention = ObservableBoolean(false)

    /**
     * ????????????
     */
    val isSubscribed = ObservableField<Boolean>(false)

    /**
     * ????????????????????????
     */
    val attentionVisibility = ObservableField<Boolean>(true)

    /**
     * ??????????????????
     */
    val commentRefreshStateMode = SmartRefreshLayoutStatusModel()
    val commentRvScrollY = ObservableField(0)

    /**
     * ??????????????????
     */
    val chapterRefreshStatus = SmartRefreshLayoutStatusModel()

    /**
     * ??????ID
     */
    private val anchorId = ObservableField<String>()

    /**
     * ??????id
     */
    var audioId = ObservableField<String>("")

    /**
     * ???????????????????????????
     */

    var listenAudio = ObservableField<ListenAudioEntity>()

    /**
     * ?????????????????????
     */
    var mCurSort = AudioSortType.SORT_ASC

    /**
     * ???????????????
     */
    val isSortAsc = ObservableBoolean(true)

    /**
     * ????????????
     */
    var chapterTotal = ObservableField(0)


    /**
     * ????????????
     */
    var showStatus = ObservableField<String>()

    /**
     * ????????????
     */
    var detailInfoData = ObservableField<AudioDetailBean>()

    /**
     * ????????????
     */
    var commentTotal = ObservableField(0)

    /**
     * ??????????????????????????????
     */
    var isNoClick = ObservableField<Boolean>(false)

    /**
     * ????????????
     */
    var commentEmptyVisible = ObservableField<Boolean>(false)

    private var lastCommentLikeTime = 0L
    private var lastCommentUnLikeTime = 0L

    val commentContentRvId = R.id.home_detail_comment_recycle_view


    /**
     * ??????dataBinding??????
     */

    var mDataBinding: HomeDetailHeaderBinding? = null

    /**
     * ????????????????????????
     */
    fun createHeader(view: RecyclerView) {
        mDataBinding = DataBindingUtil.inflate<HomeDetailHeaderBinding>(
            LayoutInflater.from(view.context),
            R.layout.home_detail_header,
            view,
            false
        ).apply {
            homeDetailCommentAdapter.addHeaderView(this.root)
            setVariable(BR.viewModel, this@HomeDetailViewModel)
        }
    }

    /**
     * ??????dapper
     */
    val homeDetailCommentAdapter by lazy {
        HomeDetailCommentAdapter(this).apply {
            setOnCommentLikeClickListener(object :
                HomeDetailCommentAdapter.OnCommentLikeClickListener {
                override fun onClickLike(
                    lottieView: LottieAnimationView,
                    textView: AppCompatTextView,
                    bean: CommentList
                ) {
                    itemLikeClick(lottieView, textView, bean)
                }

                override fun onClickMemberIcon(context: Context,  bean: CommentList) {
                    clickCommentMemberFun(context, bean.member_id)
                }
            })
        }
    }

    /**
     * ??????adapter
     */
    val homeDetailTagsAdapter by lazy {
        CommonBindAdapter(
            mutableListOf<DetailTags>(),
            R.layout.home_item_book_label,
            BR.detailTags
        )
    }

    /**
     * ?????????????????????
     */
    val chapterAnthologyAdapter by lazy { HomeDetailChapterPageAdapter(this) }

    /**
     * ??????????????????
     */
    val chapterAdapter by lazy {
        CommonBindVMAdapter<DownloadChapter>(
            this,
            mutableListOf(),
            R.layout.home_item_detail_chapter,
            BR.chapterclick,
            BR.DetailChapterViewModel
        )
    }

    /**
     * ??????????????????
     */
    fun commentLoadMoreData() {
        audioId.get()?.let {
            getCommentList(it)
        }
    }

    fun queryAudioListenRecord() {
        audioId.get()?.let {
            launchOnIO {
                try {
                    val audioRecord = ListenDaoUtils.queryAudioById(it.toLong())
                    listenAudio.set(audioRecord)
                    isAttention.set(audioRecord?.anchor?.status ?: false)
                    isSubscribed.set(audioRecord?.is_subscribe)
                    if (audioRecord == null) {
                        listenAudio.notifyChange()
                    }
                } catch (e: Exception) {
                    listenAudio.set(null)
                    listenAudio.notifyChange()
                }
            }
        }
    }

    /**
     * ????????????????????????
     */
    fun intDetailInfo(audioID: String) {
        showLoading()
        launchOnIO {
            repository.getDetailInfo(audioID).checkResult(
                onSuccess = {
                    showContentView()
                    //????????????id??????????????????id???????????????????????????

                    loginUser.get()?.let { user ->
                        attentionVisibility.set(user.id != it.list.anchor_id)
                    }
                    detailInfoData.set(it)
                    isAttention.set(it.list.anchor.status)
                    isSubscribed.set(it.list.is_subscribe)
                    anchorId.set(it.list.anchor_id)
                    homeDetailTagsAdapter.setList(it.list.tags)
                    showStatus(it)
                }, onError = { it, _ ->
                    showContentView()
                    if (it?.contains("??????") == true || it?.contains("??????") == true) {
                        isNoClick.set(true)
                        viewModelScope.launch {
                            delay(1500)
                            finish()
                        }
                    } else {
                        isNoClick.set(false)
                    }
                    showTip(it.toString(), R.color.business_color_ff5e5e)
                }
            )
        }
    }

    /**
     * ??????
     */
    private fun subscribe(context: Context, audioId: String) {
        showLoading()
        launchOnIO {
            repository.subscribe(audioId).checkResult(
                onSuccess = {
                    showContentView()
                    isSubscribed.set(true)
                    subscribeSuccess(context)

                    BusinessInsertManager.doInsertKeyAndAudio(
                        BusinessInsertConstance.INSERT_TYPE_AUDIO_SUBSCRIPTION,
                        audioId
                    )
                },
                onError = { it, _ ->
                    showContentView()
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
        showLoading()
        launchOnIO {
            repository.unSubscribe(audioId).checkResult(
                onSuccess = {
                    showContentView()
                    isSubscribed.set(false)
                    showTip("??????????????????")

                    BusinessInsertManager.doInsertKeyAndAudio(
                        BusinessInsertConstance.INSERT_TYPE_AUDIO_UNSUBSCRIBED,
                        audioId
                    )
                },
                onError = { it, _ ->
                    showContentView()
                    DLog.i("------->", "????????????  $it")
                    showTip("$it", R.color.business_color_ff5e5e)
                }
            )
        }
    }


    /**
     * ????????????????????????
     */
    fun clickPlayPage(context: Context) {
        val playService = RouterHelper.createRouter(PlayService::class.java)
        val playInfo = BaseConstance.basePlayInfoModel.get()
        val playStatus = BaseConstance.basePlayStatusModel.get()
        val playProgress = BaseConstance.basePlayProgressModel.get()

        detailInfoData.get()?.let {
            when (playInfo?.playAudioId) {
                audioId.get() -> {
                    if (chapterAdapter.data.isNotEmpty()) {
                        if (playStatus != null && playStatus.isStart()) {
                            playService.pausePlay()
                        } else {
                            RouterHelper.createRouter(PlayService::class.java).startPlayActivity(
                                context = context,
                                audioId = audioId.get() ?: "",
                                audioInfo = detailInfoData.get()?.list ?: DownloadAudio(),
                                chapterId = playInfo?.playChapterId ?: "",
                                currentDuration = playProgress?.currentDuration ?: 0,
                                sortType = mCurSort
                            )
                        }
                    } else {
                        showTip(BaseApplication.CONTEXT.getString(R.string.business_no_content))
                    }
                }
                else -> {
                    val listenRecord = listenAudio.get()
                    if (listenRecord != null && !TextUtils.isEmpty(listenRecord.listenChapterId)) {
                        val queryChapterRecentUpdate = ListenDaoUtils.queryChapterRecentUpdate(
                            listenRecord.audio_id,
                            listenRecord.listenChapterId.toLong()
                        )
                        RouterHelper.createRouter(PlayService::class.java).startPlayActivity(
                            context = context,
                            audioId = audioId.get() ?: "",
                            audioInfo = detailInfoData.get()?.list ?: DownloadAudio(),
                            chapterId = listenRecord.listenChapterId ?: "",
                            currentDuration = queryChapterRecentUpdate?.listen_duration ?: 0,
                            sortType = mCurSort
                        )
                    } else {
                        if (chapterList.isNotEmpty()) {
                            RouterHelper.createRouter(PlayService::class.java).startPlayActivity(
                                context = context,
                                audioId = audioId.get() ?: "",
                                audioInfo = detailInfoData.get()?.list ?: DownloadAudio(),
                                chapterId = chapterList[0].chapter_id.toString(),
                                sortType = mCurSort
                            )
                        }
                    }
                }
            }
        }
    }

    /**
     * ????????????
     */
    private fun showStatus(bean: AudioDetailBean?) {
        bean?.list?.let {
            when (it.progress) {
                1 -> showStatus.set("?????????")
                2 -> showStatus.set("?????????${it.count_sequence}???")
                3 -> showStatus.set("??????????????????${it.count_sequence}??????")
            }
        }
    }

    /**
     * ????????????
     * page:?????????
     * page_size:????????????
     * audio_id:??????id
     * sort?????????(asc,desc)
     */
    fun getChapterList(page: Int) {
        launchOnIO {
            repository.chapterList(audioId.get()!!, page, chapterPageSize, mCurSort)
                .checkResult(
                    onSuccess = {
                        processChapterData(it, page)
                    }, onError = { it, _ ->
                        processChapterFailure(it)
                    })
        }
    }

    fun getChapterListWithId(chapterId: String) {
        launchOnIO {
            repository.getChapterListWithId(audioId.get()!!, chapterPageSize, chapterId, mCurSort)
                .checkResult(
                    onSuccess = { chapterListModel ->
                        upChapterPage = chapterListModel.page
                        nextChapterPage = chapterListModel.page
                        processChapterData(chapterListModel, chapterListModel.page)
                        val predicate: (DownloadChapter) -> Boolean =
                            { it.chapter_id.toString() == chapterId }
                        val position = chapterListModel.list?.indexOfFirst(predicate)

                        position?.let {
                            if (position > 0) {
                                chapterAdapter.recyclerView.postDelayed({
                                    chapterAdapter.recyclerView.smoothScrollToPosition(it)
                                }, 200)
                            }
                        }
                    }, onError = { it, _ ->
                        if (listenAudio.get() != null) {
                            listenAudio.set(null)
                        } else {
                            processChapterFailure(it)
                        }
                    })
        }
    }


    /**
     * ????????????????????????
     */
    private fun processChapterFailure(msg: String?) {
        showTip("$msg", R.color.business_color_ff5e5e)
        when (mCurSort) {
            AudioSortType.SORT_ASC -> {
                if (nextChapterPage == 1) {
                    chapterRefreshStatus.finishRefresh(false)
                } else {
                    chapterRefreshStatus.finishLoadMore(false)
                }
            }
            AudioSortType.SORT_DESC -> {
                if (upChapterPage == 1) {
                    chapterRefreshStatus.finishRefresh(false)
                } else {
                    chapterRefreshStatus.finishLoadMore(false)
                }
            }
        }
    }

    /**
     * ??????????????????
     */
    private fun processChapterData(bean: ChapterListModel, page: Int) {
        chapterTotal.set(bean.total)
        if (chapterAnthologyAdapter.data.size == 0) {
            configChapterPageList()
        }
        maxChapterPage = ceil(chapterTotal.get()!! / chapterPageSize.toFloat()).toInt()

        if (oldChapterPage > page) {
            bean.list?.let { chapterList.addAll(0, getChapterStatus(it)) }
            chapterRefreshStatus.finishRefresh(true)
        } else {
            chapterRefreshStatus.finishLoadMore(true)
            bean.list?.let { chapterList.addAll(getChapterStatus(it)) }
        }
        chapterRefreshStatus.setNoHasMore(nextChapterPage == maxChapterPage)
        chapterRefreshStatus.setCanRefresh(upChapterPage != 1)
        chapterAdapter.setList(chapterList)
        oldChapterPage = page
    }


    /**
     * ?????????????????????
     */
    fun chapterRefreshData() {
        if (upChapterPage > 1) {
            upChapterPage--
            getChapterList(upChapterPage)
        }
    }

    /**
     * ??????????????????
     */
    fun chapterLoadData() {
        if (nextChapterPage < maxChapterPage) {
            nextChapterPage++
            getChapterList(nextChapterPage)
        }
    }

    /**
     * ??????????????????
     */
    fun clickSort() {
        when (mCurSort) {
            AudioSortType.SORT_ASC -> {
                mCurSort = AudioSortType.SORT_DESC
                isSortAsc.set(false)
            }
            AudioSortType.SORT_DESC -> {
                mCurSort = AudioSortType.SORT_ASC
                isSortAsc.set(true)
            }
        }
        configChapterPageList()
        nextChapterPage = 1
        upChapterPage = 1
        chapterList.clear()
        chapterRefreshStatus.setCanRefresh(true)
        chapterRefreshStatus.setResetNoMoreData(true)
        getChapterList(1)
        val daoUtil = DaoUtil(DetailAudioSort::class.java, "")
        daoUtil.saveOrUpdate(DetailAudioSort(audioId.get()!!.toLong(), mCurSort))
    }

    /**
     * ??????
     */
    fun clickShowAnthology() {
        hideOr.set(!hideOr.get())
    }


    /**
     * ????????????item????????????
     */
    fun itemClickSelectChapter(page: Int) {
        nextChapterPage = page
        upChapterPage = page
        chapterList.clear()
        hideOr.set(!hideOr.get())
        chapterRefreshStatus.setCanRefresh(true)
        chapterRefreshStatus.setResetNoMoreData(true)
        getChapterList(page)
    }

    /**
     * ????????????
     */
    private fun likeComment(
        lottieView: LottieAnimationView,
        textView: AppCompatTextView, bean: CommentList
    ) {
        if (System.currentTimeMillis() - lastCommentLikeTime < 1000) {
            return
        }
        launchOnIO {
            repository.homeLikeComment(bean.id.toString()).checkResult(
                onSuccess = {
                    lastCommentLikeTime = System.currentTimeMillis()
                    val indexOf =
                        homeDetailCommentAdapter.data.indexOf(bean)
                    if (indexOf != -1) {
                        textView.text = getPlayCount(bean.likes + 1)
                        bean.is_liked = true
                        bean.likes = bean.likes + 1
                        startLottieAnim(lottieView, true)
                    }
                },
                onError = { it, _ ->
                    DLog.i("----->", "????????????:$it")
                    lastCommentLikeTime = System.currentTimeMillis()
                    showTip("$it", R.color.business_color_ff5e5e)
                })
        }
    }


    /**
     * ??????????????????
     */
    private fun unLikeComment(
        lottieView: LottieAnimationView,
        textView: AppCompatTextView,
        bean: CommentList
    ) {
        if (System.currentTimeMillis() - lastCommentUnLikeTime < 1000) {
            return
        }
        launchOnIO {
            repository.homeUnLikeComment(bean.id.toString()).checkResult(
                onSuccess = {
                    lastCommentUnLikeTime = System.currentTimeMillis()
                    val indexOf =
                        homeDetailCommentAdapter.data.indexOf(bean)
                    if (indexOf != -1) {
                        textView.text = getPlayCount(bean.likes - 1)
                        bean.is_liked = false
                        bean.likes = bean.likes - 1
                        startLottieAnim(lottieView, false)
                    }
                },
                onError = { it, _ ->
                    lastCommentUnLikeTime = System.currentTimeMillis()
                    DLog.i("----->", "????????????:$it")
                    showTip("$it", R.color.business_color_ff5e5e)
                }
            )
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
     *
     * ?????? item ????????????
     */
    fun itemClick(context: Context, bean: DownloadChapter) {
        PlayGlobalData.playNeedQueryChapterProgress.set(true)
        RouterHelper.createRouter(PlayService::class.java).startPlayActivity(
            context,
            audioId = audioId.get()!!,
            chapterId = bean.chapter_id.toString(),
            audioInfo = detailInfoData.get()?.list ?: DownloadAudio(),
            sortType = mCurSort
        )
    }


    /**
     * ????????????
     */
    fun getCommentList(audio_id: String) {
        launchOnIO {
            repository.getCommentInfo(audio_id, commentPage, mPageSize).checkResult(
                onSuccess = {
                    commentTotal.set(it.total)
                    processCommentData(it)
                    commentRvScrollY.set(-1)
                    commentRvScrollY.notifyChange()
                }, onError = { it, _ ->
                    showTip("$it", R.color.business_color_ff5e5e)
                }
            )
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
     * ??????????????????
     */
    private fun processCommentData(bean: HomeCommentBean) {
        commentRefreshStateMode.finishLoadMore(true)
        if (commentPage == 1) {
            if (bean.list_comment?.size ?: 0 > 0) {
                commentEmptyVisible.set(false)
                homeDetailCommentAdapter.setList(bean.list_comment)
            } else {
                commentEmptyVisible.set(true)
            }

        } else {
            bean.list_comment?.let {
                homeDetailCommentAdapter.addData(it)
            }
        }

        if (homeDetailCommentAdapter.data.size >= bean.total || bean.list_comment?.size ?: 0 < mPageSize) {
            if (homeDetailCommentAdapter.data.size > 0) {
                commentRefreshStateMode.setNoHasMore(true)
            } else {
                commentRefreshStateMode.canCanLoadMore.set(false)
            }
        } else {
            if (commentRefreshStateMode.canCanLoadMore.get() == false) {
                commentRefreshStateMode.canCanLoadMore.set(true)
            }
            ++commentPage
        }

    }

    /**
     * ????????????????????????
     */
    private fun configChapterPageList() {
        val totalCount = chapterTotal.get() ?: 0
        when (mCurSort) {
            AudioSortType.SORT_ASC -> {
                chapterSortAsc(totalCount)
            }
            AudioSortType.SORT_DESC -> {
                chapterSortDesc(totalCount)
            }
        }
    }

    /**
     * ??????
     */
    private fun chapterSortAsc(totalCount: Int) {
        val list = mutableListOf<DataStr>()
        val i1 = totalCount / chapterPageSize
        val i2 = totalCount % chapterPageSize
        var y = 0
        for (i in 1..i1) {
            list.add(DataStr("${getNumStr(y + 1)}~${i * chapterPageSize}", i))
            y = i * chapterPageSize
        }
        if (i2 > 0) {
            list.add(DataStr("${getNumStr(y + 1)}~${getNumStr(totalCount)}", list.size + 1))
        }
        chapterAnthologyAdapter.setList(list)
    }

    /**
     * ??????
     */
    private fun chapterSortDesc(totalCount: Int) {
        val list = mutableListOf<DataStr>()
        val i1 = totalCount / chapterPageSize
        val i2 = totalCount % chapterPageSize
        var y = totalCount
        for (i in 1..i1) {
            list.add(DataStr("${y}~${getNumStr(totalCount - i * chapterPageSize + 1)}", i))
            y = (totalCount - i * chapterPageSize)
        }
        if (i2 > 0) {
            list.add(DataStr("${getNumStr(y)}~01", list.size + 1))
        }
        chapterAnthologyAdapter.setList(list)
    }

    private fun getNumStr(num: Int): String {
        return if (num > 9) {
            "$num"
        } else {
            "0$num"
        }
    }

    /**
     * ??????????????????
     */
    fun startDownloadChapterActivity(context: Context) {
        val homeDetailModel = detailInfoData.get()
        if (homeDetailModel != null) {
            val createRouter = RouterHelper.createRouter(DownloadService::class.java)
            val downloadAudio = DownloadAudio()
            downloadAudio.audio_id = homeDetailModel.list.audio_id.toLong()
            downloadAudio.audio_name = homeDetailModel.list.audio_name
            downloadAudio.author = homeDetailModel.list.author
            downloadAudio.audio_cover_url = homeDetailModel.list.audio_cover_url
            downloadAudio.status = homeDetailModel.list.status
            downloadAudio.last_sequence = homeDetailModel.list.last_sequence
            downloadAudio.anchor = homeDetailModel.list.anchor
            downloadAudio.count_sequence = homeDetailModel.list.count_sequence
            createRouter.startDownloadChapterSelectionActivity(
                context,
                downloadAudio
            )
        }
    }

    private fun getChapterStatus(chapterList: List<DownloadChapter>): MutableList<DownloadChapter> {
        val audioName = detailInfoData.get()?.list?.audio_name ?: ""
        val audioId = detailInfoData.get()?.list?.audio_id ?: 0L
        chapterList.forEach {
            it.audio_name = audioName
            it.audio_id = audioId
            DownLoadFileUtils.checkChapterIsDownload(chapter = it)
        }
        return chapterList.toMutableList()
    }

    /**
     * ??????????????????
     */
    fun itemLikeClick(
        lottieView: LottieAnimationView,
        textView: AppCompatTextView,
        bean: CommentList
    ) {
        if (isLogin.get()) {
            if (bean.is_liked) {
                unLikeComment(lottieView, textView, bean)
            } else {
                likeComment(lottieView, textView, bean)
            }
        } else {
            getActivity(lottieView.context)?.let { quicklyLogin(it) }
        }
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
     * ??????????????????
     */
    fun clickCommentFun(context: Context) {
        getActivity(context)?.let {
            if (isLogin.get()) {
                audioId.get()?.let { audioId ->
                    anchorId.get()?.let { anchorId ->
                        HomeCommentDialogHelper(it, audioId, anchorId) {
                            commentPage = 1
                            getCommentList(audioId)
                            showTip("????????????")
                        }.showDialog()
                    }
                }
            } else {
                quicklyLogin(it)
            }
        }
    }

    /**
     * ????????????????????????
     */
    fun clickMemberFun(context: Context) {
        detailInfoData.get()?.let {
            RouterHelper.createRouter(MineService::class.java)
                .toMineMember(context, it.list.anchor_id)
        }
    }

    /**
     * ????????????????????????
     */
    fun clickCommentMemberFun(context: Context, memberId: String?) {
        if (TextUtils.isEmpty(memberId)) {
            return
        }
        RouterHelper.createRouter(MineService::class.java)
            .toMineMember(context, memberId!!)
    }

    /**
     * ??????
     */
    fun clickShare(context: Context) {
        getActivity(context)?.let { activity ->
            detailInfoData.get()?.list?.let {
                ShareManage.shareAudio(activity, it.audio_id.toString(), it.audio_name)
            }
        }
    }


    /**
     * ????????????????????????
     */
    fun clickCollectionFun(context: Context) {
        getActivity(context)?.let {
            if (!isLogin.get()) {
                quicklyLogin(it) {
                    showAddSheetDialog(it)
                }
            } else {
                showAddSheetDialog(it)
            }
        }
    }

    private fun showAddSheetDialog(it: FragmentActivity) {
        RouterHelper.createRouter(ListenService::class.java)
            .showMySheetListDialog(it, audioId.get() ?: "")
            { showTip("????????????-??????????????????") }
    }

    /**
     * ??????????????????
     */
    fun clickSubscribeFun(context: Context) {
        getActivity(context)?.let {
            if (!isLogin.get()) {
                quicklyLogin(it)
            } else {
                if (isSubscribed.get() == true) {
                    audioId.get()?.let { audioId -> unSubscribe(audioId) }
                } else {
                    audioId.get()?.let { audioId -> subscribe(context, audioId) }
                }
            }
        }
    }


    /**
     * ????????????
     */
    private fun quicklyLogin(it: FragmentActivity, loginSuccess: () -> Unit? = {}) {
        RouterHelper.createRouter(LoginService::class.java)
            .quicklyLogin(it, loginSuccess = {
                intDetailInfo(audioId.get()!!)
                commentPage = 1
                getCommentList(audioId.get()!!)
                loginSuccess()
            })
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
                    RouterHelper.createRouter(ListenService::class.java)
                        .startSubscription(activity)
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