package com.rm.module_mine.activity

import com.rm.baselisten.model.BaseTitleModel
import com.rm.baselisten.mvvm.BaseVMActivity
import com.rm.module_mine.BR
import com.rm.module_mine.R
import com.rm.module_mine.databinding.MineActivitySettingPlayBinding
import com.rm.module_mine.viewmodel.MinePlaySettingViewModel

/**
 *
 * @author yuanfang
 * @date 9/21/20
 * @description 播放设置页面
 *
 */
class MineSettingPlayActivity :
    BaseVMActivity<MineActivitySettingPlayBinding, MinePlaySettingViewModel>() {

    override fun initModelBrId() = BR.viewModel

    override fun getLayoutId() = R.layout.mine_activity_setting_play

    /**
     * 初始化数据
     */
    override fun initData() {
    }

    override fun initView() {
        super.initView()
        val titleModel = BaseTitleModel()
            .setLeftIcon(R.drawable.base_icon_back)
            .setTitle(getString(R.string.mine_play_settings))
            .setLeftIconClick { finish() }
        mViewModel.baseTitleModel.value = titleModel

    }

    override fun startObserve() {
    }


}