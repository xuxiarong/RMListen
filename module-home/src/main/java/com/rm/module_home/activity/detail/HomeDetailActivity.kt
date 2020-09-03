package com.rm.module_home.activity.detail

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.databinding.Observable
import androidx.databinding.Observable.OnPropertyChangedCallback
import com.rm.baselisten.adapter.single.CommonBindAdapter
import com.rm.baselisten.binding.bindHorizontalLayout
import com.rm.baselisten.binding.bindVerticalLayout
import com.rm.baselisten.mvvm.BaseVMActivity
import com.rm.baselisten.utilExt.dip
import com.rm.baselisten.utilExt.screenHeight
import com.rm.business_lib.utils.EllipsizeUtils
import com.rm.business_lib.wedgit.bottomsheet.ScrollLayout
import com.rm.module_home.BR
import com.rm.module_home.R
import com.rm.module_home.databinding.HomeActivityDetailMainBinding
import com.rm.module_home.model.home.detail.ChapterList
import com.rm.module_home.model.home.detail.CommentList
import com.rm.module_home.model.home.detail.Tags
import com.rm.module_home.viewmodel.HomeDetailViewModel
import kotlinx.android.synthetic.main.home_activity_detail_content.*
import kotlinx.android.synthetic.main.home_activity_detail_main.*

/**
 * 书籍详情
 */
class HomeDetailActivity : BaseVMActivity<HomeActivityDetailMainBinding,HomeDetailViewModel>() {

    private val homedetailtagsadapter by lazy { CommonBindAdapter(mutableListOf<Tags>(),R.layout.home_item_book_label,BR.Tags) }

    private val homeDetailCommentAdapter by lazy { CommonBindAdapter(mutableListOf<CommentList>()
        ,R.layout.home_detail_item_comment, BR.comment_list) }

    private val homechapterAdater by lazy { CommonBindAdapter(mutableListOf<ChapterList>()
        ,R.layout.home_item_detail_chapter,BR.DetailChapterViewModel) }

    companion object {
        fun startActivity(context: Context) {
            context.startActivity(Intent(context, HomeDetailActivity::class.java))
        }
    }

    override fun getLayoutId(): Int = R.layout.home_activity_detail_main

    override fun initModelBrId() = BR.viewModel


    override fun startObserve() {
        mViewModel.detailViewModel.addOnPropertyChangedCallback( object : OnPropertyChangedCallback(){
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                homedetailtagsadapter.setList(mViewModel.detailViewModel.get()!!.detaillist.tags)
                homedetailtagsadapter.notifyDataSetChanged()
            }
        })
        mViewModel.detailCommentViewModel.addOnPropertyChangedCallback( object : OnPropertyChangedCallback(){
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                homeDetailCommentAdapter.setList(mViewModel.detailCommentViewModel.get()!!.List_comment)
                homeDetailCommentAdapter.notifyDataSetChanged()
            }
        })

        mViewModel.detailChapterViewModel.addOnPropertyChangedCallback( object : OnPropertyChangedCallback(){
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                homechapterAdater.setList(mViewModel.detailChapterViewModel.get()!!.chapterList)

                homechapterAdater.notifyDataSetChanged()
            }
        })
    }

    private val mOnScrollChangedListener: ScrollLayout.OnScrollChangedListener =
        object : ScrollLayout.OnScrollChangedListener {
            override fun onScrollProgressChanged(currentProgress: Float) {
                Log.e("currentProgress",""+currentProgress);
                if(currentProgress==0f){
                    //tx_ff?.setText("wwwwwww")
                }else{
                    //tx_ff?.setText("")
                }
                if (currentProgress >= 0) {
                    var precent = 255 * currentProgress
                    if (precent > 255) {
                        precent = 255f
                    } else if (precent < 0) {
                        precent = 0f
                    }
                    //mScrollLayout!!.background.alpha = 255 - precent.toInt()
                }
            }
            override fun onScrollFinished(currentStatus: ScrollLayout.Status) {
                if (currentStatus == ScrollLayout.Status.EXIT) {
//                    text_foot?.setVisibility(View.VISIBLE)
                }
            }

            override fun onChildScroll(top: Int) {

            }
        }

    override fun initData() {
        EllipsizeUtils.ellipsize(detail_title,"测试标题是否真的能够换行，显示省略号")

        scroll_down_layout!!.setMinOffset(0)
        scroll_down_layout!!.setMaxOffset((screenHeight*0.5).toInt())
        scroll_down_layout!!.setExitOffset(dip(100))
        scroll_down_layout!!.setAllowHorizontalScroll(true)
        scroll_down_layout!!.setIsSupportExit(true)
        scroll_down_layout!!.setToOpen()

        //scroll_down_layout!!.background.alpha = 1

        //scroll_down_layout.setOnScrollChangedListener(mOnScrollChangedListener)

        mViewModel.intDetailInfo()

        home_detail_recyc_style.bindHorizontalLayout(homedetailtagsadapter)
        home_detail_comment_recycler.bindVerticalLayout(homeDetailCommentAdapter)

        detail_directory_recycler.bindVerticalLayout(homechapterAdater)

    }

}