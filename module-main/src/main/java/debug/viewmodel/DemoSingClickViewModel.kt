package debug.viewmodel

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.rm.baselisten.BaseApplication
import com.rm.baselisten.util.ToastUtil
import com.rm.baselisten.viewmodel.BaseVMViewModel

/**
 * desc   :
 * date   : 2020/08/27
 * version: 1.0
 */
class DemoSingClickViewModel : BaseVMViewModel() {

    val userName = ObservableField<String>("")

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
            ToastUtil.show(BaseApplication.CONTEXT, "")
        } else {
            ToastUtil.show(BaseApplication.CONTEXT, "$userName")
        }
    }

    private fun afterInputChange() {

    }

    fun checkUserName(content: String) {

    }

}