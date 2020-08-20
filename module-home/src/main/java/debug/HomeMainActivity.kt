package debug

import androidx.viewpager.widget.ViewPager
import com.rm.baselisten.activity.BaseActivity
import com.rm.business_lib.wedgit.bendtablayout.BendTabLayout
import com.rm.module_home.R
import com.rm.module_main.adapter.HomeTestViewPagerAdapter

/**
 * desc   :
 * date   : 2020/08/13
 * version: 1.0
 */
class HomeMainActivity : BaseActivity() {
    override fun getLayoutResId(): Int = R.layout.home_activity_main

    override fun initView() {
        var viewPager= findViewById<ViewPager>(R.id.view_pager)
        var bendLayout= findViewById<BendTabLayout>(R.id.bend_tab)
        viewPager.adapter=HomeTestViewPagerAdapter(supportFragmentManager,8)
        bendLayout.setupWithViewPager(viewPager)
    }

    override fun initData() {

    }
}