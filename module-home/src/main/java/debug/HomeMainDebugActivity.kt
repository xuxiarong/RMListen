package debug

import com.rm.baselisten.debug.BaseDebugActivity
import com.rm.module_home.R
import com.rm.module_home.activity.HomeTopListActivity
import com.rm.module_home.activity.boutique.BoutiqueActivity
import com.rm.module_home.activity.detail.HomeDetailActivity
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
        btnMenu.setOnClickListener {
            HomeMenuActivity.startActivity(this)
        }

        btnBoutique.setOnClickListener {
            BoutiqueActivity.startActivity(this)
        }
        btnList.setOnClickListener {
            HomeTopListActivity.startActivity(this)
        }
        btnDetail.setOnClickListener {
            HomeDetailActivity.startActivity(this,"1")
        }
        btnTopicList.setOnClickListener {
            HomeTopicListActivity.startActivity(this,1,1,1,"精品推荐")
        }
    }

    override fun initData() {
    }
}