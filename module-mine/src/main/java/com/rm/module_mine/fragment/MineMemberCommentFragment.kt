package com.rm.module_mine.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.rm.baselisten.mvvm.BaseVMFragment
import com.rm.module_mine.BR
import com.rm.module_mine.R
import com.rm.module_mine.activity.MineMemberActivity
import com.rm.module_mine.databinding.MineFragmentMemberCommentBinding
import com.rm.module_mine.viewmodel.MineFragmentMemberCommentViewMode

class MineMemberCommentFragment :
    BaseVMFragment<MineFragmentMemberCommentBinding, MineFragmentMemberCommentViewMode>() {
    companion object {
        fun newInstance(memberId: String): Fragment {
            val bundle = Bundle()
            bundle.putString(MineMemberActivity.MEMBER_ID, memberId)
            val fragment = MineMemberCommentFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun initModelBrId() = BR.viewModel

    override fun startObserve() {
    }

    override fun initLayoutId() = R.layout.mine_fragment_member_comment

    override fun initData() {
        arguments?.getString(MineMemberActivity.MEMBER_ID)?.let {
//            mViewModel.getMemberProfile(it)
        }
    }
}