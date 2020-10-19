package com.rm.module_home.activity.detail

import com.rm.baselisten.binding.bindVerticalLayout
import com.rm.baselisten.mvvm.BaseVMActivity
import com.rm.module_home.BR
import com.rm.module_home.R
import com.rm.module_home.databinding.HomeActivityDetail1Binding
import com.rm.module_home.viewmodel.HomeDetailViewModel1

/**
 * 书籍详情
 *  //1、需添加书籍下架的toast提示，然后finish掉详情页
 */
class HomeDetailActivity1 : BaseVMActivity<HomeActivityDetail1Binding, HomeDetailViewModel1>() {

    override fun getLayoutId(): Int = R.layout.home_activity_detail1

    override fun initModelBrId() = BR.viewModel

    override fun startObserve() {

    }

    override fun initView() {
        super.initView()

        mDataBind.homeDetailCommentRecycleView.apply {
            bindVerticalLayout(mViewModel.homeDetailCommentAdapter)
            mViewModel.createHeader(this)
        }

        mViewModel.audioId.set("165050481775759360")
    }

    override fun initData() {
        mViewModel.getChapterData("165050481775759360")
        mViewModel.getDetailInfo("165050481775759360")
        mViewModel.getCommentList("165050481775759360")
    }

}