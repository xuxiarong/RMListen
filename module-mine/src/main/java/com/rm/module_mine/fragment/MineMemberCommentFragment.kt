package com.rm.module_mine.fragment

import android.os.Bundle
import android.view.LayoutInflater
import androidx.databinding.Observable
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
        const val MEMBER_TYPE = "memberType"

        fun newInstance(memberId: String, memberType: Int): Fragment {
            val bundle = Bundle()
            bundle.putString(MineMemberActivity.MEMBER_ID, memberId)
            bundle.putInt(MEMBER_TYPE, memberType)
            val fragment = MineMemberCommentFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun initModelBrId() = BR.viewModel

    override fun initLayoutId() = R.layout.mine_fragment_member_comment

    private val footView by lazy {
        LayoutInflater.from(context).inflate(R.layout.business_foot_view, null)
    }

    override fun initView() {
        super.initView()
        arguments?.getString(MineMemberActivity.MEMBER_ID)?.let { memberId ->
            mViewModel.memberId = memberId
        }
        arguments?.getInt(MEMBER_TYPE)?.let { memberType ->
            mViewModel.memberType = memberType
        }
    }

    override fun initData() {
        mViewModel.getData()
    }

    override fun startObserve() {
        mViewModel.refreshStateModel.isHasMore.addOnPropertyChangedCallback(object :
            Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                val hasMore = mViewModel.refreshStateModel.isHasMore.get()
                if (hasMore == true) {
                    mViewModel.commentAdapter.removeAllFooterView()
                    mViewModel.commentAdapter.addFooterView(footView)
                } else {
                    mViewModel.commentAdapter.removeAllFooterView()
                }
            }
        })
    }
}