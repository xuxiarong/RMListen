package com.rm.module_mine.fragment

import com.rm.baselisten.mvvm.BaseVMFragment
import com.rm.module_mine.BR
import com.rm.module_mine.R
import com.rm.module_mine.databinding.MineFragmentMemberCommentBinding
import com.rm.module_mine.viewmodel.MineMemberViewModel

class MineMemberCommentFragment : BaseVMFragment<MineFragmentMemberCommentBinding, MineMemberViewModel>(){

    override fun initModelBrId() = BR.viewModel

    override fun startObserve() {
    }

    override fun initLayoutId() = R.layout.mine_fragment_member_comment

    override fun initData() {
    }
}