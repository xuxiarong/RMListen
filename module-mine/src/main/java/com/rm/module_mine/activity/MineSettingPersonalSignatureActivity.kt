package com.rm.module_mine.activity

import com.rm.baselisten.model.BaseTitleModel
import com.rm.baselisten.mvvm.BaseVMActivity
import com.rm.module_mine.BR
import com.rm.module_mine.R
import com.rm.module_mine.databinding.MineActivitySettingPersonalSignatureBinding
import com.rm.module_mine.viewmodel.MinePersonalSignatureSettingViewModel

/**
 *
 * @author yuanfang
 * @date 9/21/20
 * @description 个性签名设置页面
 *
 */
class MineSettingPersonalSignatureActivity :
    BaseVMActivity<MineActivitySettingPersonalSignatureBinding, MinePersonalSignatureSettingViewModel>() {

    override fun initModelBrId() = BR.viewModel

    override fun getLayoutId() = R.layout.mine_activity_setting_personal_signature

    /**
     * 初始化数据
     */
    override fun initData() {
    }

    override fun initView() {
        super.initView()
        val titleModel = BaseTitleModel()
            .setLeftIcon(R.drawable.base_icon_back)
            .setTitle(getString(R.string.mine_signature))
            .setRightText(getString(R.string.business_sure))
            .setLeftIconClick { finish() }
            .setRightTextColor (R.color.business_text_color_666666)
            .setRightEnabled(false)
            .setRightTextClick { mViewModel.updateUserInfo() }

        mViewModel.baseTitleModel.value = titleModel

    }
    override fun startObserve() {
    }


}