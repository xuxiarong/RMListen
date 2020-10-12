package debug

import android.app.Activity
import android.os.Bundle
import com.rm.baselisten.view.DragCloseLayout
import com.rm.module_main.R
import kotlinx.android.synthetic.main.activity_demo_swipe_play.*

class DemoSwipePlayActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_demo_swipe_play)

        demoDrag.seDragCloseListener(object : DragCloseLayout.IDragCloseListener{
            override fun onDragClose() {
                finish()
            }
        })

    }
    override fun onPause() {
        overridePendingTransition(0, 0)
        super.onPause()
    }

}