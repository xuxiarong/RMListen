package com.rm.module_main.activity

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import com.rm.baselisten.mvvm.BaseActivity
import com.rm.business_lib.wedgit.NoTouchViewPager
import com.rm.component_comm.home.HomeService
import com.rm.component_comm.mine.MineService
import com.rm.component_comm.play.PlayService
import com.rm.component_comm.router.RouterHelper
import com.rm.module_main.R
import com.rm.module_main.adapter.MyViewPagerAdapter
import com.rm.module_main.customview.bottomtab.BottomTabView
import com.rm.module_main.customview.bottomtab.NavigationController
import com.rm.module_main.customview.bottomtab.item.NormalItemView

/**
 * desc   :
 * date   : 2020/08/12
 * version: 1.0
 */
class MainMainActivity : BaseActivity() {

    private lateinit var navigationController: NavigationController

    override fun getLayoutId() = R.layout.main_activity_main
    lateinit var homeService: HomeService
    lateinit var mineService: MineService

    override fun initView() {

        navigationController = findViewById<BottomTabView>(R.id.mainTab).custom().run {

            addItem(NormalItemView(this@MainMainActivity).apply {
                initialize(
                    R.drawable.main_ic_home_tab_listen_bar,
                    R.drawable.main_ic_home_tab_listen_bar_selected,
                    getString(R.string.main_tab_listen_bar)
                )
                setTextCheckedColor(
                    ContextCompat.getColor(
                        this@MainMainActivity,
                        R.color.main_color_accent
                    )
                )
                setTextDefaultColor(
                    ContextCompat.getColor(
                        this@MainMainActivity,
                        R.color.main_color_disable
                    )
                )
            })
            addItem(NormalItemView(this@MainMainActivity).apply {
                initialize(
                    R.drawable.main_ic_home_tab_search,
                    R.drawable.main_ic_home_tab_search_selected,
                    getString(R.string.main_tab_search)
                )
                setTextCheckedColor(
                    ContextCompat.getColor(
                        this@MainMainActivity,
                        R.color.main_color_accent
                    )
                )
                setTextDefaultColor(
                    ContextCompat.getColor(
                        this@MainMainActivity,
                        R.color.main_color_disable
                    )
                )
            })
            addItem(NormalItemView(this@MainMainActivity).apply {
                initialize(
                    R.drawable.main_ic_home_tab_my_listen,
                    R.drawable.main_ic_home_tab_my_listen_selected,
                    getString(R.string.main_tab_my_listen)
                )
                setTextCheckedColor(
                    ContextCompat.getColor(
                        this@MainMainActivity,
                        R.color.main_color_accent
                    )
                )
                setTextDefaultColor(
                    ContextCompat.getColor(
                        this@MainMainActivity,
                        R.color.main_color_disable
                    )
                )
            })
            addItem(NormalItemView(this@MainMainActivity).apply {
                initialize(
                    R.drawable.main_ic_home_tab_user,
                    R.drawable.main_ic_home_tab_user_selected,
                    getString(R.string.main_tab_user)
                )
                setTextCheckedColor(
                    ContextCompat.getColor(
                        this@MainMainActivity,
                        R.color.main_color_accent
                    )
                )
                setTextDefaultColor(
                    ContextCompat.getColor(
                        this@MainMainActivity,
                        R.color.main_color_disable
                    )
                )
            })
        }.build()
        navigationController.addPlaceholder(2)
        val viewPager = findViewById<NoTouchViewPager>(R.id.view_pager).apply {
            adapter = MyViewPagerAdapter(
                supportFragmentManager,
                navigationController.itemCount
            )
        }
        viewPager.offscreenPageLimit = 5
        navigationController.setMessageNumber(3, 8)
        navigationController.setHasMessage(1, true)
        navigationController.setupWithViewPager(viewPager)


    }

    override fun onResume() {
        super.onResume()
        val playService = RouterHelper.createRouter(PlayService::class.java)
        rootViewAddView(playService.getGlobalPlay())
        playService.showView(this)
    }

    override fun initData() {

    }

    companion object{
        fun startMainActivity(context: Context){
            context.startActivity(Intent(context,MainMainActivity::class.java))
        }
    }
}