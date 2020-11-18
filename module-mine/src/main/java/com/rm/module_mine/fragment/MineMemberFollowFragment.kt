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

    private val footView by lazy {
        LayoutInflater.from(context).inflate(R.layout.business_foot_view, null)
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
        mViewModel.refreshStatusModel.isHasMore.addOnPropertyChangedCallback(object :
            Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                val hasMore = mViewModel.refreshStatusModel.isHasMore.get()
                if (hasMore == true) {
                    mViewModel.followAdapter.removeAllFooterView()
                    mViewModel.followAdapter.addFooterView(footView)
                } else {
                    mViewModel.followAdapter.removeAllFooterView()
                }
            }
        })
    }
}