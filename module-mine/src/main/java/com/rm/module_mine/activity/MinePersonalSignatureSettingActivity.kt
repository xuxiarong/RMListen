package com.rm.module_mine.activity

import com.rm.baselisten.model.BaseTitleModel
import com.rm.baselisten.mvvm.BaseVMActivity
import com.rm.module_mine.BR
import com.rm.module_mine.R
import com.rm.module_mine.databinding.MineActivityPersonalSignatureSettingBinding
import com.rm.module_mine.databinding.MineActivityPlaySettingBinding
import com.rm.module_mine.viewmodel.MinePersonalSignatureSettingViewModel
import com.rm.module_mine.viewmodel.MinePlaySettingViewModel

/**
 *
 * @author yuanfang
 * @date 9/21/20
 * @description 个性签名设置页面
 *
 */
class MinePersonalSignatureSettingActivity :
    BaseVMActivity<MineActivityPersonalSignatureSettingBinding, MinePersonalSignatureSettingViewModel>() {

    override fun initModelBrId() = BR.viewModel

    override fun getLayoutId() = R.layout.mine_activity_personal_signature_setting

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
            .setLeftIconClick { finish() }
        mViewModel.baseTitleModel.value = titleModel

    }

    override fun startObserve() {
    }


}