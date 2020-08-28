package debug.viewmodel

import androidx.lifecycle.MutableLiveData
import com.rm.baselisten.util.DLog
import com.rm.baselisten.viewmodel.BaseVMViewModel
import debug.model.SingleVmClickModel
import java.util.*

/**
 * desc   :
 * date   : 2020/08/27
 * version: 1.0
 */
class DemoSingClickViewModel : BaseVMViewModel() {

    var singClickModelList = MutableLiveData<List<SingleVmClickModel>>()

    var demoItemClick: (SingleVmClickModel) -> Unit = {}
    var demoNameClick: (SingleVmClickModel) -> Unit = {}
    var demoAgeClick: (SingleVmClickModel) -> Unit = ::demoAgeClickFun


    fun getSingClickModel() {
        val singData = listOf(
            SingleVmClickModel("张三", 18),
            SingleVmClickModel("李四", 19),
            SingleVmClickModel("王五", 20)
        )
        singClickModelList.value = singData
    }

    fun changeData(){

        val int1 = Random().nextInt(100)
        val int2 = Random().nextInt(100)
        val int3 = Random().nextInt(100)
        val singData = listOf(
            SingleVmClickModel("张三$int1", int1),
            SingleVmClickModel("李四$int2", int2),
            SingleVmClickModel("王五$int3", int3)
        )
        singClickModelList.value = singData
    }

    fun demoItemClickFun(model: SingleVmClickModel) {
        demoItemClick(model)
    }

    fun demoNameClickFun(model: SingleVmClickModel) {
        demoNameClick(model)
    }

    fun demoAgeClickFun(model: SingleVmClickModel) {
        DLog.d("suolong", "demoAgeClickFun = ${model.name} --- age = ${model.age}")
    }

}