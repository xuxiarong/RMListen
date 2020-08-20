package com.rm.module_home.activity.menu

import android.content.Context
import android.content.Intent
import com.rm.baselisten.activity.BaseNetActivity
import com.rm.baselisten.model.BaseTitleModel
import com.rm.module_home.R
import com.rm.module_home.databinding.HomeActivityListenMenuBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class MenuActivity : BaseNetActivity<HomeActivityListenMenuBinding,MenuViewModel>() {
    private val menuViewModel by viewModel<MenuViewModel>()

    companion object{
        fun startActivity(context:Context){
            context.startActivity(Intent(context,MenuActivity::class.java))
        }
    }


    override fun getLayoutId(): Int = R.layout.home_activity_listen_menu

    override fun initViewModel(): MenuViewModel = menuViewModel

    override fun startObserve() {
    }

    override fun initView() {
        val baseTitleModel = BaseTitleModel()
            .setTitle(resources.getString(R.string.home_menu))
            .setLeftIconClick {
                finish()
            }
        menuViewModel.baseTitleModel.value = baseTitleModel
    }

    override fun initData() {
    }
}