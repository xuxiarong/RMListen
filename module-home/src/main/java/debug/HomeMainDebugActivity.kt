package debug

import android.content.Intent
import com.rm.baselisten.debug.BaseDebugActivity
import com.rm.module_home.R
import com.rm.module_home.activity.detail.HomeDetailActivity
import kotlinx.android.synthetic.main.home_activity_main.*

/**
 * desc   :
 * date   : 2020/08/13
 * version: 1.0
 */
class HomeMainDebugActivity : BaseDebugActivity() {
    override fun getLayoutResId(): Int = R.layout.home_activity_main

    override fun initView() {
        btnDetail.setOnClickListener {
            startActivity(Intent(this, HomeDetailActivity::class.java))
        }
    }

    override fun initData() {
    }
}