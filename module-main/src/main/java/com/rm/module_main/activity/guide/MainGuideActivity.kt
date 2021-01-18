package com.rm.module_main.activity.guide

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.rm.baselisten.mvvm.BaseActivity
import com.rm.module_main.R
import com.rm.module_main.activity.MainMainActivity
import kotlinx.android.synthetic.main.activity_main_guide.*

class MainGuideActivity : BaseActivity() {

    private lateinit var mViewPagerAdapter: MainGuidePageAdapter
    private val guideFragmentList = mutableListOf<Fragment>(
            GuideFirstFragment.newInstance(),
            GuideSecondFragment.newInstance(),
            GuideThreeFragment.newInstance()
    )

    override fun initView() {
        super.initView()

        mViewPagerAdapter = MainGuidePageAdapter(
                fm = this.supportFragmentManager,
                fragmentList = guideFragmentList
        )
        main_guide_pager.offscreenPageLimit = 3
        main_guide_pager.adapter = mViewPagerAdapter
        main_guide_pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                when (position) {
                    0 -> {
                        main_guide_dot1.setImageResource(R.drawable.main_guide_select_dot)
                        main_guide_dot2.setImageResource(R.drawable.main_guide_unselect_dot)
                        main_guide_dot3.setImageResource(R.drawable.main_guide_unselect_dot)
                    }
                    1 -> {
                        main_guide_dot1.setImageResource(R.drawable.main_guide_unselect_dot)
                        main_guide_dot2.setImageResource(R.drawable.main_guide_select_dot)
                        main_guide_dot3.setImageResource(R.drawable.main_guide_unselect_dot)
                    }
                    2 -> {
                        main_guide_dot1.setImageResource(R.drawable.main_guide_unselect_dot)
                        main_guide_dot2.setImageResource(R.drawable.main_guide_unselect_dot)
                        main_guide_dot3.setImageResource(R.drawable.main_guide_select_dot)
                    }
                }
            }
        })


    }

    override fun initData() {
        main_guide_to_main.setOnClickListener {
            MainMainActivity.startMainActivity(this)
            finish()
        }
    }

    override fun getLayoutId() = R.layout.activity_main_guide

    companion object {
        fun startGuideActivity(context: Context) {
            context.startActivity(Intent(context, MainGuideActivity::class.java))
        }
    }
}