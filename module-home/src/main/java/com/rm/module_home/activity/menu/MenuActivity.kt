package com.rm.module_home.activity.menu

import android.content.Context
import android.content.Intent
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.rm.baselisten.activity.BaseNetActivity
import com.rm.baselisten.model.BaseTitleModel
import com.rm.module_home.R
import com.rm.module_home.adapter.MenuListAdapter
import com.rm.module_home.databinding.HomeActivityListenMenuBinding
import kotlinx.android.synthetic.main.home_activity_listen_menu.*

class MenuActivity : BaseNetActivity<HomeActivityListenMenuBinding, MenuViewModel>() {

    private val menuListAdapter by lazy { MenuListAdapter() }

    companion object {
        fun startActivity(context: Context) {
            context.startActivity(Intent(context, MenuActivity::class.java))
        }
    }


    override fun getLayoutId(): Int = R.layout.home_activity_listen_menu

    override fun startObserve() {
        mViewModel.menuList.observe(this){
            menuListAdapter.setNewInstance(it)
        }
    }

    override fun initView() {
        val baseTitleModel = BaseTitleModel()
            .setTitle(resources.getString(R.string.home_menu))
            .setLeftIconClick {
                finish()
            }
        mViewModel.baseTitleModel.value = baseTitleModel
        dataBind.run {
            viewModel = mViewModel
        }

        home_menu_recycler_view.layoutManager = LinearLayoutManager(this,
            LinearLayoutManager.VERTICAL,false)
        home_menu_recycler_view.adapter = menuListAdapter
    }

    override fun initData() {

        mViewModel.getMenuBanner()

        mViewModel.getMenuListInfo()
    }
}