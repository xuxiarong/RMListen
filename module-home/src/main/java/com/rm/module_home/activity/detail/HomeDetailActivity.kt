package com.rm.module_home.activity.detail

import android.content.Context
import android.content.Intent
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.databinding.Observable
import androidx.databinding.Observable.OnPropertyChangedCallback
import androidx.lifecycle.Observer
import com.rm.baselisten.adapter.single.CommonBindAdapter
import com.rm.baselisten.binding.bindHorizontalLayout
import com.rm.baselisten.binding.bindVerticalLayout
import com.rm.baselisten.mvvm.BaseVMActivity
import com.rm.baselisten.thridlib.glide.loadBlurImage
import com.rm.baselisten.utilExt.dip
import com.rm.baselisten.utilExt.getStateHeight
import com.rm.baselisten.utilExt.screenHeight
import com.rm.business_lib.bean.ChapterList
import com.rm.business_lib.bean.DetailBookBean
import com.rm.business_lib.bean.DetailTags
import com.rm.business_lib.isLogin
import com.rm.business_lib.wedgit.bottomsheet.ScrollLayout
import com.rm.component_comm.listen.ListenService
import com.rm.component_comm.login.LoginService
import com.rm.component_comm.play.PlayService
import com.rm.component_comm.router.RouterHelper
import com.rm.module_home.BR
import com.rm.module_home.R
import com.rm.module_home.databinding.HomeActivityDetailMainBinding
import com.rm.module_home.model.home.detail.CommentList
import com.rm.module_home.viewmodel.HomeDetailViewModel
import kotlinx.android.synthetic.main.home_activity_detail_main.*
import kotlinx.android.synthetic.main.home_detail_activity_content.*
import kotlinx.android.synthetic.main.home_detail_chapter_headerview.*

/**
 * 书籍详情
 *  //1、需添加书籍下架的toast提示，然后finish掉详情页
 */
class HomeDetailActivity : BaseVMActivity<HomeActivityDetailMainBinding, HomeDetailViewModel>() {

    private val homedetailtagsadapter by lazy {
        CommonBindAdapter(
            mutableListOf<DetailTags>(),
            R.layout.home_item_book_label, BR.DetailTags
        )
    }

    private val homeDetailCommentAdapter by lazy {
        CommonBindAdapter(
            mutableListOf<CommentList>(), R.layout.home_detail_item_comment, BR.comment_list
        )
    }

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
        val layoutParams = (home_detail_title_cl.layoutParams) as Toolbar.LayoutParams
        layoutParams.apply {
            //动态获取状态栏的高度,并设置标题栏的topMargin
            stateHeight = getStateHeight(this@HomeDetailActivity)
            topMargin = stateHeight
        }
        audioId = intent?.getStringExtra(AUDIO_ID) ?: ""
        scroll_down_layout?.setMinOffset(0)
        scroll_down_layout?.setMaxOffset((screenHeight * 0.75).toInt())
        scroll_down_layout?.setExitOffset(dip(101))
        scroll_down_layout?.isAllowHorizontalScroll = true
        scroll_down_layout?.setIsSupportExit(true)
        scroll_down_layout?.setToOpen()
        val LayoutMargin = scroll_down_layout.layoutParams as ViewGroup.MarginLayoutParams
        LayoutMargin.topMargin = stateHeight + dip(40)

        val ContentMargin = home_detail_icon.layoutParams as ViewGroup.MarginLayoutParams
        ContentMargin.topMargin = stateHeight + dip(48)

        scroll_down_layout.layoutParams = LayoutMargin
        scroll_down_layout.setOnScrollChangedListener(mOnScrollChangedListener)
        //audioId = "162163095869968384"
        if (audioId.orEmpty().isNotEmpty()) {
            mViewModel.audioId.set(audioId)
            mViewModel.sort.set("asc")
            mViewModel.intDetailInfo(audioId)
            mViewModel.onRefresh() //初始化章节列表

            mViewModel.commentList(audioId, 1, 10)

        }

