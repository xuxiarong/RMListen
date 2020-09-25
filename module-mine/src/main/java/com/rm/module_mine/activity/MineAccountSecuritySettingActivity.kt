package com.rm.module_mine.activity

import android.content.Context
import android.content.Intent
import com.rm.baselisten.model.BaseTitleModel
import com.rm.baselisten.mvvm.BaseVMActivity
import com.rm.module_mine.BR
import com.rm.module_mine.R
import com.rm.module_mine.databinding.MineActivityAccountSecuritySettingBinding
import com.rm.module_mine.viewmodel.MineAccountSecuritySettingViewModel

/**
 *
 * @author yuanfang
 * @date 9/21/20
 * @description 账号安全设置
 *
 */
class MineAccountSecuritySettingActivity :
    BaseVMActivity<MineActivityAccountSecuritySettingBinding, MineAccountSecuritySettingViewModel>() {

    override fun initModelBrId() = BR.viewModel

    override fun getLayoutId() = R.layout.mine_activity_account_security_setting

    /**
     * 初始化数据
     */
    override fun initData() {
    }

    override fun initView() {
        super.initView()
        val titleModel = BaseTitleModel()
            .setLeftIcon(R.drawable.base_icon_back)
            .setTitle(getString(R.string.mine_account_security_setting))
            .setLeftIconClick { finish() }
        mViewModel.baseTitleModel.value = titleModel
    }

    /**
     * 监听数据的变化
     */
    override fun startObserve() {
    }


}