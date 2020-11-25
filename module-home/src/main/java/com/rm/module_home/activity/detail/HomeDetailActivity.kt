package com.rm.module_home.activity.detail

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.Observable
import androidx.recyclerview.widget.RecyclerView
import com.rm.baselisten.binding.bindVerticalLayout
import com.rm.baselisten.utilExt.getStateHeight
import com.rm.component_comm.activity.ComponentShowPlayActivity
import com.rm.module_home.BR
import com.rm.module_home.R
import com.rm.module_home.databinding.HomeActivityDetailMainBinding
import com.rm.module_home.viewmodel.HomeDetailViewModel
import com.rm.module_home.widget.HomeDetailInterceptLayout

/**
 * 书籍详情
 *  //1、需添加书籍下架的toast提示，然后finish掉详情页
 */
class HomeDetailActivity :
    ComponentShowPlayActivity<HomeActivityDetailMainBinding, HomeDetailViewModel>() {
    override fun getLayoutId(): Int = R.layout.home_activity_detail_main

    override fun initModelBrId() = BR.viewModel


    companion object {
        const val AUDIO_ID = "audioID"
        fun startActivity(context: Context, audioID: String) {
            val intent = Intent(context, HomeDetailActivity::class.java)
            intent.putExtra(AUDIO_ID, audioID)
            context.startActivity(intent)
        }
    }

    private val commentFootView by lazy {
        LayoutInflater.from(this).inflate(R.layout.business_foot_view, null)
    }

    private val chapterFootView by lazy {
        LayoutInflater.from(this).inflate(R.layout.business_foot_view, null)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent?.getStringExtra(AUDIO_ID)?.let {
            mViewModel.audioId.set(it)
            mViewModel.intDetailInfo(it)

            mViewModel.chapterRefreshStatus.setCanRefresh(false)
            mViewModel.chapterRefreshStatus.setNoHasMore(false)
            mViewModel.chapterRefreshStatus.setResetNoMoreData(true)
            mViewModel.nextChapterPage = 1
            mViewModel.previousChapterPage = 1
            mViewModel.queryAudioListenRecord()
            mViewModel.getChapterList(1, HomeDetailViewModel.CHAPTER_REFRESH_PAGE) //初始化章节列表

            mViewModel.commentPage = 1
            mViewModel.commentRefreshStateMode.setNoHasMore(false)
            mViewModel.commentRefreshStateMode.setResetNoMoreData(true)
            mViewModel.getCommentList(it)
        }
    }

    override fun initView() {
        super.initView()
        setTransparentStatusBar()

        recycleScrollListener()

        mDataBind.homeDetailCommentRecycleView.apply {
            bindVerticalLayout(mViewModel.homeDetailCommentAdapter)
            mViewModel.createHeader(this)
        }

        (mDataBind.homeDetailTitleCl.layoutParams as ViewGroup.MarginLayoutParams).apply {
            //动态获取状态栏的高度,并设置标题栏的topMargin
            topMargin = getStateHeight(this@HomeDetailActivity)
        }

//        if (Intent.ACTION_VIEW == intent.action && intent.data != null) {
//            val data = intent.data
//            data?.getQueryParameter("audioId")
//        }
//com.rm.listen.home://home_detail?audioId="123445667"
        intent?.getStringExtra(AUDIO_ID)?.let {
            mViewModel.audioId.set(it)
            mViewModel.intDetailInfo(it)

            mViewModel.chapterRefreshStatus.setCanRefresh(false)
            mViewModel.chapterRefreshStatus.setResetNoMoreData(true)
            mViewModel.chapterRefreshStatus.setNoHasMore(false)

            mViewModel.commentRefreshStateMode.setNoHasMore(false)
            mViewModel.queryAudioListenRecord()
            mViewModel.getChapterList(1, HomeDetailViewModel.CHAPTER_REFRESH_PAGE) //初始化章节列表
            mViewModel.getCommentList(it)
        }

        mDataBind.homeDetailChapterHeader.post {
            //获取章节头部的高度
            val measuredHeight = mDataBind.homeDetailChapterHeader.measuredHeight
            val params =
                mDataBind.homeDetailCommentRefresh.layoutParams as ConstraintLayout.LayoutParams

            //评论列表设置bottomMargin使其不被挡住
            params.bottomMargin = measuredHeight + 20
            mDataBind.homeDetailCommentRefresh.layoutParams = params
        }

        //监听章节列表停留的位置，如果在顶部则现实title
        mDataBind.homeDetailInterceptLayout.setScrollTopListener(object :
            HomeDetailInterceptLayout.ScrollTopListener {
            override fun toTop(isTop: Boolean) {
                if (isTop) {
                    mDataBind.homeDetailTitle.visibility = View.VISIBLE
                    mDataBind.homeDetailBlur.alpha = 1f
                } else {
                    if (!titleIsVisible) {
                        mDataBind.homeDetailBlur.alpha = oldAlpha
                        mDataBind.homeDetailTitle.visibility = View.INVISIBLE
                    }
                }
            }
        })
    }

    private var titleIsVisible = false
    private var oldAlpha = 0f

    /**
     * recyclerView滑动监听
     */
    private fun recycleScrollListener() {
        mDataBind.homeDetailCommentRecycleView.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            private var totalDy = 0
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val top = mViewModel.mDataBinding?.homeDetailIcon?.top ?: 0
                val height = mViewModel.mDataBinding?.homeDetailIcon?.height ?: 0
                totalDy += dy
                if (totalDy > 0) {
                    val alpha = if (totalDy.toFloat() / (top + height) > 1f) {
                        1f
                    } else {
                        totalDy.toFloat() / (top + height)
                    }
                    oldAlpha = alpha
                    mDataBind.homeDetailBlur.alpha = alpha
                    mDataBind.homeDetailTitle.visibility = if (alpha > 0.9) {
                        titleIsVisible = true
                        View.VISIBLE
                    } else {
                        titleIsVisible = false
                        View.INVISIBLE
                    }
                }
            }
        })
    }


    override fun startObserve() {
        mViewModel.chapterRefreshStatus.noMoreData.addOnPropertyChangedCallback(object :
            Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                val hasMore = mViewModel.chapterRefreshStatus.noMoreData.get()
                if (hasMore == true) {
                    if (mViewModel.chapterAdapter.footerLayoutCount == 0) {
                        mViewModel.chapterAdapter.removeAllFooterView()
                        mViewModel.chapterAdapter.addFooterView(chapterFootView)
                    }
                } else {
                    mViewModel.chapterAdapter.removeAllFooterView()
                }
            }
        })

        mViewModel.commentRefreshStateMode.noMoreData.addOnPropertyChangedCallback(object :
            Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                val hasMore = mViewModel.commentRefreshStateMode.noMoreData.get()
                if (hasMore == true) {
                    mViewModel.homeDetailCommentAdapter.removeAllFooterView()
                    mViewModel.homeDetailCommentAdapter.addFooterView(commentFootView)
                } else {
                    mViewModel.homeDetailCommentAdapter.removeAllFooterView()
                }
            }
        })
    }

    override fun initData() {
    }

    override fun onStart() {
        super.onStart()
        mViewModel.chapterAdapter.notifyDataSetChanged()
    }

}