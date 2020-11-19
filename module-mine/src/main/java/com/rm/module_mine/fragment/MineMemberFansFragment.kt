package com.rm.module_mine.fragment

import android.os.Bundle
import android.view.LayoutInflater
import androidx.databinding.Observable
import androidx.fragment.app.Fragment
import com.rm.baselisten.mvvm.BaseVMFragment
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

    private val footView by lazy {
        LayoutInflater.from(context).inflate(R.layout.business_foot_view, null)
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
        mViewModel.refreshStatusModel.noMoreData.addOnPropertyChangedCallback(object :
            Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                val hasMore = mViewModel.refreshStatusModel.noMoreData.get()
                if (hasMore == true) {
                    mViewModel.fanAdapter.removeAllFooterView()
                    mViewModel.fanAdapter.addFooterView(footView)
                } else {
                    mViewModel.fanAdapter.removeAllFooterView()
                }
            }
        })
    }
}