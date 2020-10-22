package com.rm.module_mine.activity

import com.rm.baselisten.model.BaseTitleModel
import com.rm.baselisten.mvvm.BaseVMActivity
import com.rm.module_mine.BR
import com.rm.module_mine.R
import com.rm.module_mine.databinding.MineActivitySettingDownloadBinding
import com.rm.module_mine.viewmodel.MineDownloadSettingViewModel

/**
 *
 * @author yuanfang
 * @date 9/21/20
 * @description 下载设置页面
 *
 */
class MineSettingDownloadActivity :
    BaseVMActivity<MineActivitySettingDownloadBinding, MineDownloadSettingViewModel>() {

    override fun initModelBrId() = BR.viewModel

    override fun getLayoutId() = R.layout.mine_activity_setting_download

    /**
     * 初始化数据
     */
    override fun initData() {
    }

    override fun initView() {
        super.initView()
        val titleModel = BaseTitleModel()
            .setLeftIcon(R.drawable.base_icon_back)
            .setTitle(getString(R.string.mine_download_setting))
            .setLeftIconClick { finish() }
        mViewModel.baseTitleModel.value = titleModel
    }

    override fun startObserve() {
    }

}