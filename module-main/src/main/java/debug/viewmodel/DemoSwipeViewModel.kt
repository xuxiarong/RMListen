package debug.viewmodel

import androidx.lifecycle.MutableLiveData
import com.rm.baselisten.adapter.single.CommonBindVMAdapter
import com.rm.baselisten.adapter.swipe.CommonSwipeVmAdapter
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
    var openItem = MutableLiveData<DemoSwipeModel>()

    var closeSwipe : ()-> Unit = {}

    val mSwipeAdapter : CommonBindVMAdapter<DemoSwipeModel> by lazy {
        CommonSwipeVmAdapter(this, mutableListOf<DemoSwipeModel>(),
            R.layout.demo_swipe_rv_item,
            R.id.demoRvSwipe,
            BR.viewModel,
            BR.item)
    }

    fun testSwipeData() {

        val tempData = arrayListOf<DemoSwipeModel>()
        for (i in 0..15) {
            tempData.add(DemoSwipeModel(content = "我是Rv第${i}条", top = "置顶", read = "已读", delete = "删除",needClose = false))
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
        var tempValue = swipeData.value

        if(tempValue!=null && tempValue.size>0){
            val iterator = tempValue.iterator()
            while(iterator.hasNext()){
                if(iterator.next() == item){
                    iterator.remove()
                }
            }
            openItem.value = null
            tempValue.add(0,item)
        }
        swipeData.value = tempValue
        showToast("${item.content}被置顶")
    }

    fun onRead(item: DemoSwipeModel) {
        var tempValue = swipeData.value

        if(tempValue!=null && tempValue.size>0){
            val iterator = tempValue.iterator()
            while(iterator.hasNext()){
                if(iterator.next() == item){
                    iterator.next().needClose = true
                }
            }
        }
//        mSwipeAdapter.clo
        openItem.value = null
        swipeData.value = tempValue
        showToast("${item.content}被阅读")
    }

    fun onDelete(item: DemoSwipeModel) {
        showToast("${item.content}被删除")
        var tempValue = swipeData.value

        if(tempValue!=null && tempValue.size>0){
            val iterator = tempValue.iterator()
            while(iterator.hasNext()){
                if(iterator.next() == item){
                    iterator.remove()
                }
            }
        }
        openItem.value = null
        swipeData.value = tempValue
    }

    fun bindSwipeItem(swipeModel:DemoSwipeModel?){
        openItem.value = swipeModel
    }

}