        home_detail_comment_recycler.bindVerticalLayout(homeDetailCommentAdapter)
        detail_directory_recycler.bindVerticalLayout(mViewModel.chapterAdapter)
        detail_anthology_recycler.bindHorizontalLayout(mViewModel.ChapterAnthologyAdapter)
        home_detail_recyc_style.bindHorizontalLayout(homedetailtagsadapter)

        //收藏点击事件
        mViewModel.clickCollected = { clickCollected() }

        //订阅点击事件
        mViewModel.clickSubscribe = { clickSubscribe() }

        home_detail_title_cl.setOnClickListener { finish() }
        // TODO: 2020/9/28 章节排序
        home_detail_play_sort.setOnCheckedChangeListener{buttonView, isChecked ->
            if(isChecked){
                mViewModel.sort.set("desc")
                mViewModel.refreshData()
            }else{
                mViewModel.sort.set("asc")
                mViewModel.refreshData()
            }
        }

        //TODO: 2020/9/28 关注主播
        detail_anthor_attention.setOnCheckedChangeListener{buttonView, isChecked ->
            if(isChecked){
                detail_anthor_attention.setText("已关注")
                detail_anthor_attention.setTextColor(getResources().getColor(R.color.business_color_b1b1b1))
            }else{
                detail_anthor_attention.setText("关注")
                detail_anthor_attention.setTextColor(getResources().getColor(R.color.business_text_color_666666))
            }
        }
    }

    override fun startObserve() {

        mViewModel.detailViewModel.addOnPropertyChangedCallback(object :
            OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                //高斯效果
                loadBlurImage(
                    img_glide,
                    mViewModel.detailViewModel.get()?.detaillist?.audio_cover_url ?: ""
                )
                homedetailtagsadapter.setList(mViewModel.detailViewModel.get()?.detaillist?.detail_tags)
            }
        })

        mViewModel.detailCommentViewModel.observe(this, Observer {
            homeDetailCommentAdapter.setList(mViewModel.detailCommentViewModel.value!!.list_comment)
        })

        mViewModel.actionControl.observe(this, Observer {
            mViewModel.detailViewModel.get()?.let {
                playService.toPlayPage(
                    this@HomeDetailActivity, DetailBookBean(
                        audio_id = it.detaillist.audio_id,
                        audio_name = it.detaillist.audio_name,
                        original_name = it.detaillist.original_name,
                        author = it.detaillist.author,
                        audio_cover_url = it.detaillist.audio_cover_url
                    ), 0
                )
            }
        })

    }

    private val mOnScrollChangedListener: ScrollLayout.OnScrollChangedListener =
        object : ScrollLayout.OnScrollChangedListener {
            override fun onScrollProgressChanged(currentProgress: Float) {
                //Log.e("currentProgress",""+currentProgress)
                if (currentProgress == 0f) {
                    home_detail_title.visibility = View.VISIBLE
                } else {
                    home_detail_title.visibility = View.GONE
                }
            }

            override fun onScrollFinished(currentStatus: ScrollLayout.Status) {
//                if (currentStatus == ScrollLayout.Status.EXIT)
                //Log.e("onScrollFinished",""+currentStatus)
            }

            override fun onChildScroll(top: Int) {
                //Log.e("onChildScroll",""+top)
            }
        }

    override fun initData() {
        //mViewModel.intDetailInfo("162163095869968384")

    }

    override fun getLayoutId(): Int = R.layout.home_activity_detail_main

    override fun initModelBrId() = BR.viewModel


    /**
     * 收藏点击事件
     */
    private fun clickCollected() {
        if (!isLogin.get()) {
            toLogin()
            return
        }
        RouterHelper.createRouter(ListenService::class.java)
            .showMySheetListDialog(mViewModel, this, audioId)
    }

    /**
     * 点击订阅
     */
    private fun clickSubscribe() {
        if (!isLogin.get()) {
            toLogin()
            return
        }
        mViewModel.subscribe(audioId)
    }

    /**
     * 快捷登陆
     */
    private fun toLogin() {
        RouterHelper.createRouter(LoginService::class.java).quicklyLogin(mViewModel, this)
    }



}