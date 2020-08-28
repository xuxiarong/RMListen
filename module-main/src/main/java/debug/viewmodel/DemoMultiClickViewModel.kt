package debug.viewmodel

import androidx.lifecycle.MutableLiveData
import com.rm.baselisten.adapter.multi.BaseMultiAdapter
import com.rm.baselisten.util.DLog
import com.rm.baselisten.viewmodel.BaseVMViewModel
import debug.model.DemoMultiModel1
import debug.model.DemoMultiModel2
import debug.repository.DemoMultiRepository

/**
 * desc   :
 * date   : 2020/08/27
 * version: 1.0
 */
class DemoMultiClickViewModel(val repository: DemoMultiRepository) : BaseVMViewModel() {

    var multiList = MutableLiveData<ArrayList<BaseMultiAdapter.IBindItemType>>()

    var readHeader = MutableLiveData<List<DemoMultiModel2>>()
    var readData = MutableLiveData<List<DemoMultiModel1>>()
    var findHeader = MutableLiveData<List<DemoMultiModel2>>()
    var findData = MutableLiveData<List<DemoMultiModel1>>()


    var demoItem1Click: (DemoMultiModel1) -> Unit = {}
    var demoItem2Click: (DemoMultiModel2) -> Unit = {}
    //因为这里不需要Activity响应点击事件，所以声明的方法的引用变量不需要用掉，可以删除
    var demoItem1AgeClick: (DemoMultiModel1) -> Unit = ::demoAgeClickFun


    fun getMultiData() {
        readHeader.value = listOf(
            DemoMultiModel2("精品推荐", "阅读")
        )

        readData.value = listOf(
            DemoMultiModel1("张三", 18),
            DemoMultiModel1("李四", 19),
            DemoMultiModel1("王五", 20)
        )

        findHeader.value = listOf(
            DemoMultiModel2("发现更多", "更多")
        )

        findData.value = listOf(
            DemoMultiModel1("java", 18),
            DemoMultiModel1("kotlin", 19),
            DemoMultiModel1("flutter", 20)
        )

        val allData = ArrayList<BaseMultiAdapter.IBindItemType>()
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

    fun demoAgeClickFun(item1: DemoMultiModel1) {
        DLog.d("suolong", "demoAgeClickFun = ${item1.name} --- age = ${item1.age}")
    }

}