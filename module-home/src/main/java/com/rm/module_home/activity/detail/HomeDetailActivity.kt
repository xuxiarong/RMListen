package com.rm.module_home.activity.detail

import android.content.Context
import android.content.Intent
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import com.rm.baselisten.mvvm.BaseVMActivity
import com.rm.baselisten.utilExt.dip
import com.rm.baselisten.utilExt.getStateHeight
import com.rm.business_lib.bean.DetailBookBean
import com.rm.business_lib.wedgit.bottomsheet.ScrollLayout
import com.rm.component_comm.play.PlayService
import com.rm.component_comm.router.RouterHelper
import com.rm.module_home.BR
import com.rm.module_home.R
import com.rm.module_home.databinding.HomeActivityDetailMainBinding
import com.rm.module_home.viewmodel.HomeDetailViewModel
import com.rm.module_play.enum.Jump
import kotlinx.android.synthetic.main.home_activity_detail_main.*
import kotlinx.android.synthetic.main.home_detail_activity_content.*
import kotlinx.android.synthetic.main.home_detail_chapter_headerview.*

/**
 * 书籍详情
 *  //1、需添加书籍下架的toast提示，然后finish掉详情页
 */
class HomeDetailActivity : BaseVMActivity<HomeActivityDetailMainBinding, HomeDetailViewModel>() {


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

        val layoutParams = home_detail_title_cl.layoutParams as ViewGroup.MarginLayoutParams
        layoutParams.apply {
            //动态获取状态栏的高度,并设置标题栏的topMargin
            stateHeight = getStateHeight(this@HomeDetailActivity)
            topMargin = stateHeight
        }
        audioId = intent?.getStringExtra(AUDIO_ID) ?: ""
        scroll_down_layout?.apply {
            setMinOffset(0)
            setMaxOffset((screenHeight * 0.75).toInt())
            setExitOffset(dip(101))
            isAllowHorizontalScroll = true
            setIsSupportExit(true)
            setToOpen()
            val margin = this.layoutParams as ViewGroup.MarginLayoutParams
            margin.topMargin = stateHeight + dip(40)
            this.layoutParams = margin
            setOnScrollChangedListener(mOnScrollChangedListener)
        }

        val contentMargin = home_detail_icon.layoutParams as ViewGroup.MarginLayoutParams
        contentMargin.topMargin = stateHeight + dip(48)

        //audioId = "162163095869968384"
        if (audioId.isNotEmpty()) {
            mViewModel.audioId.set(audioId)
            mViewModel.sort.set("asc")
            mViewModel.intDetailInfo(audioId)
            mViewModel.onRefresh() //初始化章节列表

            mViewModel.commentList(audioId, 1, 10)

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

        //TODO: 2020/9/28 关注主播
        detail_anthor_attention.setOnCheckedChangeListener { _, isChecked ->
            mViewModel.isAttention.set(isChecked)
        }

    }

    override fun startObserve() {

        mViewModel.actionControl.observe(this, Observer {
            mViewModel.detailViewModel.get()?.let {
                playService.toPlayPage(
                    this@HomeDetailActivity, DetailBookBean(
                        audio_id = it.detaillist.audio_id,
                        audio_name = it.detaillist.audio_name,
                        original_name = it.detaillist.original_name,
                        author = it.detaillist.author,
                        audio_cover_url = it.detaillist.audio_cover_url
                    ), Jump.DETAILSBOOK.from
                )
            }
        })

    }

    private val mOnScrollChangedListener: ScrollLayout.OnScrollChangedListener =
        object : ScrollLayout.OnScrollChangedListener {
            override fun onScrollProgressChanged(currentProgress: Float) {
                if (currentProgress == 0f) {
                    home_detail_title.visibility = View.VISIBLE
                } else {
                    home_detail_title.visibility = View.GONE
                }
            }

            override fun onScrollFinished(currentStatus: ScrollLayout.Status) {
            }

            override fun onChildScroll(top: Int) {
            }
        }

    override fun initData() {
        //mViewModel.intDetailInfo("162163095869968384")

    }

    override fun getLayoutId(): Int = R.layout.home_activity_detail_main

    override fun initModelBrId() = BR.viewModel



}