package com.rm.module_mine.activity

import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.rm.baselisten.mvvm.BaseVMActivity
import com.rm.baselisten.util.DLog
import com.rm.baselisten.utilExt.getStateHeight
import com.rm.baselisten.utilExt.px2dip
import com.rm.baselisten.utilExt.screenHeight
import com.rm.module_mine.BR
import com.rm.module_mine.R
import com.rm.module_mine.databinding.ActivityMineMemberDetailBinding
import com.rm.module_mine.fragment.MineMemberCommentFragment
import com.rm.module_mine.fragment.MineMemberMainFragment
import com.rm.module_mine.viewmodel.MineMemberViewModel
import kotlinx.android.synthetic.main.activity_mine_member_booklist.*
import kotlinx.android.synthetic.main.activity_mine_member_detail.*

/**
 *  主播/用户详情
 */
class MineMemberActivity : BaseVMActivity<ActivityMineMemberDetailBinding, MineMemberViewModel>() {

    private var stateHeight: Int = 0 //状态栏高度
    private var heightPixels: Int = 0 // 屏幕高度
    private var marginTop: Int = 0 //钮至屏幕顶部的高度
    private var peekHeight: Int = 0
    private var offsetDistance: Int = 0
    private lateinit var mHandler: Handler

    companion object {
        const val MEMBER_ID = "memberId"
        fun newInstance(context: Context, memberId: String) {
            context.startActivity(
                Intent(
                    context,
                    MineMemberActivity::class.java
                ).putExtra(MEMBER_ID, memberId)
            )
        }
    }

    override fun initModelBrId() = BR.viewModel

    override fun getLayoutId() = R.layout.activity_mine_member_detail

    override fun startObserve() {

    }

    private fun initParams() {
        setTransparentStatusBar()
        val layoutParams = mDataBind.mineDetailTitleCl.layoutParams
                as ViewGroup.MarginLayoutParams
        layoutParams.apply {
            //动态获取状态栏的高度,并设置标题栏的topMargin
            stateHeight = getStateHeight(this@MineMemberActivity)
            topMargin = stateHeight
        }

        heightPixels = screenHeight //屏幕高度

        dash_line_view.post {
            val behaviorHeight = px2dip(heightPixels - dash_line_view.top + stateHeight)
            peekHeight =
                TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    behaviorHeight,
                    resources.displayMetrics
                ).toInt()
        }

        home_detail_back.post {
            val lp = home_detail_back.layoutParams as ConstraintLayout.LayoutParams
            marginTop = home_detail_back.height + lp.topMargin + lp.bottomMargin / 2 + screenHeight
            offsetDistance = lp.topMargin
        }
    }

    override fun initData() {
        initParams()
        val memberId = intent.getStringExtra(MEMBER_ID)
        memberId?.let {
            mViewModel.memberId.set(it)
            mViewModel.getInfoDetail(it)

            mHandler = Handler()
            initBehavior()
            setupViewPager(mine_member_detail_viewpager, memberId)
            tab_mine_member_list!!.setupWithViewPager(mine_member_detail_viewpager)
        }

    }

    private fun initBehavior() {
        val behavior = BottomSheetBehavior.from(nestedScrollView)
        behavior.isHideable = false
        behavior.state = BottomSheetBehavior.STATE_HIDDEN
        behavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                //色差值变化
                var distance: Float = 0F;
                maskView.alpha = slideOffset
                distance = offsetDistance * slideOffset
                if (distance > 0) {
                    constraint.translationY = -distance
                }
            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                val layoutParams = bottomSheet.layoutParams
                DLog.i("bottomSheet", "height:$bottomSheet.height")
                if (bottomSheet.height > heightPixels) {
                    layoutParams.height = heightPixels
                    bottomSheet.layoutParams = layoutParams
                }
                var state = "null"
                when (newState) {
                    1 -> state = "STATE_DRAGGING" //过渡状态此时用户正在向上或者向下拖动bottom sheet
                    2 -> state = "STATE_SETTLING" // 视图从脱离手指自由滑动到最终停下的这一小段时间
                    3 -> mViewModel.isVisible.set(false)//处于完全展开的状态
                    4 -> //默认的折叠状态
                        mViewModel.isVisible.set(true)
                    5 -> state = "STATE_HIDDEN" //下滑动完全隐藏 bottom sheet
                }
                DLog.i("state", "state:$state")
            }
        })
        mHandler.postDelayed({
            behavior.isHideable = false
            behavior.state = BottomSheetBehavior.STATE_COLLAPSED
            behavior.peekHeight = peekHeight
            ObjectAnimator.ofFloat(nestedScrollView, "alpha", 0f, 1f).setDuration(500).start()
        }, 200)
    }

    internal inner class ViewPagerAdapter(manager: FragmentManager) :
        FragmentPagerAdapter(manager) {
        private val mFragmentList = ArrayList<Fragment>()
        private val mFragmentTitleList = ArrayList<String>()

        override fun getItem(position: Int): Fragment {
            return mFragmentList[position]
        }

        override fun getCount(): Int {
            return mFragmentList.size
        }

        fun addFragment(fragment: Fragment, title: String) {
            mFragmentList.add(fragment)
            mFragmentTitleList.add(title)
        }

        override fun getPageTitle(position: Int): CharSequence {
            return mFragmentTitleList[position]
        }
    }

    private fun setupViewPager(viewPager: ViewPager, memberId: String) {
        val adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(MineMemberMainFragment.newInstance(memberId), "主页")
        adapter.addFragment(MineMemberCommentFragment.newInstance(memberId), "评论")
        viewPager.adapter = adapter
    }
}