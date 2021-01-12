package debug.viewmodel

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.rm.baselisten.viewmodel.BaseVMViewModel

/**
 * desc   :
 * date   : 2020/08/27
 * version: 1.0
 */
class DemoViewModel : BaseVMViewModel() {

    val userName = ObservableField<String>("")

    var canClick = ObservableField<String>("不能登陆")

    var demoData = MutableLiveData<List<String>>()

    val verifyInput: (String) -> Unit = { userNameChange() }

    fun getDemoData() {
        demoData.value = listOf(
            "kotlin",
            "flutter",
            "java"
        )
    }

    private fun userNameChange() {
        if (userName.get().isNullOrEmpty()) {
            canClick.set("不能登陆")
        } else {
            canClick.set("点击登陆")
        }
    }

}