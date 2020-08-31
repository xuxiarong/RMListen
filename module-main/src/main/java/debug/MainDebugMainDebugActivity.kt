package debug

import androidx.core.content.ContextCompat
import com.rm.baselisten.debug.BaseDebugActivity
import com.rm.business_lib.wedgit.NoTouchViewPager
import com.rm.module_main.R
import com.rm.module_main.adapter.MyViewPagerAdapter
import com.rm.module_main.customview.bottomtab.NavigationController
import com.rm.module_main.customview.bottomtab.BottomTabView
import com.rm.module_main.customview.bottomtab.item.NormalItemView

/**
 * desc   :
 * date   : 2020/08/13
 * version: 1.0
 */
class MainDebugMainDebugActivity : BaseDebugActivity() {
    lateinit var navigationController: NavigationController
    override fun getLayoutResId(): Int = R.layout.main_debug_activity_main


    override fun initView() {
        navigationController = findViewById<BottomTabView>(R.id.mainTab).custom().run {

            addItem(NormalItemView(this@MainDebugMainDebugActivity).apply {
                initialize(
                    R.drawable.main_ic_home_tab_listen_bar,
                    R.drawable.main_ic_home_tab_listen_bar_selected,
                    getString(R.string.main_tab_listen_bar)
                )
                setTextCheckedColor(ContextCompat.getColor(this@MainDebugMainDebugActivity, R.color.main_color_accent))
                setTextDefaultColor(ContextCompat.getColor(this@MainDebugMainDebugActivity, R.color.main_color_disable))
            })
            addItem(NormalItemView(this@MainDebugMainDebugActivity).apply {
                initialize(
                    R.drawable.main_ic_home_tab_search,
                    R.drawable.main_ic_home_tab_search_selected,
                    getString(R.string.main_tab_search)
                )
                setTextCheckedColor(ContextCompat.getColor(this@MainDebugMainDebugActivity, R.color.main_color_accent))
                setTextDefaultColor(ContextCompat.getColor(this@MainDebugMainDebugActivity, R.color.main_color_disable))
            })
            addItem(NormalItemView(this@MainDebugMainDebugActivity).apply {
                initialize(
                    R.drawable.main_ic_home_tab_my_listen,
                    R.drawable.main_ic_home_tab_my_listen_selected,
                    getString(R.string.main_tab_my_listen)
                )
                setTextCheckedColor(ContextCompat.getColor(this@MainDebugMainDebugActivity, R.color.main_color_accent))
                setTextDefaultColor(ContextCompat.getColor(this@MainDebugMainDebugActivity, R.color.main_color_disable))
            })
            addItem(NormalItemView(this@MainDebugMainDebugActivity).apply {
                initialize(
                    R.drawable.main_ic_home_tab_user,
                    R.drawable.main_ic_home_tab_user_selected,
                    getString(R.string.main_tab_user)
                )
                setTextCheckedColor(ContextCompat.getColor(this@MainDebugMainDebugActivity, R.color.main_color_accent))
                setTextDefaultColor(ContextCompat.getColor(this@MainDebugMainDebugActivity, R.color.main_color_disable))
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