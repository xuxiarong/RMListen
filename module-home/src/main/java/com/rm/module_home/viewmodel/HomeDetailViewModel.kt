package com.rm.module_home.viewmodel

import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.text.TextUtils
import android.view.LayoutInflater
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView
import com.rm.baselisten.BaseConstance
import com.rm.baselisten.adapter.single.CommonBindAdapter
import com.rm.baselisten.adapter.single.CommonBindVMAdapter
import com.rm.baselisten.net.checkResult
import com.rm.baselisten.util.DLog
import com.rm.baselisten.util.getBooleanMMKV
import com.rm.baselisten.util.putMMKV
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.business_lib.AudioSortType
import com.rm.business_lib.IS_FIRST_SUBSCRIBE
import com.rm.business_lib.base.dialog.CustomTipsFragmentDialog
import com.rm.business_lib.bean.AudioDetailBean
import com.rm.business_lib.bean.ChapterListModel
import com.rm.business_lib.bean.DataStr
import com.rm.business_lib.bean.DetailTags
import com.rm.business_lib.db.download.DownloadAudio
import com.rm.business_lib.db.download.DownloadChapter
import com.rm.business_lib.db.listen.ListenAudioEntity
import com.rm.business_lib.db.listen.ListenDaoUtils
import com.rm.business_lib.isLogin
import com.rm.business_lib.loginUser
import com.rm.business_lib.share.Share2
import com.rm.business_lib.share.ShareContentType
import com.rm.business_lib.wedgit.smartrefresh.model.SmartRefreshLayoutStatusModel
import com.rm.component_comm.download.DownloadService
import com.rm.component_comm.listen.ListenService
import com.rm.component_comm.login.LoginService
import com.rm.component_comm.mine.MineService
import com.rm.component_comm.play.PlayService
import com.rm.component_comm.router.RouterHelper
import com.rm.module_home.BR
import com.rm.module_home.R
import com.rm.module_home.adapter.HomeDetailChapterAdapter
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

    companion object {
        //章节加载上一页
        const val CHAPTER_PREVIOUS_PAGE = "chapter_previous_page"

        //章节加载下一页
        const val CHAPTER_NEXT_PAGE = "chapter_next_page"

        //刷新章节
        const val CHAPTER_REFRESH_PAGE = "chapter_refresh_page"
    }


    /**
     * 下一页章节的页码
     */
    var nextChapterPage = 1

    /**
     * 上一页章节页码
     */
    var previousChapterPage = 1

    /**
     * 章节的最大页码
     */
    private var chapterMaxPage = -1

    /**
     * 章节分页加载的数量
     */
    private val chapterPageSize = 20

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
     * 记录上一次章节列表的总数
     */
    private var oldChapterTotal = 0

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


    //整个页面是否可以点击
    var isNoClick = ObservableField<Boolean>(false)

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
    val chapterAdapter by lazy { HomeDetailChapterAdapter(this) }

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
                } catch (e: Exception) {
                    listenAudio.set(null)
                }
            }
        }
    }

    /**
     * 获取书籍详情信息
     */
    fun intDetailInfo(audioID: String) {
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
                }, onError = {
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
                },
                onError = {
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
                },
                onError = {
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
                }
                else -> {
                    val listenRecord = listenAudio.get()
                    if (listenRecord != null) {
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
                        RouterHelper.createRouter(PlayService::class.java).startPlayActivity(
                            context = context,
                            audioId = audioId.get() ?: "",
                            sortType = mCurSort
                        )
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
    fun getChapterList(page: Int, chapterType: String) {
        launchOnIO {
            repository.chapterList(audioId.get()!!, page, chapterPageSize, AudioSortType.SORT_ASC)
                .checkResult(
                    onSuccess = {
                        processChapterData(it, page, chapterType)
                    }, onError = {
                        processChapterFailure(it, chapterType)
                    })
        }
    }

    /**
     * 处理章节加载失败
     */
    private fun processChapterFailure(msg: String?, chapterType: String) {
        when (chapterType) {
            CHAPTER_NEXT_PAGE -> {
                chapterRefreshStatus.finishLoadMore(false)
                nextChapterPage--
            }
            CHAPTER_PREVIOUS_PAGE -> {
                chapterRefreshStatus.finishRefresh(false)
            }
            CHAPTER_REFRESH_PAGE -> {
                chapterRefreshStatus.finishRefresh(false)
            }
        }
        showTip("$msg", R.color.business_color_ff5e5e)
    }


    /**
     * 处理章节数据
     */
    private fun processChapterData(bean: ChapterListModel, page: Int, chapterType: String) {
        chapterTotal.set(bean.total)
        configChapterPageList()
        chapterMaxPage = ceil(bean.total / chapterPageSize.toDouble()).toInt()

        when (chapterType) {
            CHAPTER_NEXT_PAGE -> {
                nextLoadData(bean)
            }
            CHAPTER_PREVIOUS_PAGE -> {
                previousLoadData(bean)
            }
            CHAPTER_REFRESH_PAGE -> {
                refreshLoadData(page, bean)
            }
        }
    }

    /**
     * 处理加载下一页章节数据
     */
    private fun nextLoadData(bean: ChapterListModel) {
        if (mCurSort == AudioSortType.SORT_ASC) {
            chapterRefreshStatus.finishLoadMore(true)
            bean.list?.let {
                chapterAdapter.addData(it)
            }
            chapterRefreshStatus.setNoHasMore(bean.total == chapterAdapter.data.size || nextChapterPage == chapterMaxPage)
        } else {
            chapterRefreshStatus.finishLoadMore(true)
            bean.list?.let {
                chapterAdapter.addData(it.reversed())
                chapterAdapter.notifyDataSetChanged()
            }
            chapterRefreshStatus.setNoHasMore(previousChapterPage == 1)
        }
    }

    /**
     * 处理加载上一页章节数据
     */
    private fun previousLoadData(bean: ChapterListModel) {
        if (mCurSort == AudioSortType.SORT_ASC) {
            chapterRefreshStatus.finishRefresh(true)
            bean.list?.let {
                chapterAdapter.addData(0, it)
                chapterAdapter.notifyDataSetChanged()
            }
            chapterRefreshStatus.setCanRefresh(previousChapterPage != 1)
        } else {
            chapterRefreshStatus.finishRefresh(true)
            bean.list?.let {
                chapterAdapter.addData(0, it.reversed())
            }
            chapterRefreshStatus.setCanRefresh(previousChapterPage != chapterMaxPage)
        }
    }

    /**
     * 处理加载新数据
     */
    private fun refreshLoadData(page: Int, bean: ChapterListModel) {
        chapterRefreshStatus.finishRefresh(true)
        chapterAdapter.setSort(mCurSort)
        when (mCurSort) {
            AudioSortType.SORT_ASC -> {
                if (chapterAdapter.data.size >= bean.total) {
                    chapterRefreshStatus.setNoHasMore(true)
                }
                chapterRefreshStatus.setCanRefresh(page != 1)
                chapterAdapter.setSortList(mCurSort, bean.list)
            }
            AudioSortType.SORT_DESC -> {
                if (nextChapterPage == 1) {
                    chapterRefreshStatus.setNoHasMore(true)
                }
                chapterAdapter.setSortList(mCurSort, bean.list?.reversed())
                chapterRefreshStatus.setCanRefresh(chapterAdapter.data.size < bean.total)
            }
        }
    }

    /**
     * 章节加载上一页
     */
    fun chapterRefreshData() {

        when (mCurSort) {
            AudioSortType.SORT_ASC -> {
                if (previousChapterPage > 1) {
                    previousChapterPage--
                    getChapterList(previousChapterPage, CHAPTER_PREVIOUS_PAGE)
                } else {
                    chapterRefreshStatus.setCanRefresh(false)
                }
            }
            AudioSortType.SORT_DESC -> {
                if (previousChapterPage < chapterMaxPage) {
                    previousChapterPage++
                    getChapterList(previousChapterPage, CHAPTER_PREVIOUS_PAGE)
                } else {
                    chapterRefreshStatus.setCanRefresh(false)
                }
            }
        }

    }

    /**
     * 章节加载更多
     */
    fun chapterLoadData() {
        when (mCurSort) {
            AudioSortType.SORT_ASC -> {
                if (nextChapterPage < chapterMaxPage) {
                    nextChapterPage++
                    getChapterList(nextChapterPage, CHAPTER_NEXT_PAGE)
                } else {
                    chapterRefreshStatus.setNoHasMore(true)
                }
            }
            AudioSortType.SORT_DESC -> {
                if (nextChapterPage > 1) {
                    nextChapterPage--
                    getChapterList(nextChapterPage, CHAPTER_NEXT_PAGE)
                } else {
                    chapterRefreshStatus.setNoHasMore(true)
                }
            }
        }
    }

    /**
     * 排序点击事件
     */
    fun clickSort() {

        when (mCurSort) {
            AudioSortType.SORT_ASC -> {
                if (previousChapterPage == 1) {
                    chapterRefreshStatus.setNoHasMore(true)
                } else {
                    chapterRefreshStatus.setNoHasMore(false)
                    chapterRefreshStatus.setResetNoMoreData(true)
                }
                chapterRefreshStatus.setCanRefresh(previousChapterPage < chapterMaxPage)
                mCurSort = AudioSortType.SORT_DESC
            }
            AudioSortType.SORT_DESC -> {
                if (previousChapterPage == 1) {
                    chapterRefreshStatus.setNoHasMore(true)
                } else {
                    chapterRefreshStatus.setNoHasMore(false)
                    chapterRefreshStatus.setResetNoMoreData(true)
                }
                chapterRefreshStatus.setCanRefresh(nextChapterPage != 1)
                mCurSort = AudioSortType.SORT_ASC
            }
        }

        val next = nextChapterPage
        nextChapterPage = previousChapterPage
        previousChapterPage = next

        chapterAdapter.setSort(mCurSort)
        configChapterPageList()

    }


    /**
     * 章节分页item点击事件
     */
    fun itemClickSelectChapter(page: Int) {
        nextChapterPage = page
        previousChapterPage = page
        hideOr.set(!hideOr.get())
        chapterRefreshStatus.setResetNoMoreData(true)
        getChapterList(page, CHAPTER_REFRESH_PAGE)
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
                onError = {
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
                onError = {
                    showContentView()
                    DLog.i("----->", "评论点赞:$it")
                    showTip("$it", R.color.business_color_ff5e5e)
                }
            )
        }
    }


    /**
     * 章节 item 点击事件
     */
    fun itemClick(context: Context, bean: DownloadChapter) {
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

                }, onError = {
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
     * 处理评论数据
     */
    private fun processCommentData(bean: HomeCommentBean) {
        commentRefreshStateMode.finishLoadMore(true)
        if (commentPage == 1) {
            homeDetailCommentAdapter.setList(bean.list_comment)
        } else {
            homeDetailCommentAdapter.addData(bean.list_comment)
        }

        if (homeDetailCommentAdapter.data.size >= bean.total || bean.list_comment.size < mPageSize) {
            commentRefreshStateMode.setNoHasMore(true)
        } else {
            ++commentPage
        }

    }

    /**
     * 获取章节分页信息
     */
    private fun configChapterPageList() {
        if (chapterTotal.get()!! <= 0 || chapterTotal.get() == oldChapterTotal) {
            return
        }
        oldChapterTotal
        val totalCount = chapterTotal.get()!!
        oldChapterTotal = totalCount

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
                        unAttentionAnchor(followId!!)
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
        getActivity(context)?.let {
            Share2.Builder(it)
                .setContentType(ShareContentType.TEXT)
                .setTitle("分享测试")
                .setTextContent("http://www.baidu.com")
                .build()
                .shareBySystem()
        }
    }


    /**
     * 收藏点击事件
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
                contentText = context.getString(R.string.business_favorites_success_content)
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
//            showToast(context.getString(R.string.home_subscribe_success_tip))
            showTip(context.getString(R.string.business_subscribe_success_tip))
        }
        IS_FIRST_SUBSCRIBE.putMMKV(false)
    }


}