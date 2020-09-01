package com.rm.module_home.activity.detail

import android.content.Context
import android.content.Intent
import com.rm.baselisten.mvvm.BaseVMActivity
import com.rm.baselisten.util.dip
import com.rm.baselisten.util.screenHeight
import com.rm.business_lib.utils.EllipsizeUtils
import com.rm.module_home.BR
import com.rm.module_home.R
import com.rm.module_home.databinding.HomeActivityDetailMainBinding
import com.rm.module_home.viewmodel.HomeDetailViewModel
import kotlinx.android.synthetic.main.home_activity_detail_content.*
import kotlinx.android.synthetic.main.home_activity_detail_main.*

/**
 * 书籍详情
 */
class HomeDetailActivity : BaseVMActivity<HomeActivityDetailMainBinding,HomeDetailViewModel>() {

    companion object {
        fun startActivity(context: Context) {
            context.startActivity(Intent(context, HomeDetailActivity::class.java))
        }
    }

    override fun getLayoutId(): Int = R.layout.home_activity_detail_main

    override fun initModelBrId() = BR.viewModel


    override fun startObserve() {

    }

    override fun initData() {
        EllipsizeUtils.ellipsize(detail_title,"测试标题是否真的能够换行，显示省略号")

        scroll_down_layout!!.setMinOffset(0)
        scroll_down_layout!!.setMaxOffset((screenHeight*0.5).toInt())
        scroll_down_layout!!.setExitOffset(dip(50f))
        scroll_down_layout!!.setIsSupportExit(true)
        scroll_down_layout!!.isAllowHorizontalScroll = true
        scroll_down_layout!!.setToExit()
    }
}