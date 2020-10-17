package com.rm.module_mine.activity

import com.rm.baselisten.model.BaseTitleModel
import com.rm.baselisten.mvvm.BaseVMActivity
import com.rm.module_mine.BR
import com.rm.module_mine.R
import com.rm.module_mine.databinding.ActivityMineMemberDetailBinding
import com.rm.module_mine.viewmodel.MineMemberViewModel

/**
 *  主播/用户详情
 */
class MineMemberActivity : BaseVMActivity<ActivityMineMemberDetailBinding, MineMemberViewModel>() {

    override fun initModelBrId() = BR.viewModel

    override fun startObserve() {

    }

    override fun initData() {
        val titleModel = BaseTitleModel()
            .setLeftIcon(R.drawable.base_icon_back)
            .setLeftIconClick { finish() }
        mViewModel.baseTitleModel.value = titleModel
    }

    override fun getLayoutId() = R.layout.activity_mine_member_detail
}