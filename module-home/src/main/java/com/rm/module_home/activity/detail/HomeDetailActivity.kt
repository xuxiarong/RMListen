package com.rm.module_home.activity.detail

import android.content.Context
import android.content.Intent
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.Observable
import androidx.databinding.Observable.OnPropertyChangedCallback
import com.rm.baselisten.adapter.single.CommonBindAdapter
import com.rm.baselisten.binding.bindHorizontalLayout
import com.rm.baselisten.binding.bindVerticalLayout
import com.rm.baselisten.mvvm.BaseVMActivity
import com.rm.baselisten.thridlib.glide.loadBlurImage
import com.rm.baselisten.utilExt.dip
import com.rm.baselisten.utilExt.getStateHeight
import com.rm.baselisten.utilExt.screenHeight
import com.rm.business_lib.wedgit.bottomsheet.ScrollLayout
import com.rm.module_home.BR
import com.rm.module_home.R
import com.rm.module_home.databinding.HomeActivityDetailMainBinding
import com.rm.module_home.model.home.detail.*
import com.rm.module_home.viewmodel.HomeDetailViewModel
import kotlinx.android.synthetic.main.home_activity_detail_content.*
import kotlinx.android.synthetic.main.home_activity_detail_main.*
import kotlinx.android.synthetic.main.home_activity_listen_menu_detail.*

/**
 * 书籍详情
 */
class HomeDetailActivity : BaseVMActivity<HomeActivityDetailMainBinding,HomeDetailViewModel>() {

    private val homedetailtagsadapter by lazy { CommonBindAdapter(mutableListOf<Tags>(),
        R.layout.home_item_book_label,BR.Tags) }

    private val homeDetailCommentAdapter by lazy { CommonBindAdapter(mutableListOf<CommentList>()
        ,R.layout.home_detail_item_comment, BR.comment_list) }

    private val homechapterAdater by lazy { CommonBindAdapter(mutableListOf<ChapterList>()
        ,R.layout.home_item_detail_chapter,BR.DetailChapterViewModel) }

    companion object {
        fun startActivity(context: Context) {
            context.startActivity(Intent(context, HomeDetailActivity::class.java))
        }
    }
    private var stateHeight :Int = 0
    override fun initView() {
        super.initView()
        setD()
        val layoutParams = (home_detail_title_cl.layoutParams) as ConstraintLayout.LayoutParams
        layoutParams.apply {
            //动态获取状态栏的高度,并设置标题栏的topMargin
            stateHeight = getStateHeight(this@HomeDetailActivity)
            topMargin = stateHeight
        }
    }

    override fun startObserve() {

        mViewModel.detailViewModel.addOnPropertyChangedCallback( object : OnPropertyChangedCallback(){
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                loadBlurImage(img_glide,mViewModel.detailViewModel.get()!!.detaillist.cover_url)

                homedetailtagsadapter.setNewInstance(mViewModel.detailViewModel.get()!!.detaillist.tags)
                homedetailtagsadapter.notifyDataSetChanged()

            }
        })
        mViewModel.detailCommentViewModel.addOnPropertyChangedCallback( object : OnPropertyChangedCallback(){
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                homeDetailCommentAdapter.setNewInstance(mViewModel.detailCommentViewModel.get()!!.List_comment)
                homeDetailCommentAdapter.notifyDataSetChanged()
            }
        })

        mViewModel.detailChapterViewModel.addOnPropertyChangedCallback( object : OnPropertyChangedCallback(){
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                homechapterAdater.setNewInstance(mViewModel.detailChapterViewModel.get()!!.chapterList)
                homechapterAdater.notifyDataSetChanged()
            }
        })

    }

    private val mOnScrollChangedListener: ScrollLayout.OnScrollChangedListener =
        object : ScrollLayout.OnScrollChangedListener {
            override fun onScrollProgressChanged(currentProgress: Float) {
                //Log.e("currentProgress",""+currentProgress)
                if(currentProgress==0f){
                    home_detail_title.visibility = View.VISIBLE
                }else{
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

        scroll_down_layout!!.setMinOffset(0)
        scroll_down_layout!!.setMaxOffset((screenHeight*0.75).toInt())
        scroll_down_layout!!.setExitOffset(dip(101))
        scroll_down_layout!!.isAllowHorizontalScroll = true
        scroll_down_layout!!.setIsSupportExit(true)
        scroll_down_layout!!.setToOpen()
        val LayoutMargin = scroll_down_layout.layoutParams as ViewGroup.MarginLayoutParams
        LayoutMargin.topMargin = stateHeight + dip(44)
        scroll_down_layout.layoutParams = LayoutMargin
        //scroll_down_layout!!.background.alpha = 1

        scroll_down_layout.setOnScrollChangedListener(mOnScrollChangedListener)

        mViewModel.intDetailInfo()

        home_detail_recyc_style.bindHorizontalLayout(homedetailtagsadapter)
        home_detail_comment_recycler.bindVerticalLayout(homeDetailCommentAdapter)

        detail_directory_recycler.bindVerticalLayout(homechapterAdater)


    }

    override fun getLayoutId(): Int = R.layout.home_activity_detail_main

    override fun initModelBrId() = BR.viewModel
}