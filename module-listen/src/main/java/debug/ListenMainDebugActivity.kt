package debug

import android.content.Intent
import com.rm.baselisten.debug.BaseDebugActivity
import com.rm.module_listen.R
import com.rm.module_listen.player.TestPlayerActivity

/**
 * desc   :
 * date   : 2020/08/13
 * version: 1.0
 */
class ListenMainDebugActivity : BaseDebugActivity() {
    override fun getLayoutResId(): Int = R.layout.listen_activity_main

    override fun initView() {
    }

    override fun initData() {
        startActivity(Intent(this,TestPlayerActivity::class.java))
    }
}