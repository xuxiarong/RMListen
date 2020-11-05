package com.rm.module_home.activity.detail

import android.content.Context
import android.content.Intent
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import com.rm.baselisten.binding.bindVerticalLayout
import com.rm.baselisten.mvvm.BaseVMActivity
import com.rm.baselisten.utilExt.getStateHeight
import com.rm.component_comm.play.PlayService
import com.rm.component_comm.router.RouterHelper
import com.rm.module_home.BR
import com.rm.module_home.R
import com.rm.module_home.databinding.HomeActivityDetailMainBinding
import com.rm.module_home.viewmodel.HomeDetailViewModel
import com.rm.module_home.widget.HomeDetailInterceptLayout
import kotlinx.android.synthetic.main.home_activity_detail_main.*

/**
 * 书籍详情
 *  //1、需添加书籍下架的toast提示，然后finish掉详情页
 */
class HomeDetailActivity : BaseVMActivity<HomeActivityDetailMainBinding, HomeDetailViewModel>() {
    override fun getLayoutId(): Int = R.layout.home_activity_detail_main

    override fun initModelBrId() = BR.viewModel


    companion object {
        const val AUDIO_ID = "audioID"
        fun startActivity(context: Context, audioID: String) {
            val intent = Intent(context, HomeDetailActivity::class.java)
            intent.putExtra(AUDIO_ID, audioID)
            context.startActivity(intent)
        }

        fun getIntent(audioId: String): HashMap<String, Any> {
            return hashMapOf(
                Pair(AUDIO_ID, audioId)
            )
        }
    }


    override fun initView() {
        super.initView()
        setTransparentStatusBar()

        mDataBind.homeDetailCommentRecycleView.apply {
            bindVerticalLayout(mViewModel.homeDetailCommentAdapter)
            mViewModel.createHeader(this)
        }

        (mDataBind.homeDetailTitleCl.layoutParams as ViewGroup.MarginLayoutParams).apply {
            //动态获取状态栏的高度,并设置标题栏的topMargin
            topMargin = getStateHeight(this@HomeDetailActivity)
        }

        intent?.getStringExtra(AUDIO_ID)?.let {
            mViewModel.audioId.set(it)
            mViewModel.intDetailInfo(it)
            mViewModel.getChapterList() //初始化章节列表

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
                } else {
                    mDataBind.homeDetailTitle.visibility = View.INVISIBLE
                }
            }
        })
    }

    override fun startObserve() {

    }

    override fun initData() {
    }

    override fun onStart() {
        super.onStart()
        mViewModel.chapterAdapter.notifyDataSetChanged()
    }


    override fun onResume() {
        super.onResume()
        val playService = RouterHelper.createRouter(PlayService::class.java)
        rootViewAddView(playService.getGlobalPlay())
        playService.showView(this)
    }

}