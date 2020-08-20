package debug

import com.rm.baselisten.activity.BaseActivity
import com.rm.module_home.R
import com.rm.module_home.activity.menu.MenuActivity
import kotlinx.android.synthetic.main.home_activity_main.*

/**
 * desc   :
 * date   : 2020/08/13
 * version: 1.0
 */
class HomeMainActivity : BaseActivity() {
    override fun getLayoutResId(): Int = R.layout.home_activity_main

    override fun initView() {
        btnMenu.setOnClickListener {
            MenuActivity.startActivity(this)
        }
    }

    override fun initData() {
    }
}