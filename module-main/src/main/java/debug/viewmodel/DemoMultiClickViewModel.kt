package debug.viewmodel

import androidx.lifecycle.MutableLiveData
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.rm.baselisten.viewmodel.BaseVMViewModel
import debug.model.DemoMultiModel1
import debug.model.DemoMultiModel2
import debug.repository.DemoMultiRepository

/**
 * desc   :
 * date   : 2020/08/27
 * version: 1.0
 */
class DemoMultiClickViewModel(private val repository: DemoMultiRepository) : BaseVMViewModel() {

    var multiList = MutableLiveData<MutableList<MultiItemEntity>>()

    var readHeader = MutableLiveData<MutableList<DemoMultiModel2>>()
    var readData = MutableLiveData<MutableList<DemoMultiModel1>>()
    var findHeader = MutableLiveData<MutableList<DemoMultiModel2>>()
    var findData = MutableLiveData<MutableList<DemoMultiModel1>>()


    var demoItem1Click: (DemoMultiModel1) -> Unit = {}
    var demoItem2Click: (DemoMultiModel2) -> Unit = {}

    fun getMultiData() {
        readHeader.value = mutableListOf(
            DemoMultiModel2("精品推荐", "阅读")
        )

        readData.value = mutableListOf(
            DemoMultiModel1("张三", 18),
            DemoMultiModel1("李四", 19),
            DemoMultiModel1("王五", 20)
        )

        findHeader.value = mutableListOf(
            DemoMultiModel2("发现更多", "更多")
        )

        findData.value = mutableListOf(
            DemoMultiModel1("java", 18),
            DemoMultiModel1("kotlin", 19),
            DemoMultiModel1("flutter", 20)
        )

        val allData = mutableListOf<MultiItemEntity>()
        allData.addAll(readHeader.value!!)
        allData.addAll(readData.value!!)
        allData.addAll(findHeader.value!!)
        allData.addAll(findData.value!!)


        multiList.value = allData
    }


    fun getServiceData(){
        multiList.value = repository.getMultiDataFromService()
    }


    fun demoItem1ClickFun(item1: DemoMultiModel1) {
        demoItem1Click(item1)
    }

    fun demoItem2ClickFun(item2: DemoMultiModel2) {
        demoItem2Click(item2)
    }
}