package com.rm.module_mine.fragment

import com.rm.baselisten.mvvm.BaseVMFragment
import com.rm.module_mine.BR
import com.rm.module_mine.R
import com.rm.module_mine.databinding.MineFragmentMemberMainBinding
import com.rm.module_mine.viewmodel.MineMemberViewModel

/**
 * 主播/个人 发布的书籍/创建听单/收藏听单
 */
class MineMemberMainFragment  : BaseVMFragment<MineFragmentMemberMainBinding, MineMemberViewModel>(){

    override fun initModelBrId() = BR.viewModel

    override fun startObserve() {

    }

    override fun initLayoutId() = R.layout.mine_fragment_member_main

    override fun initData() {

    }
}