package com.rm.module_home.viewmodel

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableField
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.rm.baselisten.adapter.single.CommonBindAdapter
import com.rm.baselisten.adapter.single.CommonBindVMAdapter
import com.rm.baselisten.net.checkResult
import com.rm.baselisten.util.DLog
import com.rm.baselisten.util.getBooleanMMKV
import com.rm.baselisten.util.putMMKV
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.business_lib.IS_FIRST_SUBSCRIBE
import com.rm.business_lib.base.dialog.CustomTipsFragmentDialog
import com.rm.business_lib.bean.*
import com.rm.business_lib.isLogin
import com.rm.business_lib.wedgit.smartrefresh.model.SmartRefreshLayoutStatusModel
import com.rm.component_comm.listen.ListenService
import com.rm.component_comm.login.LoginService
import com.rm.component_comm.router.RouterHelper
import com.rm.module_home.BR
import com.rm.module_home.R
import com.rm.module_home.databinding.HomeDetailHeaderBinding
import com.rm.module_home.model.home.detail.CommentList
import com.rm.module_home.model.home.detail.HomeCommentBean
import com.rm.module_home.repository.HomeRepository
import com.rm.module_home.util.HomeCommentDialogHelper


class HomeDetailViewModel1(private val repository: HomeRepository) : BaseVMViewModel() {
    //每次加载多少数据
    private val mPageSize = 12

    //评论当前的页码
    private var commentPage = 1

    //评论加载更多
    val commentRefreshStateMode = SmartRefreshLayoutStatusModel()

    //章节当前的页码
    private var chapterPage = 1

    //章节加载更多
    val chapterRefreshStateMode = SmartRefreshLayoutStatusModel()

    //听单详情
    val detailInfoData = ObservableField<HomeDetailBean>()

    //听单id
    val audioId = ObservableField<String>()

    //是否关注
    val isAttention = ObservableField<Boolean>(false)

    //是否订阅
    val isSubscribed = ObservableField<Boolean>(false)

    // 章节的适配器
    val chapterAdapter by lazy {
        CommonBindVMAdapter(
            this,
            mutableListOf<ChapterList>(),
            R.layout.home_item_detail_chapter1,
            BR.chapterViewModel,
            BR.chapterItem
        )
    }

    //标签adapter
    val homeDetailTagsAdapter by lazy {
        CommonBindAdapter(
            mutableListOf<DetailTags>(),
            R.layout.home_item_book_label,
            BR.detailTags
        )
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

    /**
     * 创建头部详细信息
     */
    fun createHeader(view: RecyclerView) {
        DataBindingUtil.inflate<HomeDetailHeaderBinding>(
            LayoutInflater.from(view.context),
            R.layout.home_detail_header,
            view,
            false
        ).apply {
            homeDetailCommentAdapter.addHeaderView(this.root)
            setVariable(BR.viewModel, this@HomeDetailViewModel1)
        }

    }

    /**
     * 获取书籍详情信息
     */
    fun getDetailInfo(audioID: String) {
        launchOnUI {
            repository.getDetailInfo(audioID).checkResult(
                onSuccess = {
                    processDetailInfo(it)
                }, onError = {
                    showContentView()
                }
            )
        }
    }

    /**
     * 处理书籍数据
     */
    private fun processDetailInfo(bean: HomeDetailBean) {
        showContentView()
        detailInfoData.set(bean)
        isAttention.set(bean.list.anchor.status)
        homeDetailTagsAdapter.setList(bean.list.tags)
    }

    /**
     * 订阅
     */
    private fun subscribe(context: Context, audioId: String) {
        launchOnIO {
            repository.subscribe(audioId).checkResult(
                onSuccess = {
                    isSubscribed.set(true)
                    DLog.i("------->", "订阅成功")
                    subscribeSuccess(context)
                },
                onError = {
                    showContentView()
                    DLog.i("------->", "订阅失败  $it")
                }
            )
        }
    }

    /**
     * 取消订阅
     */
    private fun unSubscribe(audioId: String) {
        launchOnIO {
            repository.subscribe(audioId).checkResult(
                onSuccess = {
                    isSubscribed.set(false)
                    showToast("取消订阅成功")
                },
                onError = {
                    showContentView()
                    DLog.i("------->", "取消订阅  $it")
                }
            )
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
                }, onError = {
                    Log.i("commentList", it.toString())
                    commentRefreshStateMode.finishLoadMore(false)
                }
            )
        }
    }

    /**
     * 获取章节列表
     */
    fun getChapterData(audioId: String) {
        launchOnIO {
            repository.chapterList(audioId, chapterPage, mPageSize, "asc").checkResult(
                onSuccess = {
                    processChapterData(it)
                }, onError = {
                    chapterRefreshStateMode.finishLoadMore(false)
                })
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
     * 处理章节数据
     */
    private fun processChapterData(bean: AudioChapterListModel) {
        chapterRefreshStateMode.finishLoadMore(true)
        if (chapterPage == 1) {
            chapterAdapter.setList(bean.list)
        } else {
            bean.list?.let { chapterAdapter.addData(it) }
        }
//        chapterRefreshStateMode.setHasMore(bean.list?.size ?: 0 > 0)
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
     * 评论加载更多
     */
    fun commentLoadMoreData() {
        ++commentPage
        audioId.get()?.let {
            getCommentList(it)
        }
    }

    /**
     * 章节加载更多
     */
    fun chapterLoadMoreData() {
        ++chapterPage
        audioId.get()?.let {
            DLog.i("------>", "chapterLoadMoreData")
            getChapterData(it)
        }
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
                    IS_FIRST_SUBSCRIBE.putMMKV(false)
                    dismiss()
                }
                customView =
                    ImageView(activity).apply { setImageResource(R.mipmap.home_ic_launcher) }
            }.show(activity)
        } else {
            showToast(context.getString(R.string.home_subscribe_success_tip))
        }
    }

    /**
     * 章节item点击事件
     */
    fun chapterItemClick(context: Context, bean: ChapterList) {
        DLog.i("-------->", "章节item点击事件")
    }

    /**
     * 全部播放
     */
    fun clickPlayAll() {
        DLog.i("-------->", "全部播放点击事件")
    }

    /**
     * 排序
     */
    fun clickSort() {
        DLog.i("-------->", "排序点击事件")
    }

    /**
     * 下载
     */
    fun clickDownload() {
        DLog.i("-------->", "下载点击事件")
    }


    /**
     * 添加到听单点击事件
     */
    fun clickCollectionFun(context: Context) {
        getActivity(context)?.let {
            if (isLogin.get()) {
                quicklyLogin(it)
            } else {
                audioId.get()?.let { audioId ->
                    RouterHelper.createRouter(ListenService::class.java)
                        .showMySheetListDialog(this, it, audioId)
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
     * 评论点击事件
     */
    fun clickCommentFun(context: Context) {
        getActivity(context)?.let {
            HomeCommentDialogHelper(this, it).showDialog()
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
                if (isAttention.get() == true) {
                    unAttentionAnchor(followId)
                } else {
                    attentionAnchor(followId)
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
                getDetailInfo(audioId.get()!!)
            })
    }


}