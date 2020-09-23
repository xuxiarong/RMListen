package debug

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.rm.baselisten.adapter.multi.CommonMultiAdapter
import com.rm.baselisten.binding.bindVerticalLayout
import com.rm.module_main.BR
import com.rm.module_main.R
import debug.model.DemoTopRvModel
import debug.model.DemoTopView
import kotlinx.android.synthetic.main.activity_demo_top_rv.*

class DemoTopRvActivity : AppCompatActivity() {

    var topViewPosition = 0

    private val topAdapter: CommonMultiAdapter<MultiItemEntity> by lazy {
        CommonMultiAdapter(
            getData(),
            BR.item
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_demo_top_rv)
        topRv.bindVerticalLayout(topAdapter)
        topAdapter.notifyDataSetChanged()

        topRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                try {
                    val linearLayoutManager: LinearLayoutManager = topRv.layoutManager as LinearLayoutManager
                    val first = linearLayoutManager.findFirstVisibleItemPosition()
                    if (topAdapter.data[first].itemType == R.layout.demo_item_top_view) {
                        topViewPosition = first
                    }
                    if (first > topViewPosition) {
                        topView.visibility = View.VISIBLE
                    } else {
                        topView.visibility = View.GONE
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        })

    }

    fun getData(): MutableList<MultiItemEntity> {
        val result = ArrayList<MultiItemEntity>()

        for (i in 0..30) {
            if (i == 5) {
                result.add(DemoTopView("评论 ----- 个人"))
            } else {
                result.add(DemoTopRvModel("我是第 $i 条数据"))
            }

        }
        return result

    }

}