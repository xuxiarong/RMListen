package com.rm.module_mine.fragment

import android.os.Bundle
import android.view.LayoutInflater
import androidx.databinding.Observable
import androidx.fragment.app.Fragment
import com.rm.baselisten.mvvm.BaseVMFragment
import com.rm.module_mine.BR
import com.rm.module_mine.R
import com.rm.module_mine.activity.MineMemberActivity.Companion.MEMBER_ID
import com.rm.module_mine.databinding.MineFragmentMemberFollowBinding
import com.rm.module_mine.viewmodel.MineMemberFollowsViewModel

/**
 * 主播/个人 发布的书籍/创建听单/收藏听单
 */
class MineMemberFollowFragment :
    BaseVMFragment<MineFragmentMemberFollowBinding, MineMemberFollowsViewModel>() {
    companion object {
        fun newInstance(memberId: String): Fragment {
            val bundle = Bundle()
            bundle.putString(MEMBER_ID, memberId)
            val fragment = MineMemberFollowFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun initModelBrId() = BR.viewModel

    override fun initLayoutId() = R.layout.mine_fragment_member_follow

    override fun initData() {
        arguments?.getString(MEMBER_ID)?.let {
            mViewModel.memberId=it
            mViewModel.mineMemberFollowList()
        }
    }

    override fun startObserve() {
    }
}