package com.rm.module_home.activity.menu

import android.content.Context
import android.content.Intent
import android.view.View
import androidx.lifecycle.observe
import com.rm.baselisten.model.BaseTitleModel
import com.rm.baselisten.mvvm.BaseVMActivity
import com.rm.business_lib.binding.bindData
import com.rm.module_home.BR
import com.rm.module_home.R
import com.rm.module_home.databinding.HomeActivityListenMenuBinding
import com.stx.xhb.androidx.XBanner

class MenuActivity : BaseVMActivity<HomeActivityListenMenuBinding, MenuViewModel>() {
    private val headView by lazy {
        View.inflate(this, R.layout.home_header_banner, null)
    }

    companion object {
        fun startActivity(context: Context) {
            context.startActivity(Intent(context, MenuActivity::class.java))
        }
    }

    override fun initModelBrId() = BR.viewModel

    override fun getLayoutId(): Int = R.layout.home_activity_listen_menu

    override fun startObserve() {
        mViewModel.bannerInfoList.observe(this) {
            headView.findViewById<XBanner>(R.id.home_head_banner).bindData(it)
            mViewModel.menuAdapter.addHeaderView(headView)

            mViewModel.menuAdapter.setOnItemClickListener { _, _, _ ->
                MenuDetailActivity.startActivity(this@MenuActivity)
            }
        }
        mViewModel.menuList.observe(this) {
            mViewModel.menuAdapter.setNewInstance(it)
        }
    }

    override fun initView() {
        val baseTitleModel = BaseTitleModel()
            .setTitle(resources.getString(R.string.home_menu))
            .setLeftIconClick {
                finish()
            }
        mViewModel.baseTitleModel.value = baseTitleModel
        mDataBind.run {
            viewModel = mViewModel
        }
    }

    override fun initData() {

        mViewModel.getMenuBanner()

        mViewModel.getMenuListInfo()
    }
}