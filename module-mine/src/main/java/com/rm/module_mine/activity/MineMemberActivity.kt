package com.rm.module_mine.activity

import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Handler
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.tabs.TabLayout
import com.rm.baselisten.mvvm.BaseVMActivity
import com.rm.baselisten.util.DLog
import com.rm.baselisten.utilExt.dip
import com.rm.baselisten.utilExt.getStateHeight
import com.rm.baselisten.utilExt.px2dip
import com.rm.baselisten.utilExt.screenHeight
import com.rm.module_mine.BR
import com.rm.module_mine.R
import com.rm.module_mine.databinding.ActivityMineMemberDetailBinding
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

    override fun initData() {
        setTransparentStatusBar()
        val layoutParams = mDataBind.mineDetailTitleCl.layoutParams
                as ViewGroup.MarginLayoutParams
        layoutParams.apply {
            //动态获取状态栏的高度,并设置标题栏的topMargin
            stateHeight = getStateHeight(this@MineMemberActivity)
            topMargin = stateHeight
        }
        heightPixels = screenHeight //屏幕高度

        //初始化behaviorHeight的高度
        val behaviorHeight = px2dip(heightPixels/2 + stateHeight)

        peekHeight =
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                behaviorHeight,
                resources.displayMetrics).toInt()

        home_detail_back.post{
            val lp = home_detail_back.layoutParams as ConstraintLayout.LayoutParams
            marginTop = home_detail_back.height + lp.topMargin + lp.bottomMargin / 2 + screenHeight
            offsetDistance = lp.topMargin
        }
        mViewModel.getInfoDetail(intent.getStringExtra(MEMBER_ID))
        mHandler = Handler()
        initBehavior()
        initTabFrameData()
    }
    private fun initBehavior(){
        val behavior = BottomSheetBehavior.from(nestedScrollView)
        behavior.isHideable = false
        behavior.state = BottomSheetBehavior.STATE_HIDDEN
        behavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback(){
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                var distance: Float = 0F;
                maskView.alpha = slideOffset
                distance = offsetDistance * slideOffset
                if (distance > 0) {
                    constraint.translationY = -distance
                }
            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                val layoutParams = bottomSheet.layoutParams

                if(bottomSheet.height > heightPixels - stateHeight - dip(48)){
                    layoutParams.height = heightPixels - stateHeight - dip(48)
                    bottomSheet.layoutParams = layoutParams
                }
            }
        })
        mHandler.postDelayed({
            behavior.isHideable = false
            behavior.state = BottomSheetBehavior.STATE_COLLAPSED
            behavior.peekHeight = peekHeight
            ObjectAnimator.ofFloat(nestedScrollView, "alpha", 0f, 1f).setDuration(500).start()
        }, 200)
    }

    //temp test to
    fun initTabFrameData(){
        tab_mine_member_list.tabMode = TabLayout.MODE_SCROLLABLE
        tab_mine_member_list.tabMode = TabLayout.MODE_SCROLLABLE
        tab_mine_member_list.addTab(tab_mine_member_list.newTab().setText("主页"))
        tab_mine_member_list.addTab(tab_mine_member_list.newTab().setText("评论"))
        tab_mine_member_list.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab) {

            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabSelected(tab: TabLayout.Tab) {
                when (tab.position) {
                    0 -> frameLayout.setBackgroundColor(Color.parseColor("#ff0000"))
                    1 -> frameLayout.setBackgroundColor(Color.parseColor("#0000ff"))
                    2 -> frameLayout.setBackgroundColor(Color.parseColor("#00ff00"))
                }
            }
        })
    }
}