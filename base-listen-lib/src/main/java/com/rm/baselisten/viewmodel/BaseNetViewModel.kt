package com.rm.baselisten.viewmodel

import androidx.lifecycle.MutableLiveData
import com.rm.baselisten.model.BaseNetLayoutModel
import com.rm.baselisten.model.BaseNetModel
import com.rm.baselisten.model.BaseTitleModel
import com.rm.baselisten.mvvm.BaseViewModel
import com.rm.baselisten.model.BaseNetStatus
import com.rm.baselisten.model.BaseStatusModel

/**
 * desc   :
 * date   : 2020/08/11
 * version: 1.0
 */
open class BaseNetViewModel(private val baseLayoutId: Int) : BaseViewModel(baseLayoutId) {

    var baseNetModel = MutableLiveData<BaseNetModel>()

    var baseTitleModel = MutableLiveData<BaseTitleModel>()

    var baseStatusModel = MutableLiveData<BaseStatusModel>()

     var baseLayoutModel = MutableLiveData<BaseNetLayoutModel>()

    fun showContent() {
        baseStatusModel.value?.netStatus = BaseNetStatus.BASE_SHOW_CONTENT
    }

    fun showLoad() {
        baseStatusModel.value?.netStatus = BaseNetStatus.BASE_SHOW_LOADING
    }

    fun showEmpty() {
        baseStatusModel.value?.netStatus = BaseNetStatus.BASE_SHOW_DATA_EMPTY
    }

    fun showError() {
        baseNetModel.value = baseNetModel.value?.setStatus(BaseNetStatus.BASE_SHOW_NET_ERROR)
    }
}