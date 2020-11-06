package com.rm.module_mine.activity

import com.rm.baselisten.model.BaseTitleModel
import com.rm.baselisten.mvvm.BaseVMActivity
import com.rm.module_mine.BR
import com.rm.module_mine.R
import com.rm.module_mine.databinding.MineActivityVersionUpdateBinding
import com.rm.module_mine.viewmodel.MineVersionUpdateViewModel

class MineVersionUpdateActivity : BaseVMActivity<MineActivityVersionUpdateBinding,MineVersionUpdateViewModel>() {

    override fun initData() {

    }

    override fun initView() {
        super.initView()
        val titleModel = BaseTitleModel()
            .setLeftIcon(R.drawable.base_icon_back)
            .setTitle(getString(R.string.mine_app_version))
            .setLeftIconClick { finish() }
        mViewModel.baseTitleModel.value = titleModel
    }

    override fun getLayoutId() = R.layout.mine_activity_version_update
    override fun initModelBrId() =  BR.viewModel

    override fun startObserve() {
    }
}