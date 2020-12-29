package com.rm.module_mine.activity

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Handler
import android.renderscript.RSRuntimeException
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import androidx.databinding.Observable
import com.google.android.material.appbar.AppBarLayout
import com.rm.baselisten.mvvm.BaseVMActivity
import com.rm.baselisten.thridlib.glide.FastBlur
import com.rm.baselisten.thridlib.glide.RSBlur
import com.rm.baselisten.util.DLog
import com.rm.baselisten.utilExt.DisplayUtils.getStateHeight
import com.rm.baselisten.utilExt.dip
import com.rm.module_mine.BR
import com.rm.module_mine.R
import com.rm.module_mine.adapter.MineMemberPageAdapter
import com.rm.module_mine.databinding.MineActivityMemberDetail1BindingImpl
import com.rm.module_mine.fragment.MineMemberCommentFragment
import com.rm.module_mine.fragment.MineMemberMainFragment
import com.rm.module_mine.viewmodel.MineMemberViewModel
import kotlinx.android.synthetic.main.mine_activity_member_detail1.*
import kotlin.math.abs


/**
 *  主播/用户详情
 */
class MineMemberActivity :
    BaseVMActivity<MineActivityMemberDetail1BindingImpl, MineMemberViewModel>() {

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

    override fun getLayoutId() = R.layout.mine_activity_member_detail1

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
        val layoutParams =
            mine_member_detail_title_back.layoutParams as ViewGroup.MarginLayoutParams
        val stateHeight = getStateHeight(this@MineMemberActivity)
        layoutParams.apply {
            //动态获取状态栏的高度,并设置标题栏的topMargin
            topMargin = stateHeight
        }

        val minHeight = stateHeight + dip(94)
        mine_member_detail_collapsing_layout.minimumHeight = minHeight
        mine_member_detail_appbar_layout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { _, verticalOffset ->
            val height = mine_member_detail_head_layout.measuredHeight - minHeight
            val alpha = abs(verticalOffset).toFloat() / height
            mine_member_detail_info_layout.alpha = 1 - alpha

            if (alpha == 1f) {
                mine_member_detail_title_back.setImageResource(R.drawable.base_icon_write_back)
                mine_member_detail_title.visibility = View.VISIBLE
                mine_member_detail_title_follow.visibility = View.VISIBLE
            } else {
                mine_member_detail_title.visibility = View.GONE
                mine_member_detail_title_back.setImageResource(R.drawable.base_icon_back)
                mine_member_detail_title_follow.visibility = View.GONE
            }

            var radius = abs(verticalOffset) * 25 / (height)
            if (radius <= 0) {
                radius = 1
            }
            if (radius > 25) {
                radius = 25
            }
            blurImage(radius)

            DLog.i("====>", "$alpha     ${abs(verticalOffset)}       ${height / 2}  ")
            if (abs(verticalOffset) > height / 2) {
                mine_member_detail_blur.alpha = abs(verticalOffset).toFloat() / height / 2
            }
        })
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

    private fun blurImage(mRadius: Int) {
        var bitmap = BitmapFactory.decodeResource(resources, R.mipmap.img_my_bac)
        bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            try {
                RSBlur.blur(this, bitmap, mRadius)
            } catch (e: RSRuntimeException) {
                FastBlur.blur(bitmap, mRadius, true)
            }
        } else {
            FastBlur.blur(bitmap, mRadius, true)
        }
        img_mine_background.setImageBitmap(bitmap)
    }

    private fun createFragment() {
        val bean = mViewModel.detailInfoData.get()!!
        val memberId = mViewModel.memberId.get()!!
        val mainFragment = MineMemberMainFragment.newInstance(memberId)
        val commentFragment = MineMemberCommentFragment.newInstance(memberId, bean.member_type)
        val list = mutableListOf<MineMemberPageAdapter.MemberPageData>()
        list.add(MineMemberPageAdapter.MemberPageData(mainFragment, "主页"))
        list.add(MineMemberPageAdapter.MemberPageData(commentFragment, "评论"))
        mine_member_detail_viewpager.adapter =
            MineMemberPageAdapter(list, supportFragmentManager)
        mine_member_detail_tab.setupWithViewPager(mine_member_detail_viewpager)
        mine_member_detail_viewpager.setCurrentItem(
            if (isNavToCommentFragment) {
                1
            } else {
                0
            }, false
        )
    }
}