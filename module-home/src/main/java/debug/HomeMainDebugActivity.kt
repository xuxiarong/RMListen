package debug

import android.content.Intent
import com.rm.baselisten.debug.BaseDebugActivity
import com.rm.module_home.R
import com.rm.module_home.activity.HomeTopListActivity
import com.rm.module_home.activity.boutique.BoutiqueActivity
import com.rm.module_home.activity.detail.HomeDetailActivity
import com.rm.module_home.activity.detail.HomeDetailActivity.Companion.startActivity
import com.rm.module_home.activity.detail.HomeDetailActivity1
import com.rm.module_home.activity.menu.HomeMenuActivity
import com.rm.module_home.activity.topic.HomeTopicListActivity
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
            startActivity(Intent(this, HomeDetailActivity1::class.java))
        }
    }

    override fun initData() {
    }
}