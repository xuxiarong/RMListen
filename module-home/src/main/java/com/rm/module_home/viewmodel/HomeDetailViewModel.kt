package com.rm.module_home.viewmodel

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.rm.baselisten.adapter.single.CommonBindAdapter
import com.rm.baselisten.adapter.single.CommonBindVMAdapter
import com.rm.baselisten.net.checkResult
import com.rm.baselisten.util.DLog
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.business_lib.bean.ChapterList
import com.rm.business_lib.bean.DataStr
import com.rm.business_lib.bean.DetailTags
import com.rm.business_lib.bean.HomeDetailBean
import com.rm.business_lib.db.download.DownloadAudio
import com.rm.business_lib.isLogin
import com.rm.business_lib.wedgit.smartrefresh.model.SmartRefreshLayoutStatusModel
import com.rm.component_comm.download.DownloadService
import com.rm.component_comm.listen.ListenService
import com.rm.component_comm.login.LoginService
import com.rm.component_comm.play.PlayService
import com.rm.component_comm.router.RouterHelper
import com.rm.module_home.BR
import com.rm.module_home.R
import com.rm.module_home.databinding.HomeDetailHeaderBinding
import com.rm.module_home.model.home.detail.CommentList
import com.rm.module_home.model.home.detail.HomeCommentBean
import com.rm.module_home.repository.HomeRepository
import com.rm.module_home.util.HomeCommentDialogHelper
import com.rm.module_play.enum.Jump


class HomeDetailViewModel(private val repository: HomeRepository) : BaseVMViewModel() {

    //当前加载的页码
    private var mPage = 1

    //每次加载数据的条数
    private val mPageSize = 12

    private val chapterPageSize = 5

    //评论当前的页码
    private var commentPage = 1

    //上一页页码
    private var upTrackPage = 0

    //下一页页码
    private var curTrackPage = 1

    //章节分页显示与隐藏
    var hideOr = ObservableBoolean(false)

    //主播是否关注
    var isAttention = ObservableBoolean(false)

    //是否订阅
    val isSubscribed = ObservableField<Boolean>(false)

    //评论加载更多
    val commentRefreshStateMode = SmartRefreshLayoutStatusModel()


    var audioId = ObservableField<String>("")
    var sort = ObservableField<String>()
    var total = ObservableField<String>()
    var showStatus = ObservableField<String>()
    val actionControl = MutableLiveData<String>()
    private val errorTips = ObservableField<String>()

    var detailInfoData = ObservableField<HomeDetailBean>()

    val refreshStatusModel = SmartRefreshLayoutStatusModel()

    fun getAttentionStr(isAttention: Boolean): String {
        return if (isAttention) {
            "已关注"
        } else {
            "关注"
        }
    }

    /**
     * 评论加载更多
     */
    fun commentLoadMoreData() {
        ++commentPage
        audioId.get()?.let {
            getCommentList(it)
        }
    }

    //评论dapper
    val homeDetailCommentAdapter by lazy {
        CommonBindVMAdapter<CommentList>(
            this,
            mutableListOf(),
            R.layout.home_detail_item_comment,
            BR.commentViewModel,
            BR.commentItem
        )
    }

    //标签adapter
    val homeDetailTagsAdapter by lazy {
        CommonBindAdapter(
            mutableListOf<DetailTags>(),
            R.layout.home_item_book_label, BR.detailTags
        )
    }

    /**
     * 分页适配器
     */
    val chapterAnthologyAdapter by lazy {
        CommonBindVMAdapter(
            this, mutableListOf<DataStr>(),
            R.layout.home_chapter_item_anthology,
            BR.click,
            BR.item
        )
    }

    /**
     * 章节的适配器
     */
    val chapterAdapter by lazy {
        CommonBindVMAdapter(
            this,
            mutableListOf<ChapterList>(),
            R.layout.home_item_detail_chapter,
            BR.chapterclick,
            BR.DetailChapterViewModel

        )
    }


    /**
     * 加载上一页
     */
    fun refreshData() {
        getTrackList(true)
    }

    /**
     * 加载更多
     */
    fun loadData() {
        getTrackList(false)
    }

