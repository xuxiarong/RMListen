package com.rm.module_home.activity.detail

import android.content.Context
import android.content.Intent
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
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
import kotlinx.android.synthetic.main.home_activity_detail_content.*
import kotlinx.android.synthetic.main.home_activity_detail_main.*

/**
 * 书籍详情
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

    private val homechapterAdater by lazy {
        CommonBindAdapter(
            mutableListOf<ChapterList>(),
            R.layout.home_item_detail_chapter,
            BR.DetailChapterViewModel
        ).apply {
            setOnItemClickListener { adapter, view, position ->
                mViewModel.detailViewModel.get()?.let {
                    playService.toPlayPage(this@HomeDetailActivity, it, position)
                }
            }
        }
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
        setD()
        val layoutParams = (home_detail_title_cl.layoutParams) as ConstraintLayout.LayoutParams
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
        LayoutMargin.topMargin = stateHeight + dip(44)
        scroll_down_layout.layoutParams = LayoutMargin
        scroll_down_layout.setOnScrollChangedListener(mOnScrollChangedListener)
        //audioId = "162163095869968384"
        if (audioId.orEmpty().isNotEmpty()) {
            mViewModel.intDetailInfo(audioId)
            mViewModel.chapterList(audioId, 1, 20, "asc")
            mViewModel.commentList(audioId, 1, 20)
        }

        home_detail_recyc_style.bindHorizontalLayout(homedetailtagsadapter)
        home_detail_comment_recycler.bindVerticalLayout(homeDetailCommentAdapter)
        detail_directory_recycler.bindVerticalLayout(homechapterAdater)

        //收藏点击事件
        mViewModel.clickCollected = { clickCollected() }

        //订阅点击事件
        mViewModel.clickSubscribe = { clickSubscribe() }

        home_detail_title_cl.setOnClickListener { finish() }
    }

    override fun startObserve() {

        mViewModel.detailViewModel.addOnPropertyChangedCallback(object :
            OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                loadBlurImage(
                    img_glide,
                    mViewModel.detailViewModel.get()?.detaillist?.audio_cover_url ?: ""
                )
                homedetailtagsadapter.setList(mViewModel.detailViewModel.get()?.detaillist?.detail_tags)
            }
        })

        mViewModel.detailCommentViewModel.observe(this, Observer {
            homeDetailCommentAdapter.setList(mViewModel.detailCommentViewModel.value!!.List_comment)
        })

        /*mViewModel.detailChapterViewModel.observe(this, Observer {
            homechapterAdater.setList(mViewModel.detailChapterViewModel.value!!.chapterList)
        })*/

        mViewModel.detailChapterViewModel.addOnPropertyChangedCallback(object :
            OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                homechapterAdater.setList(mViewModel.detailChapterViewModel.get()!!.chapter_list)
            }
        })

        mViewModel.actionControl.observe(this, Observer {
            mViewModel.detailViewModel.get()?.let {
                playService.toPlayPage(this@HomeDetailActivity, it, 0)
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

    private val Chapter_headView by lazy {
        View.inflate(this, R.layout.home_detail_chapter_headerview, null)
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
            .showMySheetListDialog(mViewModel, this, audioId.toString())
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