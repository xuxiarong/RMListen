package debug.viewmodel

import androidx.databinding.ObservableField
import com.rm.baselisten.BaseApplication
import com.rm.baselisten.util.ToastUtil
import com.rm.baselisten.viewmodel.BaseVMViewModel

/**
 * desc   :
 * date   : 2020/08/27
 * version: 1.0
 */
class DemoViewModel : BaseVMViewModel() {

    val userName = ObservableField<String>("")


    val verifyInput: (String) -> Unit = { userNameChange() }

    private fun userNameChange() {
        if(userName.get().isNullOrEmpty()){
            ToastUtil.show(BaseApplication.CONTEXT,"")
        }else{
            ToastUtil.show(BaseApplication.CONTEXT,"$userName")

        }
    }

}