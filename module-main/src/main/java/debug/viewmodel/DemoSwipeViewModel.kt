package debug.viewmodel

import androidx.lifecycle.MutableLiveData
import com.rm.business_lib.swipe.CommonSwipeVmAdapter
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.module_main.BR
import com.rm.module_main.R
import debug.model.DemoSwipeModel

/**
 * desc   :
 * date   : 2020/08/27
 * version: 1.0
 */
class DemoSwipeViewModel : BaseVMViewModel() {

    var swipeData = MutableLiveData<ArrayList<DemoSwipeModel>>()

    val mSwipeAdapter : CommonSwipeVmAdapter<DemoSwipeModel> by lazy {
        CommonSwipeVmAdapter(this, mutableListOf<DemoSwipeModel>(),
            R.layout.demo_swipe_rv_item,
            R.id.demoRvSwipe,
            R.id.demoRvSwipe,
            BR.viewModel,
            BR.item)
    }

    fun testSwipeData() {
        val tempData = arrayListOf<DemoSwipeModel>()
        for (i in 0..15) {
            tempData.add(DemoSwipeModel(content = "我是Rv第{$i}条", top = "置顶", read = "已读", delete = "删除", demoPosition = i))
        }
        swipeData.value = tempData
    }

    fun onClick(item: DemoSwipeModel) {
        showToast("${item.content}被点击")
    }

    fun onLongClick(item: DemoSwipeModel) {
        showToast("${item.content}被长按")
    }

    fun onTop(item: DemoSwipeModel) {

        mSwipeAdapter.mItemManger.closeAllItems()
        showToast("${item.content} ---{itemPosition = {$item.position}}被置顶")
    }

    fun onRead(item: DemoSwipeModel) {
        mSwipeAdapter.mItemManger.closeAllItems()
        showToast("${item.content}被阅读")
    }

    fun onDelete(item: DemoSwipeModel) {
        showToast("${item.content}被删除")
        mSwipeAdapter.mItemManger.closeAllItems()
        mSwipeAdapter.data.remove(item)
        mSwipeAdapter.notifyDataSetChanged()
    }



}