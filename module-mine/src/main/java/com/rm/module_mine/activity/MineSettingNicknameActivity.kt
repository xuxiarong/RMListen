package com.rm.module_mine.activity

import com.rm.baselisten.model.BaseTitleModel
import com.rm.baselisten.mvvm.BaseVMActivity
import com.rm.module_mine.BR
import com.rm.module_mine.R
import com.rm.module_mine.databinding.MineActivitySettingNicknameBinding
import com.rm.module_mine.viewmodel.MineNicknameSettingViewModel

/**
 *
 * @author yuanfang
 * @date 9/21/20
 * @description 昵称设置页面
 *
 */
class MineSettingNicknameActivity :
    BaseVMActivity<MineActivitySettingNicknameBinding, MineNicknameSettingViewModel>() {

    override fun initModelBrId() = BR.viewModel

    override fun getLayoutId() = R.layout.mine_activity_setting_nickname

    /**
     * 初始化数据
     */
    override fun initData() {
    }

    override fun initView() {
        super.initView()
        val titleModel = BaseTitleModel()
            .setLeftIcon(R.drawable.base_icon_back)
            .setTitle(getString(R.string.mine_change_nickname))
            .setRightText(getString(R.string.business_sure))
            .setRightTextColor (R.color.business_text_color_666666)
            .setRightTextClick { mViewModel.updateUserInfo() }
            .setLeftIconClick { finish() }
        mViewModel.baseTitleModel.value = titleModel

    }

    override fun startObserve() {
    }


}