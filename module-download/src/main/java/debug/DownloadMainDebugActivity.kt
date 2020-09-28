package debug

import androidx.appcompat.widget.AppCompatButton
import com.rm.baselisten.debug.BaseDebugActivity
import com.rm.module_download.R
import com.rm.module_download.activity.DownloadChapterSelectionActivity
import com.rm.module_download.activity.DownloadMainActivity

/**
 * desc   :
 * date   : 2020/09/03
 * version: 1.0
 */
class DownloadMainDebugActivity : BaseDebugActivity() {
    override fun getLayoutResId(): Int = R.layout.download_debug_activity_main

    override fun initView() {
        findViewById<AppCompatButton>(R.id.btn_start_main).setOnClickListener {
//            DownloadMainActivity.startActivity(this@DownloadMainDebugActivity)
            DownloadChapterSelectionActivity.startActivity(this@DownloadMainDebugActivity,"162163095869968384")
        }
    }

    override fun initData() {
    }
}