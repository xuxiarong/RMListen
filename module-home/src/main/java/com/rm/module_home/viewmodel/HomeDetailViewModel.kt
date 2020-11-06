package com.rm.module_home.viewmodel

import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.rm.baselisten.adapter.single.CommonBindAdapter
import com.rm.baselisten.adapter.single.CommonBindVMAdapter
import com.rm.baselisten.mvvm.BaseActivity
import com.rm.baselisten.net.checkResult
import com.rm.baselisten.util.DLog
import com.rm.baselisten.util.getBooleanMMKV
import com.rm.baselisten.util.putMMKV
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.business_lib.AudioSortType
import com.rm.business_lib.IS_FIRST_SUBSCRIBE
import com.rm.business_lib.base.dialog.CustomTipsFragmentDialog
import com.rm.business_lib.bean.*
import com.rm.business_lib.db.download.DownloadAudio
import com.rm.business_lib.isLogin
import com.rm.business_lib.loginUser
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
import com.rm.module_play.enum.Jump
import kotlin.math.ceil


class HomeDetailViewModel(private val repository: HomeRepository) : BaseVMViewModel() {

    /**
     * 章节当前加载的页码
     */
    private var chapterPage = 1

    /**
     * 章节上一次加载的页码
     */
    private var oldChapterPage = -1

    /**
     * 章节分页加载的数量
     */
    private val chapterPageSize = 20

    /**
     * 评论当前的页码
     */
    private var commentPage = 1

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
     * 当前的排序类型
     */
    var mCurSort = AudioSortType.SORT_ASC

    /**
     * 章节总数
     */
    var chapterTotal = ObservableField(0)

    var oldChapterTotal = 0

    /**
     * 书籍状态
     */
    var showStatus = ObservableField<String>()

    /**
     * 书籍信息
     */
    var detailInfoData = ObservableField<HomeDetailBean>()

    /**
     * 评论数量
     */
    var commentTotal = ObservableField(0)

