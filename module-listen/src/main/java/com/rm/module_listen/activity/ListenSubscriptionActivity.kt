package com.rm.module_listen.activity

import android.content.Context
import android.content.Intent
import com.rm.baselisten.model.BaseTitleModel
import com.rm.component_comm.activity.ComponentShowPlayActivity
import com.rm.module_listen.BR
import com.rm.module_listen.R
import com.rm.module_listen.databinding.ListenActivitySubscriptionBinding
import com.rm.module_listen.viewmodel.ListenSubscriptionViewModel

/**
 * 订阅界面
 */
class ListenSubscriptionActivity :
    ComponentShowPlayActivity<ListenActivitySubscriptionBinding, ListenSubscriptionViewModel>() {


    companion object {
        fun startActivity(context: Context) {
            context.startActivity(Intent(context, ListenSubscriptionActivity::class.java))
        }
    }

    override fun getLayoutId() = R.layout.listen_activity_subscription


    override fun initModelBrId() = BR.viewModel


    override fun initView() {
        super.initView()
        //初始化title信息
        val baseTitleModel = BaseTitleModel().setTitle(getString(R.string.listen_subscription))
            .setLeftIcon(R.drawable.base_icon_back)
            .setLeftIconClick { finish() }
        mViewModel.baseTitleModel.value = baseTitleModel
    }

    override fun initData() {
        mViewModel.showLoading()
        mViewModel.getData()
    }


    override fun startObserve() {
    }


}