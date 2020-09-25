package com.rm.module_mine.activity

import com.rm.baselisten.model.BaseTitleModel
import com.rm.baselisten.mvvm.BaseVMActivity
import com.rm.module_mine.BR
import com.rm.module_mine.R
import com.rm.module_mine.databinding.MineActivityPersonalInfoBinding
import com.rm.module_mine.viewmodel.MinePersonalInfoViewModel

/**
 *
 * @author yuanfang
 * @date 9/21/20
 * @description
 *
 */
class MinePersonalInfoActivity :
    BaseVMActivity<MineActivityPersonalInfoBinding, MinePersonalInfoViewModel>() {


    override fun initModelBrId() = BR.viewModel

    override fun getLayoutId() = R.layout.mine_activity_personal_info

    /**
     * 初始化数据
     */
    override fun initData() {
    }

    override fun initView() {
        super.initView()
        val titleModel = BaseTitleModel()
            .setLeftIcon(R.drawable.base_icon_back)
            .setTitle("个人资料")
            .setLeftIconClick { finish() }
        mViewModel.baseTitleModel.value = titleModel
    }

    override fun startObserve() {
    }
}