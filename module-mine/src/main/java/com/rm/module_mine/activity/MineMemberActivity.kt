package com.rm.module_mine.activity

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.view.ViewGroup
import androidx.databinding.Observable
import com.rm.baselisten.mvvm.BaseVMActivity
import com.rm.baselisten.utilExt.dip
import com.rm.baselisten.utilExt.getStateHeight
import com.rm.module_mine.BR
import com.rm.module_mine.R
import com.rm.module_mine.adapter.MineMemberPageAdapter
import com.rm.module_mine.databinding.MineActivityMemberDetailBinding
import com.rm.module_mine.fragment.MineMemberCommentFragment
import com.rm.module_mine.fragment.MineMemberMainFragment
import com.rm.module_mine.viewmodel.MineMemberViewModel

/**
 *  主播/用户详情
 */
class MineMemberActivity : BaseVMActivity<MineActivityMemberDetailBinding, MineMemberViewModel>() {

    private var stateHeight: Int = 0 //状态栏高度
    private lateinit var mHandler: Handler

    companion object {
        const val MEMBER_ID = "memberId"
        var isNavToCommentFragment = false
        fun newInstance(context: Context, memberId: String) {
            context.startActivity(
                Intent(
                    context,
                    MineMemberActivity::class.java
                ).putExtra(MEMBER_ID, memberId)
            )
        }

        fun toMineCommentFragment(context: Context, memberId: String) {
            isNavToCommentFragment = true
            context.startActivity(
                Intent(
                    context,
                    MineMemberActivity::class.java
                ).putExtra(MEMBER_ID, memberId)
            )
        }
    }

    override fun initModelBrId() = BR.viewModel

    override fun getLayoutId() = R.layout.mine_activity_member_detail

    override fun startObserve() {
        mViewModel.detailInfoData.addOnPropertyChangedCallback(object :
            Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                createFragment()
            }
        })
    }


    private fun initParams() {
        setTransparentStatusBar()
        val layoutParams = mDataBind.mineMemberDetailTitleLayout.layoutParams
                as ViewGroup.MarginLayoutParams
        layoutParams.apply {
            //动态获取状态栏的高度,并设置标题栏的topMargin
            stateHeight = getStateHeight(this@MineMemberActivity)
            topMargin = stateHeight
        }
    }

    override fun initData() {
        initParams()
        val memberId = intent.getStringExtra(MEMBER_ID)
        memberId?.let {
            mViewModel.memberId.set(it)
            mViewModel.getInfoDetail(it)

            mHandler = Handler()
        }
    }


    private fun createFragment() {
        val bean = mViewModel.detailInfoData.get()!!
        val memberId = mViewModel.memberId.get()!!
        val mainFragment = MineMemberMainFragment.newInstance(memberId)
        val commentFragment = MineMemberCommentFragment.newInstance(memberId, bean.member_type)

        val list = mutableListOf<MineMemberPageAdapter.MemberPageData>()
        list.add(MineMemberPageAdapter.MemberPageData(mainFragment, "主页"))
        list.add(MineMemberPageAdapter.MemberPageData(commentFragment, "评论"))

        mDataBind.mineMemberDetailViewpager.adapter =
            MineMemberPageAdapter(list, supportFragmentManager)
        mDataBind.tabMineMemberList.setupWithViewPager(mDataBind.mineMemberDetailViewpager)
        mDataBind.mineMemberDetailViewpager.setCurrentItem(
            if (isNavToCommentFragment) {
                1
            } else {
                0
            }, false
        )


    }

}