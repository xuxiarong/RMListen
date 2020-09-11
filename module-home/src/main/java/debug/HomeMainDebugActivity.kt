package debug

import com.rm.baselisten.debug.BaseDebugActivity
import com.rm.module_home.R
import com.rm.module_home.activity.boutique.BoutiqueActivity
import com.rm.module_home.activity.detail.HomeDetailActivity
import com.rm.module_home.activity.TopListActivity
import com.rm.module_home.activity.menu.MenuActivity
import kotlinx.android.synthetic.main.home_activity_main.*

/**
 * desc   :
 * date   : 2020/08/13
 * version: 1.0
 */
class HomeMainDebugActivity : BaseDebugActivity() {
    override fun getLayoutResId(): Int = R.layout.home_activity_main

    override fun initView() {
        btnMenu.setOnClickListener {
            MenuActivity.startActivity(this)
        }

        btnBoutique.setOnClickListener {
            BoutiqueActivity.startActivity(this)
        }
        btnList.setOnClickListener {
            TopListActivity.startActivity(this)
        }
        btnDetail.setOnClickListener {
            HomeDetailActivity.startActivity(this)
        }
    }

    override fun initData() {
    }
}