package com.rm.module_home.activity.menu

import android.content.Context
import android.content.Intent
import android.view.View
import androidx.lifecycle.observe
import com.rm.baselisten.binding.bindVerticalLayout
import com.rm.baselisten.model.BaseTitleModel
import com.rm.baselisten.mvvm.BaseVMActivity
import com.rm.business_lib.binding.bindData
import com.rm.module_home.BR
import com.rm.module_home.R
import com.rm.module_home.adapter.MenuListAdapter
import com.rm.module_home.databinding.HomeActivityListenMenuBinding
import com.stx.xhb.androidx.XBanner
import kotlinx.android.synthetic.main.home_activity_listen_menu.*

class MenuActivity : BaseVMActivity<HomeActivityListenMenuBinding, MenuViewModel>() {
    private val headView by lazy {
        View.inflate(this, R.layout.home_header_banner, null)
    }
    private val menuAdapter by lazy { MenuListAdapter(mViewModel) }

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
        }
        mViewModel.menuList.observe(this) {
            menuAdapter.setList(it)
        }
    }

    override fun initView() {
        val baseTitleModel = BaseTitleModel()
            .setTitle(resources.getString(R.string.home_menu))
            .setLeftIconClick {
                finish()
            }
        mViewModel.baseTitleModel.value = baseTitleModel

        home_menu_recycler_view.bindVerticalLayout(menuAdapter)
        menuAdapter.setOnItemClickListener { _, _, _ ->
            MenuDetailActivity.startActivity(this@MenuActivity)
        }
        menuAdapter.addHeaderView(headView)
    }

    override fun initData() {

        mViewModel.getMenuBanner()

        mViewModel.getMenuListInfo()
    }
}