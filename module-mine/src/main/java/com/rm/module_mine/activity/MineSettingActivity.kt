package com.rm.module_mine.activity

import android.content.Context
import android.content.Intent
import com.rm.baselisten.model.BaseTitleModel
import com.rm.baselisten.mvvm.BaseVMActivity
import com.rm.business_lib.isLogin
import com.rm.business_lib.loginUser
import com.rm.component_comm.login.LoginService
import com.rm.component_comm.router.RouterHelper
import com.rm.module_mine.BR
import com.rm.module_mine.R
import com.rm.module_mine.databinding.MineActivitySettingBinding
import com.rm.module_mine.viewmodel.MineSettingViewModel

/**
 *
 * @author yuanfang
 * @date 9/21/20
 * @description
 *
 */
class MineSettingActivity : BaseVMActivity<MineActivitySettingBinding, MineSettingViewModel>() {

    companion object {
        fun startActivity(context: Context) {
            context.startActivity(Intent(context, MineSettingActivity::class.java))
        }
    }

    override fun initModelBrId() = BR.viewModel

    override fun getLayoutId() = R.layout.mine_activity_setting

    /**
     * 初始化数据
     */
    override fun initData() {
    }

    override fun initView() {
        super.initView()
        val titleModel = BaseTitleModel()
            .setLeftIcon(R.drawable.base_icon_back)
            .setTitle("设置")
            .setLeftIconClick { finish() }
        mViewModel.baseTitleModel.value = titleModel
    }

    /**
     * 监听数据的变化
     */
    override fun startObserve() {
    }


}