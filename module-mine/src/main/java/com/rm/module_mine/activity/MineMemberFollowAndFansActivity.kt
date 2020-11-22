package com.rm.module_mine.activity

import android.content.Context
import android.content.Intent
import android.text.SpannableString
import androidx.fragment.app.Fragment
import com.rm.baselisten.mvvm.BaseVMActivity
import com.rm.baselisten.util.spannable.SpannableHelper
import com.rm.business_lib.wedgit.bendtablayout.BendTabLayoutMediator
import com.rm.module_mine.BR
import com.rm.module_mine.R
import com.rm.module_mine.adapter.MineFollowAndFansAdapter
import com.rm.module_mine.adapter.MineMemberPageAdapter
import com.rm.module_mine.databinding.MineActivityMemberFollowAndFansBinding
import com.rm.module_mine.fragment.MineMemberFollowFragment
import com.rm.module_mine.fragment.MineMemberFansFragment
import com.rm.module_mine.viewmodel.MineMemberFansViewModel

/**
 *
 * @author yuanfang
 * @date 10/21/20
 * @description 关注/粉丝列表页
 *
 */
class MineMemberFollowAndFansActivity :
    BaseVMActivity<MineActivityMemberFollowAndFansBinding, MineMemberFansViewModel>() {
    companion object {
        private const val FANS_NUM = "fansNum"
        private const val FOLLOW_NUM = "followNum"
        private const val MEMBER_ID = "memberId"
        private const val TYPE = "type"
        const val TYPE_FOLLOW = 1
        const val TYPE_FANS = 2

        fun newInstance(
            context: Context,
            fansNum: Int,
            followNum: Int,
            memberId: String,
            type: Int
        ) {
            val intent = Intent(context, MineMemberFollowAndFansActivity::class.java)
            intent.putExtra(FANS_NUM, fansNum)
            intent.putExtra(FOLLOW_NUM, followNum)
            intent.putExtra(MEMBER_ID, memberId)
            intent.putExtra(TYPE, type)
            context.startActivity(intent)
        }
    }

    override fun initModelBrId() = BR.viewModel

    override fun getLayoutId() = R.layout.mine_activity_member_follow_and_fans

    override fun initData() {
        createFragment()
    }

    override fun startObserve() {
    }

    /**
     * 创建fragment
     */
    private fun createFragment() {
        val type = intent.getIntExtra(TYPE, TYPE_FOLLOW)
        val memberId = intent.getStringExtra(MEMBER_ID)
        memberId?.let {
            val fansFragment = MineMemberFansFragment.newInstance(it)
            val followFragment = MineMemberFollowFragment.newInstance(it)
            val fragments = mutableListOf<Fragment>()
            fragments.add(followFragment)
            fragments.add(fansFragment)
            mDataBind.mineMemberFansViewpager.adapter = MineFollowAndFansAdapter(this, fragments)
            attachViewPager()
            if (type == TYPE_FANS) {
                mDataBind.mineMemberFansViewpager.setCurrentItem(1, false)
            }
        }
    }

    /**
     * tabLayout绑定viewpager滑动事件
     */
    private fun attachViewPager() {
        val fansNum = intent.getIntExtra(FANS_NUM, 0)
        val followNum = intent.getIntExtra(FOLLOW_NUM, 0)
        val titles = mutableListOf<CharSequence>()
        titles.add("关注($followNum)")
        titles.add("粉丝($fansNum)")

        BendTabLayoutMediator(
            mDataBind.mineMemberFansTab, mDataBind.mineMemberFansViewpager
        ) { tab, position ->
            tab.text = titles[position]
        }.attach()
    }
}