    /**
     * 章节列表
     */
    private val chapterList = mutableListOf<ChapterList>()

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
        ++commentPage
        audioId.get()?.let {
            getCommentList(it)
        }
    }


    /**
     * 章节加载上一页
     */
    fun chapterRefreshData() {
        chapterPage--
        getChapterList()
    }

    /**
     * 章节加载更多
     */
    fun chapterLoadData() {
        chapterPage++
        getChapterList()
    }

    /**
     * 获取书籍详情信息
     */
    fun intDetailInfo(audioID: String) {
        launchOnUI {
            repository.getDetailInfo(audioID).checkResult(
                onSuccess = {
                    showContentView()
                    //如果主播id与当前的用户id一致则隐藏关注按钮
                    loginUser.get()?.let { user ->
                        attentionVisibility.set(user.id != it.list.member_id)
                    }
                    detailInfoData.set(it)
                    isAttention.set(it.list.anchor.status)
                    anchorId.set(it.list.anchor_id)
                    homeDetailTagsAdapter.setList(it.list.tags)
                }, onError = {
                    if (it?.contains("下架") == true || it?.contains("违规") == true) {
                        finish()
                    }
                    showToast(it.toString())
                }
            )
        }
        showStatus()
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
        launchOnIO {
            repository.unSubscribe(audioId).checkResult(
                onSuccess = {
                    isSubscribed.set(false)
//                    showToast("取消订阅成功")
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
        detailInfoData.get()?.let {
            RouterHelper.createRouter(PlayService::class.java).toPlayPage(
                context = context,
                bean = DetailBookBean(
                    audio_id = it.list.audio_id,
                    audio_name = it.list.audio_name,
                    original_name = it.list.original_name,
                    author = it.list.author,
                    audio_cover_url = it.list.audio_cover_url,
                    anchor = it.list.anchor
                ), from = Jump.DETAILSBOOK.from,
                sortType = mCurSort
            )
        }
    }

    /**
     * 书籍状态
     */
    private fun showStatus() {
        when (detailInfoData.get()?.list?.progress) {
            0 -> showStatus.set("未开播")
            1 -> showStatus.set("已连载" + detailInfoData.get()?.list?.last_sequence + "集")
            else -> showStatus.set("已完结")
        }
    }

    /**
     * 章节列表
     * page:第几页
     * page_size:每页数量
     * audio_id:音频id
     * sort：排序(asc,desc)
     */
    fun getChapterList() {
        DLog.i("----------->", "$chapterPage    $mCurSort")
        launchOnIO {
            repository.chapterList(audioId.get()!!, chapterPage, chapterPageSize, mCurSort)
                .checkResult(
                    onSuccess = {
                        processChapterData(it)
                    }, onError = {
                        showContentView()
                        chapterRefreshStatus.finishRefresh(false)
                        chapterRefreshStatus.finishLoadMore(false)
                        showTip("$it", R.color.business_color_ff5e5e)
                    })
        }
    }

    /**
     * 处理章节数据
     */
    private fun processChapterData(bean: AudioChapterListModel) {
        showContentView()
        chapterTotal.set(bean.total)
        configChapterPageList()

        //向上取整
        val ceil = ceil(bean.total / chapterPageSize.toFloat()).toInt()

        chapterRefreshStatus.setCanRefresh(chapterPage != 1)
        if (chapterPage == ceil) {
            chapterRefreshStatus.setHasMore(false)
        } else {
            chapterRefreshStatus.setHasMore(true)
        }

        if (oldChapterPage > chapterPage) {
            bean.list?.let { chapterList.addAll(0, it) }
            chapterRefreshStatus.finishRefresh(true)
        } else {
            chapterRefreshStatus.finishLoadMore(true)
            bean.list?.let { chapterList.addAll(it) }
        }
        chapterAdapter.setList(chapterList)
        oldChapterPage = chapterPage

    }

    /**
     * 评论点赞
     */
    private fun likeComment(bean: CommentList) {
        launchOnIO {
            repository.homeLikeComment(bean.id.toString()).checkResult(
                onSuccess = {
                    val indexOf = homeDetailCommentAdapter.data.indexOf(bean)
                    bean.is_liked = true
                    bean.likes = bean.likes + 1
                    //记得加上头部的个数，不然会报错  https://github.com/CymChad/BaseRecyclerViewAdapterHelper/issues/871
                    val headerLayoutCount = homeDetailCommentAdapter.headerLayoutCount
                    homeDetailCommentAdapter.notifyItemChanged(indexOf + headerLayoutCount)
                },
                onError = {
                    DLog.i("----->", "评论点赞:$it")
                    showTip("$it", R.color.business_color_ff5e5e)
                })
        }
    }


    /**
     * 取消评论点赞
     */
    private fun unLikeComment(bean: CommentList) {
        launchOnIO {
            repository.homeUnLikeComment(bean.id.toString()).checkResult(
                onSuccess = {
                    val indexOf = homeDetailCommentAdapter.data.indexOf(bean)
                    bean.is_liked = false
                    bean.likes = bean.likes - 1
                    val headerLayoutCount = homeDetailCommentAdapter.headerLayoutCount
                    homeDetailCommentAdapter.notifyItemChanged(indexOf + headerLayoutCount)
                },
                onError = {
                    DLog.i("----->", "评论点赞:$it")
                    showTip("$it", R.color.business_color_ff5e5e)
                }
            )
        }
    }


    /**
     * 章节 item 点击事件
     */
    fun itemClick(context: Context, bean: ChapterList) {
        RouterHelper.createRouter(PlayService::class.java).toPlayPage(
            context, bean, Jump.CHAPTER.from, mCurSort
        )
    }


    /**
     * 评论列表
     */
    fun getCommentList(audio_id: String) {
        launchOnUI {
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
        commentRefreshStateMode.setHasMore(bean.list_comment.size >= mPageSize)
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
            createRouter.startDownloadChapterSelectionActivity(
                context,
                downloadAudio
            )
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
        chapterPage = 1
        chapterList.clear()
        getChapterList()
    }

    /**
     * 章节分页item点击事件
     */
    fun itemClickSelectChapter(page: Int) {
        chapterPage = page
        chapterList.clear()
        hideOr.set(!hideOr.get())
        getChapterList()
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
                    HomeCommentDialogHelper(this, it, audioId) {
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
        if (isLogin.get()) {
            detailInfoData.get()?.let {
                RouterHelper.createRouter(MineService::class.java)
                    .toMineMember(context, it.list.anchor_id)
            }
        } else {
            getActivity(context)?.let { quicklyLogin(it) }
        }
    }

    /**
     * 评论头像点击事件
     */
    fun clickCommentMemberFun(context: Context, memberId: String?) {
        if (TextUtils.isEmpty(memberId)) {
            return
        }
        if (isLogin.get()) {
            RouterHelper.createRouter(MineService::class.java)
                .toMineMember(context, memberId!!)
        } else {
            getActivity(context)?.let { quicklyLogin(it) }
        }
    }

    /**
     * 分享
     */
    fun clickShare() {
        val canRefresh = chapterRefreshStatus.canRefresh.get()!!
        chapterRefreshStatus.setCanRefresh(!canRefresh)
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
                    .showMySheetListDialog(this, it, audioId.get()!!)
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
                titleText = context.getString(R.string.home_favorites_success)
                contentText = context.getString(R.string.home_favorites_success_content)
                leftBtnText = context.getString(R.string.home_know)
                rightBtnText = context.getString(R.string.home_goto_look)
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
//            showToast(context.getString(R.string.home_subscribe_success_tip))
            showTip(context.getString(R.string.home_subscribe_success_tip))
        }
        IS_FIRST_SUBSCRIBE.putMMKV(false)
    }


}