package debug.viewmodel

import androidx.lifecycle.MutableLiveData
import com.rm.baselisten.viewmodel.BaseVMViewModel

/**
 * desc   :
 * date   : 2020/08/27
 * version: 1.0
 */
class DemoSwipeViewModel : BaseVMViewModel() {

    var demoData = MutableLiveData<List<String>>()



}