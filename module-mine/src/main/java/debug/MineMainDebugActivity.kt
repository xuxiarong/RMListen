package debug

import androidx.fragment.app.Fragment
import com.rm.baselisten.debug.BaseDebugActivity
import com.rm.module_mine.R
import kotlinx.android.synthetic.main.mine_activity_main.*
import java.util.*

/**
 * desc   :
 * date   : 2020/08/13
 * version: 1.0
 */
class MineMainDebugActivity : BaseDebugActivity() {
    override fun getLayoutResId(): Int = R.layout.mine_activity_main

    override fun initView() {
        createFragment()
    }

    override fun initData() {
    }

    private fun createFragment() {
        val mainFragment = TestFragment()
        val commentFragment = TestFragment()
        val titleList: MutableList<String> =
            ArrayList()
        titleList.add("推荐")
        titleList.add("抖音")
        val fragmentList = mutableListOf<Fragment>(mainFragment, commentFragment)
        page_collapsing.adapter = TabFragmentAdapter(supportFragmentManager, fragmentList,titleList)
        tablayout.setupWithViewPager(page_collapsing)

    }
}