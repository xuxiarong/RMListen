package com.rm.module_home.activity.detail

import com.rm.baselisten.mvvm.BaseVMActivity
import com.rm.baselisten.util.DLog
import com.rm.module_home.BR
import com.rm.module_home.R
import com.rm.module_home.databinding.HomeActivityDetail1Binding
import com.rm.module_home.viewmodel.HomeDetailViewModel1
import kotlinx.android.synthetic.main.home_activity_detail1.*

/**
 * 书籍详情
 *  //1、需添加书籍下架的toast提示，然后finish掉详情页
 */
class HomeDetailActivity1 : BaseVMActivity<HomeActivityDetail1Binding, HomeDetailViewModel1>() {

    override fun getLayoutId(): Int = R.layout.home_activity_detail1

    override fun initModelBrId() = BR.viewModel

    override fun startObserve() {

    }

    override fun initData() {
        mViewModel.onRefresh()
        mViewModel.getDetailInfo("165050481775759360")
        mViewModel.commentList("165050481775759360", 1, 10)
    }

}