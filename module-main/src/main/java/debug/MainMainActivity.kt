package debug

import com.rm.baselisten.activity.BaseActivity
import com.rm.module_main.R
import com.rm.module_main.adapter.MyViewPagerAdapter
import com.rm.module_main.customview.bottomtab.NavigationController
import com.rm.module_main.customview.NoTouchViewPager
import com.rm.module_main.customview.bottomtab.BottomTabView
import com.rm.module_main.customview.bottomtab.item.NormalItemView

/**
 * desc   :
 * date   : 2020/08/13
 * version: 1.0
 */
class MainMainActivity : BaseActivity() {
    lateinit var navigationController: NavigationController
    override fun getLayoutResId(): Int = R.layout.main_activity_main

    override fun initView() {
        navigationController= findViewById<BottomTabView>(R.id.tab).custom().run {

            addItem(NormalItemView(this@MainMainActivity).apply {
                initialize(R.drawable.ic_favorite_gray_24dp, R.drawable.ic_favorite_teal_24dp)
            })
            addItem(NormalItemView(this@MainMainActivity).apply {
                initialize(R.drawable.ic_favorite_gray_24dp, R.drawable.ic_favorite_teal_24dp)
            })
            addItem(NormalItemView(this@MainMainActivity).apply {
                initialize(R.drawable.ic_favorite_gray_24dp, R.drawable.ic_favorite_teal_24dp)
            })
            addItem(NormalItemView(this@MainMainActivity).apply {
                initialize(R.drawable.ic_favorite_gray_24dp, R.drawable.ic_favorite_teal_24dp)
            })
        }.build()
        navigationController.addPlaceholder(2)
        var viewPager = findViewById<NoTouchViewPager>(R.id.view_pager).apply {
            adapter = MyViewPagerAdapter(
                supportFragmentManager,
                navigationController.itemCount
            )
        }

        navigationController.setupWithViewPager(viewPager)
    }

    override fun initData() {

    }
}