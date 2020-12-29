package com.rm.module_home.viewmodel

import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView
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
import com.rm.business_lib.db.DaoUtil
import com.rm.business_lib.db.audiosort.DetailAudioSort
import com.rm.business_lib.db.download.DownloadAudio
import com.rm.business_lib.db.download.DownloadChapter
import com.rm.business_lib.db.listen.ListenAudioEntity
import com.rm.business_lib.db.listen.ListenDaoUtils
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
     * 章节分页加载的数量
     */
    private val chapterPageSize = 20

    /**
     * 下章节页码
     */
    private var nextChapterPage = 1

    /**
     * 上章节页码
     */
    private var upChapterPage = 1

    private var maxChapterPage = -1

    /**
     * 章节列表
     */
    private val chapterList = mutableListOf<DownloadChapter>()

    /**
     * 章节上一次加载的页码
     */
    private var oldChapterPage = -1

    /**
     * 评论当前的页码
     */
    var commentPage = 1

    /**
     * 评论每次加载数据的条数
     */
    private val mPageSize = 12

    /**
     * 章节分页显示与隐藏
     */
    var hideOr = ObservableBoolean(false)

    /**
     * 主播是否关注
     */
    var isAttention = ObservableBoolean(false)

    /**
     * 是否订阅
     */
    val isSubscribed = ObservableField<Boolean>(false)

    /**
     * 关注按钮是否显示
     */
    val attentionVisibility = ObservableField<Boolean>(true)

    /**
     * 评论加载更多
     */
    val commentRefreshStateMode = SmartRefreshLayoutStatusModel()

    /**
     * 章节加载更多
     */
    val chapterRefreshStatus = SmartRefreshLayoutStatusModel()

    /**
     * 主播ID
     */
    private val anchorId = ObservableField<String>()

    /**
     * 音频id
     */
    var audioId = ObservableField<String>("")

    /**
     * 音频收听记录的对象
     */

    var listenAudio = ObservableField<ListenAudioEntity>()

    /**
     * 当前的排序类型
     */
    var mCurSort = AudioSortType.SORT_ASC

    /**
     * 章节总数
     */
    var chapterTotal = ObservableField(0)


    /**
     * 书籍状态
     */
    var showStatus = ObservableField<String>()

    /**
     * 书籍信息
     */
    var detailInfoData = ObservableField<AudioDetailBean>()

    /**
     * 评论数量
     */
    var commentTotal = ObservableField(0)

    /**
     * 整个页面是否可以点击
     */
    var isNoClick = ObservableField<Boolean>(false)

    /**
     * 评论缺省
     */
    var commentEmptyVisible = ObservableField<Boolean>(false)

    val commentContentRvId = R.id.home_detail_comment_recycle_view

    val chapterContentRvId = R.id.home_detail_chapter_rv

    /**
     * 头部dataBinding对象
     */

    var mDataBinding: HomeDetailHeaderBinding? = null

    /**
     * 创建头部详细信息
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
     * 评论dapper
     */
    val homeDetailCommentAdapter by lazy {
        CommonBindVMAdapter<CommentList>(
            this,
            mutableListOf(),
            R.layout.home_detail_item_comment,
            BR.commentViewModel,
            BR.commentItem
        )
    }

    /**
     * 标签adapter
     */
    val homeDetailTagsAdapter by lazy {
        CommonBindAdapter(
            mutableListOf<DetailTags>(),
            R.layout.home_item_book_label,
            BR.detailTags
        )
    }

    /**
     * 章节分页适配器
     */
    val chapterAnthologyAdapter by lazy { HomeDetailChapterPageAdapter(this) }

    /**
     * 章节的适配器
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
     * 评论加载更多
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
                    if(audioRecord == null){
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
     * 获取书籍详情信息
     */
    fun intDetailInfo(audioID: String) {
        showLoading()
        launchOnIO {
            repository.getDetailInfo(audioID).checkResult(
                onSuccess = {
                    showContentView()
                    //如果主播id与当前的用户id一致则隐藏关注按钮

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
                    if (it?.contains("下架") == true || it?.contains("违规") == true) {
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
     * 订阅
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
        showLoading()
        launchOnIO {
            repository.unSubscribe(audioId).checkResult(
                onSuccess = {
                    showContentView()
                    isSubscribed.set(false)
                    showTip("取消订阅成功")

                    BusinessInsertManager.doInsertKeyAndAudio(
                        BusinessInsertConstance.INSERT_TYPE_AUDIO_UNSUBSCRIBED,
                        audioId
                    )
                },
                onError = { it, _ ->
                    showContentView()
                    DLog.i("------->", "取消订阅  $it")
                    showTip("$it", R.color.business_color_ff5e5e)
                }
            )
        }
    }


    /**
     * 跳转到播放器页面
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
                            chapterId = listenRecord.listenChapterId ?: "",
                            currentDuration = queryChapterRecentUpdate?.listen_duration ?: 0,
                            sortType = mCurSort
                        )
                    } else {
                        if (chapterList.isNotEmpty()) {
                            RouterHelper.createRouter(PlayService::class.java).startPlayActivity(
                                context = context,
                                audioId = audioId.get() ?: "",
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
     * 书籍状态
     */
    private fun showStatus(bean: AudioDetailBean?) {
        bean?.list?.let {
            when (it.progress) {
                1 -> showStatus.set("未开播")
                2 -> showStatus.set("已连载${it.count_sequence}集")
                3 -> showStatus.set("已完结（总共${it.count_sequence}集）")
            }
        }
    }

    /**
     * 章节列表
     * page:第几页
     * page_size:每页数量
     * audio_id:音频id
     * sort：排序(asc,desc)
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
                        val predicate: (DownloadChapter) -> Boolean = { it.chapter_id.toString() == chapterId }
                        val position = chapterListModel.list?.indexOfFirst(predicate)

                        position?.let {
                            if(position>0){
                                chapterAdapter.recyclerView.postDelayed({
                                    chapterAdapter.recyclerView.smoothScrollToPosition(it)
                                },200)
                            }
                        }
                    }, onError = { it, _ ->
                        if(listenAudio.get()!=null){
                            listenAudio.set(null)
                        }else{
                            processChapterFailure(it)
                        }
                    })
        }
    }


    /**
     * 处理章节加载失败
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
     * 处理章节数据
     */
    private fun processChapterData(bean: ChapterListModel, page: Int) {
        chapterTotal.set(bean.total)
        if (chapterAnthologyAdapter.data.size == 0) {
            configChapterPageList()
        }
        maxChapterPage = ceil(chapterTotal.get()!! / chapterPageSize.toFloat()).toInt()
        DLog.i(
            "------------>",
            "$mCurSort     $nextChapterPage      $upChapterPage       $oldChapterPage     $page    "
        )

        if (oldChapterPage > page) {
            bean.list?.let { chapterList.addAll(0, it) }
            chapterRefreshStatus.finishRefresh(true)
        } else {
            chapterRefreshStatus.finishLoadMore(true)
            bean.list?.let { chapterList.addAll(it) }
        }

        chapterRefreshStatus.setNoHasMore(page == maxChapterPage)
        chapterRefreshStatus.setCanRefresh(upChapterPage != 1)
        chapterAdapter.setList(chapterList)
        oldChapterPage = page
    }


    /**
     * 章节加载上一页
     */
    fun chapterRefreshData() {
        if (upChapterPage > 1) {
            upChapterPage--
            getChapterList(upChapterPage)
        }
    }

    /**
     * 章节加载更多
     */
    fun chapterLoadData() {
        if (nextChapterPage < maxChapterPage) {
            nextChapterPage++
            getChapterList(nextChapterPage)
        }
    }

    /**
     * 排序点击事件
     */
    fun clickSort() {
        when (mCurSort) {
            AudioSortType.SORT_ASC -> {
                mCurSort = AudioSortType.SORT_DESC
            }
            AudioSortType.SORT_DESC -> {
                mCurSort = AudioSortType.SORT_ASC
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
     * 选集
     */
    fun clickShowAnthology() {
        hideOr.set(!hideOr.get())
    }


    /**
     * 章节分页item点击事件
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
     * 评论点赞
     */
    private fun likeComment(bean: CommentList) {
        showLoading()
        launchOnIO {
            repository.homeLikeComment(bean.id.toString()).checkResult(
                onSuccess = {
                    showContentView()
                    val indexOf = homeDetailCommentAdapter.data.indexOf(bean)
                    bean.is_liked = true
                    bean.likes = bean.likes + 1
                    //记得加上头部的个数，不然会报错  https://github.com/CymChad/BaseRecyclerViewAdapterHelper/issues/871
                    val headerLayoutCount = homeDetailCommentAdapter.headerLayoutCount
                    homeDetailCommentAdapter.notifyItemChanged(indexOf + headerLayoutCount)
                },
                onError = { it, _ ->
                    showContentView()
                    DLog.i("----->", "评论点赞:$it")
                    showTip("$it", R.color.business_color_ff5e5e)
                })
        }
    }


    /**
     * 取消评论点赞
     */
    private fun unLikeComment(bean: CommentList) {
        showLoading()
        launchOnIO {
            repository.homeUnLikeComment(bean.id.toString()).checkResult(
                onSuccess = {
                    showContentView()
                    val indexOf = homeDetailCommentAdapter.data.indexOf(bean)
                    bean.is_liked = false
                    bean.likes = bean.likes - 1
                    val headerLayoutCount = homeDetailCommentAdapter.headerLayoutCount
                    homeDetailCommentAdapter.notifyItemChanged(indexOf + headerLayoutCount)
                },
                onError = { it, _ ->
                    showContentView()
                    DLog.i("----->", "评论点赞:$it")
                    showTip("$it", R.color.business_color_ff5e5e)
                }
            )
        }
    }


    /**
     *
     * 章节 item 点击事件
     */
    fun itemClick(context: Context, bean: DownloadChapter) {
        PlayGlobalData.playNeedQueryChapterProgress.set(true)
        RouterHelper.createRouter(PlayService::class.java).startPlayActivity(
            context,
            audioId = audioId.get()!!,
            chapterId = bean.chapter_id.toString(),
            sortType = mCurSort
        )
    }


    /**
     * 评论列表
     */
    fun getCommentList(audio_id: String) {
        launchOnIO {
            repository.getCommentInfo(audio_id, commentPage, mPageSize).checkResult(
                onSuccess = {
                    commentTotal.set(it.total)
                    processCommentData(it)

                }, onError = { it, _ ->
                    showTip("$it", R.color.business_color_ff5e5e)
                }
            )
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
                titleText = context.String(R.string.business_tips)
                contentText = context.String(R.string.business_sure_cancel_attention)
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
                    isAttention.set(false)
                    showTip("取消关注成功")
                },
                onError = { it, _ ->
                    showContentView()
                    showTip("$it", R.color.business_color_ff5e5e)
                })
        }
    }


    /**
     * 处理评论数据
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
     * 获取章节分页信息
     */
    private fun configChapterPageList() {
        val totalCount = chapterTotal.get() ?: 0
        val list = mutableListOf<DataStr>()
        val i1 = totalCount / chapterPageSize
        val i2 = totalCount % chapterPageSize
        when (mCurSort) {
            AudioSortType.SORT_ASC -> {
                var y = 0
                for (i in 1..i1) {
                    list.add(DataStr("${y + 1}~${i * chapterPageSize}", i))
                    y = i * chapterPageSize
                }
                if (i2 > 0) {
                    list.add(DataStr("${y + 1}~$totalCount", list.size + 1))
                }
                chapterAnthologyAdapter.setList(list)
            }
            AudioSortType.SORT_DESC -> {
                var y = totalCount
                for (i in 1..i1) {
                    list.add(DataStr("${y}~${totalCount - i * chapterPageSize}", i))
                    y = (totalCount - i * chapterPageSize) - 1
                }
                if (i2 > 0) {
                    list.add(DataStr("${y + 1}~1", list.size + 1))
                }
                chapterAnthologyAdapter.setList(list)
            }
        }
    }

    /**
     * 跳转下载章节
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
            createRouter.startDownloadChapterSelectionActivity(
                context,
                downloadAudio
            )
        }
    }


    /**
     * 点赞点击事件
     */
    fun itemLikeClick(context: Context, bean: CommentList) {
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
     * 评论点击事件
     */
    fun clickCommentFun(context: Context) {
        getActivity(context)?.let {
            if (isLogin.get()) {
                audioId.get()?.let { audioId ->
                    HomeCommentDialogHelper(it, audioId) {
                        commentPage = 1
                        getCommentList(audioId)
                        showTip("评论成功")
                    }.showDialog()
                }

            } else {
                quicklyLogin(it)
            }
        }
    }

    /**
     * 主播头像点击事件
     */
    fun clickMemberFun(context: Context) {
        detailInfoData.get()?.let {
            RouterHelper.createRouter(MineService::class.java)
                .toMineMember(context, it.list.anchor_id)
        }
    }

    /**
     * 评论头像点击事件
     */
    fun clickCommentMemberFun(context: Context, memberId: String?) {
        if (TextUtils.isEmpty(memberId)) {
            return
        }
        RouterHelper.createRouter(MineService::class.java)
            .toMineMember(context, memberId!!)
    }

    /**
     * 分享
     */
    fun clickShare(context: Context) {
        getActivity(context)?.let { activity ->
            detailInfoData.get()?.list?.let {
                ShareManage.shareAudio(activity, it.audio_id.toString(), it.audio_name)
            }
        }
    }


    /**
     * 添加听单点击事件
     */
    fun clickCollectionFun(context: Context) {
        getActivity(context)?.let {
            if (!isLogin.get()) {
                quicklyLogin(it)
            } else {
                RouterHelper.createRouter(ListenService::class.java)
                    .showMySheetListDialog(it, audioId.get()!!) {
                        showTip("在“我听-听单”中查看")
                    }
            }
        }

    }

    /**
     * 订阅点击事件
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
     * 快捷登陆
     */
    private fun quicklyLogin(it: FragmentActivity) {
        RouterHelper.createRouter(LoginService::class.java)
            .quicklyLogin(this, it, loginSuccess = {
                intDetailInfo(audioId.get()!!)
                commentPage = 1
                getCommentList(audioId.get()!!)
            })
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