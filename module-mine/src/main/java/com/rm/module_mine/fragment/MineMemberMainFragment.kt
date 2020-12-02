package com.rm.module_mine.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.rm.baselisten.mvvm.BaseVMFragment
import com.rm.module_mine.BR
import com.rm.module_mine.R
import com.rm.module_mine.activity.MineMemberActivity.Companion.MEMBER_ID
import com.rm.module_mine.databinding.MineFragmentMemberMainBinding
import com.rm.module_mine.viewmodel.MineFragmentMemberMainViewModel

/**
 * 主播/个人 发布的书籍/创建听单/收藏听单
 */
class MineMemberMainFragment :
    BaseVMFragment<MineFragmentMemberMainBinding, MineFragmentMemberMainViewModel>() {
    companion object {
        fun newInstance(memberId: String): Fragment {
            val bundle = Bundle()
            bundle.putString(MEMBER_ID, memberId)
            val fragment = MineMemberMainFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun initModelBrId() = BR.viewModel

    override fun startObserve() {

    }


    override fun initLayoutId() = R.layout.mine_fragment_member_main

    override fun initData() {
        arguments?.getString(MEMBER_ID)?.let {
            mViewModel.getMemberProfile(it)
        }
    }
}