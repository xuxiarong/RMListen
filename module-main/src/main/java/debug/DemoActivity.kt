package debug

import android.content.Intent
import com.rm.baselisten.mvvm.BaseActivity
import com.rm.module_main.R
import kotlinx.android.synthetic.main.activity_demo.*

class DemoActivity : BaseActivity() {

    override fun initData() {

    }

    override fun getLayoutId() = R.layout.activity_demo

    override fun initView() {
        super.initView()
        setStatusBar(R.color.base_activity_bg_color)

        demoDataBindBtn.setOnClickListener {
            startActivity(Intent(this@DemoActivity,DemoDataBindVmActivity::class.java))
        }

        demoSingleBtn.setOnClickListener {
            startActivity(Intent(this@DemoActivity,DemoSingleRvActivity::class.java))
        }
        demoSingleClickRvBtn.setOnClickListener {
            startActivity(Intent(this@DemoActivity,DemoSingleClickVmActivity::class.java))
        }
        demoMultiRvBtn.setOnClickListener {
            startActivity(Intent(this@DemoActivity,DemoMultiRvActivity::class.java))
        }
        demoMultiVmRvBtn.setOnClickListener {
            startActivity(Intent(this@DemoActivity,DemoMultiClickVmActivity::class.java))
        }
        demoSwipeBtn.setOnClickListener {
            startActivity(Intent(this@DemoActivity,DemoSwipeActivity::class.java))
        }

    }



}