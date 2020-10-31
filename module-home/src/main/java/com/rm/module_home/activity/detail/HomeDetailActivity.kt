package com.rm.module_home.activity.detail

import android.content.Context
import android.content.Intent
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import com.rm.baselisten.binding.bindVerticalLayout
import com.rm.baselisten.mvvm.BaseVMActivity
import com.rm.baselisten.utilExt.getStateHeight
import com.rm.business_lib.bean.DetailBookBean
import com.rm.component_comm.play.PlayService
import com.rm.component_comm.router.RouterHelper
import com.rm.module_home.BR
import com.rm.module_home.R
import com.rm.module_home.databinding.HomeActivityDetailMainBinding
import com.rm.module_home.viewmodel.HomeDetailViewModel
import com.rm.module_home.widget.HomeDetailInterceptLayout
import com.rm.module_play.enum.Jump
import kotlinx.android.synthetic.main.home_activity_detail_main.*

/**
 * 书籍详情
 *  //1、需添加书籍下架的toast提示，然后finish掉详情页
 */
class HomeDetailActivity : BaseVMActivity<HomeActivityDetailMainBinding, HomeDetailViewModel>() {
    val memberId: String = ""
    override fun getLayoutId(): Int = R.layout.home_activity_detail_main

    override fun initModelBrId() = BR.viewModel

    //播放器路由
    private val playService by lazy {
        RouterHelper.createRouter(PlayService::class.java)
    }

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

    private var audioId: String = ""
    private var stateHeight: Int = 0

    override fun initView() {
        super.initView()
        setTransparentStatusBar()
        mDataBind.homeDetailCommentRecycleView.apply {
            val adapter = mViewModel.homeDetailCommentAdapter
            adapter.onAttachedToRecyclerView(this)
            bindVerticalLayout(adapter)
            mViewModel.createHeader(this)
        }

        mDataBind.homeChapterDetailRefresh.setEnableLoadMoreWhenContentNotFull(true)

        val layoutParams = mDataBind.homeDetailTitleCl.layoutParams as ViewGroup.MarginLayoutParams
        layoutParams.apply {
            //动态获取状态栏的高度,并设置标题栏的topMargin
            stateHeight = getStateHeight(this@HomeDetailActivity)
            topMargin = stateHeight
        }

        audioId = intent?.getStringExtra(AUDIO_ID) ?: ""
        if (audioId.isNotEmpty()) {
            mViewModel.audioId.set(audioId)
            mViewModel.sort.set("asc")
            mViewModel.intDetailInfo(audioId)
            mViewModel.onRefresh() //初始化章节列表

            mViewModel.getCommentList(audioId)

        }
        // TODO: 2020/9/28 章节排序
        home_detail_play_sort.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                mViewModel.sort.set("desc")
                mViewModel.getTrackList("desc")
            } else {
                mViewModel.sort.set("asc")
                mViewModel.getTrackList("asc")
            }
        }

        mDataBind.homeDetailChapterHeader.post {
            val measuredHeight = mDataBind.homeDetailChapterHeader.measuredHeight
            val params =
                mDataBind.homeDetailCommentRefresh.layoutParams as ConstraintLayout.LayoutParams
            params.bottomMargin = measuredHeight + 20
            mDataBind.homeDetailCommentRefresh.layoutParams = params
        }

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

        mViewModel.actionControl.observe(this, Observer {
            mViewModel.detailInfoData.get()?.let {
                playService.toPlayPage(
                    this@HomeDetailActivity, DetailBookBean(
                        audio_id = it.list.audio_id,
                        audio_name = it.list.audio_name,
                        original_name = it.list.original_name,
                        author = it.list.author,
                        audio_cover_url = it.list.audio_cover_url
                    ), Jump.DETAILSBOOK.from
                )
            }
        })

    }

//    private val mOnScrollChangedListener: ScrollLayout.OnScrollChangedListener =
//        object : ScrollLayout.OnScrollChangedListener {
//            override fun onScrollProgressChanged(currentProgress: Float) {
//                if (currentProgress == 0f) {
//                    mDataBind.homeDetailTitle.visibility = View.VISIBLE
//                } else {
//                    mDataBind.homeDetailTitle.visibility = View.GONE
//                }
//            }
//
//            override fun onScrollFinished(currentStatus: ScrollLayout.Status) {
//            }
//
//            override fun onChildScroll(top: Int) {
//            }
//        }

    override fun initData() {
        //mViewModel.intDetailInfo("162163095869968384")
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