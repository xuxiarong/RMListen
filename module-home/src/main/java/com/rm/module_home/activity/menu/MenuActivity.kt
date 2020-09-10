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
import com.rm.module_home.bean.MenuSheetBean
import com.rm.module_home.databinding.HomeActivityListenMenuBinding
import com.rm.module_home.viewmodel.MenuViewModel
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

        mViewModel.menuList.observe(this) {
            menuAdapter.setList(it.sheet_list?.list)
            headView.findViewById<XBanner>(R.id.home_head_banner).bindData(it.banner_list)
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
        menuAdapter.setOnItemClickListener { _, _, position ->
            val sheetBean: MenuSheetBean? = mViewModel.menuList.value
            val pageId = sheetBean?.page_id ?: 0
            val sheetId = (sheetBean?.sheet_list?.list?.get(position)?.sheet_id) ?: ""
            MenuDetailActivity.startActivity(
                this@MenuActivity,
                pageId,
                sheetId
            )
        }
        menuAdapter.addHeaderView(headView)
    }

    override fun initData() {
        mViewModel.getMenuListInfo()

    }
}