package debug

import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.rm.baselisten.mvvm.BaseFragment
import com.rm.baselisten.util.DLog
import com.rm.module_mine.R
import kotlinx.android.synthetic.main.mine_fragment_test.*

/**
 *
 * @author yuanfang
 * @date 10/23/20
 * @description
 *
 */
class TestFragment : BaseFragment() {
    override fun initLayoutId() = R.layout.mine_fragment_test

    override fun initData() {
        DLog.i("---->", "dwwwwwwwww")
        rv.layoutManager = LinearLayoutManager(context)
        val myadapter = Myadapter()
        rv.adapter = myadapter
        myadapter.setList(getList())
    }

    private fun getList(): MutableList<String> {
        val mutableListOf = mutableListOf<String>()
        for (i in 0..100) {
            mutableListOf.add("item:$i")
        }
        return mutableListOf
    }

    class Myadapter : BaseQuickAdapter<String, BaseViewHolder>(R.layout.mine_adapter_test) {
        override fun convert(holder: BaseViewHolder, item: String) {
            holder.setText(R.id.text, item)
        }
    }
}