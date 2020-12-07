package com.rm.module_mine.fragment

import android.os.Bundle
import android.view.LayoutInflater
import androidx.databinding.Observable
import androidx.fragment.app.Fragment
import com.rm.baselisten.mvvm.BaseVMFragment
import com.rm.baselisten.util.DLog
import com.rm.business_lib.loginUser
import com.rm.module_mine.BR
import com.rm.module_mine.R
import com.rm.module_mine.activity.MineMemberActivity.Companion.MEMBER_ID
import com.rm.module_mine.databinding.MineFragmentMemberFansBinding
import com.rm.module_mine.viewmodel.MineMemberFansViewModel

/**
 * 主播/个人 发布的书籍/创建听单/收藏听单
 */
class MineMemberFansFragment :
    BaseVMFragment<MineFragmentMemberFansBinding, MineMemberFansViewModel>() {
    companion object {
        fun newInstance(memberId: String): Fragment {
            val bundle = Bundle()
            bundle.putString(MEMBER_ID, memberId)
            val fragment = MineMemberFansFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun initModelBrId() = BR.viewModel

    override fun initLayoutId() = R.layout.mine_fragment_member_fans

    override fun initData() {
        arguments?.getString(MEMBER_ID)?.let {
            mViewModel.memberId = it
            mViewModel.mineMemberFansList()
        }
    }

    override fun startObserve() {
    }
}