    /**
     * 获取书籍详情信息
     */
    fun intDetailInfo(audioID: String) {
        launchOnUI {
            repository.getDetailInfo(audioID).checkResult(
                onSuccess = {
                    showContentView()
                    detailInfoData.set(it)
                    isAttention.set(it.list.anchor.status)

                    homeDetailTagsAdapter.setList(it.list.tags)
                }, onError = {
                    showContentView()
                    errorTips.set(it)
                    showToast(it.toString())
                }
            )
        }
        showStatus()
    }

    /**
     * 订阅
     */
    private fun subscribe(audioID: String) {
        showLoading()
        launchOnIO {
            repository.subscribe(audioID).checkResult(
                onSuccess = {
                    showContentView()
                    DLog.i("------->", "订阅成功")
                },
                onError = {
                    showContentView()
                    DLog.i("------->", "订阅失败  $it")
                }
            )
        }
    }

    /**
     * 跳转到播放器页面
     */
    fun toPlayPage() {
        actionControl.postValue("toPlayPage")
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

    //独立刷新事件
    fun onRefresh() {
        launchOnIO {
            repository.chapterList(audioId.get()!!, mPage, chapterPageSize, sort.get()!!)
                .checkResult(
                    onSuccess = {
                        showContentView()
                        total.set("共" + it.total + "集")
                        setPager(it.total)
                        it.list.let { list -> chapterAdapter.setList(list) }
                    }, onError = {
                        showContentView()
                        errorTips.set(it)
                    })
        }
    }

    /**
     * 排序
     */
    fun getTrackList(mSort: String) {
        if (mSort != sort.get()) {
            //curTrackPage = 1
            sort.set(mSort)
        }
        launchOnIO {
            repository.chapterList(audioId.get()!!, mPage, chapterPageSize, sort.get()!!)
                .checkResult(
                    onSuccess = {
                        showContentView()
                        setPager(it.total)
                        //upTrackPage = 0
                        //curTrackPage ++
                        it.list.let { list -> chapterAdapter.setList(list) }
                    }, onError = {
                        showContentView()
                        errorTips.set(it)
                    })
        }
    }

    /**
     * 2 上拉，下拉更多
     */
    private fun getTrackList(isUp: Boolean) {
        val page: Int
        if (isUp) {
            page = upTrackPage
            if (0 == page) {
                refreshStatusModel.finishRefresh(false)
            }
        } else {
            page = curTrackPage
        }
        launchOnIO {
            repository.chapterList(audioId.get()!!, page, chapterPageSize, sort.get()!!)
                .checkResult(
                    onSuccess = {
                        showContentView()
                        if (isUp) {
                            upTrackPage--
                            it.list.let { list -> chapterAdapter.addData(0, list) }
                            refreshStatusModel.finishRefresh(true)
                        } else {
                            curTrackPage++
                            it.list.let { list -> chapterAdapter.addData(list) }
                            refreshStatusModel.finishLoadMore(true)
                        }
                        refreshStatusModel.setHasMore(it.list.size >= chapterPageSize)
                    }, onError = {
                        showContentView()
                        refreshStatusModel.finishRefresh(false)
                        refreshStatusModel.finishLoadMore(false)
                        errorTips.set(it)

                    })
        }
    }

    /**
     * 分页查询
     */
    fun getTrackList(mPage: Int) {
        DLog.e("mPage", "" + mPage)

        launchOnIO {
            repository.chapterList(audioId.get()!!, mPage, chapterPageSize, sort.get()!!)
                .checkResult(
                    onSuccess = {
                        showContentView()
                        //当前为1，上一页为0，下一页为2
                        upTrackPage = mPage
                        curTrackPage = mPage

                        upTrackPage--
                        curTrackPage++

                        it.list.let { list -> chapterAdapter.setList(list) }
                        refreshStatusModel.setHasMore(it.list.size >= chapterPageSize)
                        hideOr.set(false)

                    }, onError = {
                        showContentView()
                        errorTips.set(it)
                    })
        }

    }

    /**
     * 点赞
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
     * 评论点赞
     */
    private fun likeComment(bean: CommentList) {
        launchOnIO {
            repository.homeLikeComment(bean.id.toString()).checkResult(
                onSuccess = {
                    val indexOf = homeDetailCommentAdapter.data.indexOf(bean)
                    bean.is_liked = true
                    bean.likes = bean.likes + 1
                    homeDetailCommentAdapter.notifyItemChanged(indexOf)
                },
                onError = {
                    DLog.i("----->", "评论点赞:$it")
                })
        }
    }


    /**
     * 取消评论点赞
     */
    private fun unLikeComment(bean: CommentList) {
        launchOnIO {
            repository.homeLikeComment(bean.id.toString()).checkResult(
                onSuccess = {
                    val indexOf = homeDetailCommentAdapter.data.indexOf(bean)
                    bean.is_liked = false
                    bean.likes = bean.likes - 1
                    homeDetailCommentAdapter.notifyItemChanged(indexOf)
                },
                onError = {
                    DLog.i("----->", "评论点赞:$it")
                }
            )
        }
    }


    /**
     * 章节 item 点击事件
     */
    fun itemClick(context: Context, bean: ChapterList) {
        playService.toPlayPage(
            context, bean, Jump.CHAPTER.from
        )
    }

    //播放器路由
    private val playService by lazy {
        RouterHelper.createRouter(PlayService::class.java)
    }

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
     * 评论列表
     */
    fun getCommentList(audio_id: String) {
        launchOnUI {
            repository.getCommentInfo(audio_id, commentPage, mPageSize).checkResult(
                onSuccess = {
                    processCommentData(it)
                    Log.i("commentList", it.toString())
                }, onError = {
                    errorTips.set(it)
                    Log.i("commentList", it.toString())
                }
            )
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
        commentRefreshStateMode.setHasMore(bean.list_comment.size > 0)
    }

    /**
     * 快捷登陆
     */
    private fun toLogin(activity: FragmentActivity) {
        RouterHelper.createRouter(LoginService::class.java).quicklyLogin(this, activity)
    }


    /**
     * 收藏点击事件
     */
    fun clickCollectionFun(context: Context) {
        getActivity(context)?.let {
            if (!isLogin.get()) {
                toLogin(it)
                return
            }
            RouterHelper.createRouter(ListenService::class.java)
                .showMySheetListDialog(this, it, audioId.get()!!)
        }

    }

    /**
     * 订阅点击事件
     */
    fun clickSubscribeFun(context: Context) {
        getActivity(context)?.let {
            if (!isLogin.get()) {
                toLogin(it)
                return
            }
            subscribe(audioId.get()!!)
        }
    }

    /**
     * 关注主播点击事件
     */
    fun clickSubMemberFun() {

    }

    /**
     * 获取分页列表
     */
    private fun setPager(totalCount: Int) {

        val anthologyList = ArrayList<DataStr>()

        val size = totalCount / chapterPageSize

        if (sort.get().equals("desc")) {
            for (i in 0 until size) {
                anthologyList.add(
                    DataStr(
                        (totalCount - i * chapterPageSize).toString() + "~" + (totalCount - (i + 1) * chapterPageSize + 1),
                        i + 1
                    )
                )
            }
            if (size != 0) {
                anthologyList.add(DataStr("${totalCount - size * chapterPageSize}-1", size + 1))
            }
        } else {
            for (i in 0 until size) {
                anthologyList.add(
                    DataStr(
                        "${i * chapterPageSize + 1}-" + (i + 1) * chapterPageSize,
                        i + 1
                    )
                )
            }
            if (size != 0) {
                anthologyList.add(DataStr("${size * chapterPageSize + 1}", size + 1))
            }
        }
        chapterAnthologyAdapter.setList(anthologyList)
    }

    fun startDownloadChapterActivity(context: Context) {
        val homeDetailModel = detailInfoData.get()
        if (homeDetailModel != null) {
            val createRouter = RouterHelper.createRouter(DownloadService::class.java)
            createRouter.startDownloadChapterSelectionActivity(
                context,
                DownloadAudio(
                    homeDetailModel.list.audio_id.toLong(),
                    homeDetailModel.list.audio_name,
                    homeDetailModel.list.author,
                    homeDetailModel.list.audio_cover_url,
                    homeDetailModel.list.status,
                    homeDetailModel.list.last_sequence
                )
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
                    showToast("关注成功")
                },
                onError = {
                    showContentView()
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
                    showToast("取消关注成功")
                },
                onError = {
                    showContentView()
                })
        }
    }


    /**
     * 关注点击事件
     */
    fun clickAttentionFun(context: Context, followId: String) {
        getActivity(context)?.let {
            if (!isLogin.get()) {
                quicklyLogin(it)
            } else {
                if (isAttention.get()) {
                    unAttentionAnchor(followId)
                } else {
                    attentionAnchor(followId)
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
                    }.showDialog()
                }

            } else {
                quicklyLogin(it)
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
}