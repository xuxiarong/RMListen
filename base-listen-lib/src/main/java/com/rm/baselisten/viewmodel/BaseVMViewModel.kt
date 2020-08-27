package com.rm.baselisten.viewmodel

import androidx.lifecycle.MutableLiveData
import com.rm.baselisten.model.BaseNetLayoutModel
import com.rm.baselisten.model.BaseStatusModel
import com.rm.baselisten.model.BaseTitleModel
import com.rm.baselisten.mvvm.BaseViewModel

/**
 * desc   :
 * date   : 2020/08/11
 * version: 1.0
 */
open class BaseVMViewModel : BaseViewModel() {

    var baseLayoutModel = MutableLiveData<BaseNetLayoutModel>()

    var baseTitleModel = MutableLiveData<BaseTitleModel>()

    var baseStatusModel = MutableLiveData<BaseStatusModel>()

}