package com.rm.baselisten.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.rm.baselisten.model.BaseNetLayoutModel
import com.rm.baselisten.model.BaseNetModel
import com.rm.baselisten.model.BaseTitleModel
import com.rm.baselisten.mvvm.BaseViewModel
import com.rm.baselisten.net.BaseNetStatus
import com.rm.baselisten.net.bean.BaseStatusModel

/**
 * desc   :
 * date   : 2020/08/11
 * version: 1.0
 */
open class BaseNetViewModel(private val baseLayoutId: Int) : BaseViewModel(baseLayoutId) {

    var baseNetModel = MutableLiveData<BaseNetModel>()

    var baseTitleModel = MutableLiveData<BaseTitleModel>()

    var baseStatusModel = MutableLiveData<BaseStatusModel>()

    private var _baseLayoutModel = MutableLiveData<BaseNetLayoutModel>()
    val baseLayoutModel: LiveData<BaseNetLayoutModel> get() = _baseLayoutModel

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

    fun setEmptyView(layoutId: Int) {
        baseNetModel.value = baseNetModel.value?.setEmptyLayout(layoutId)
    }

    fun setContentView(layoutId: Int) {
        baseNetModel.value = baseNetModel.value?.setContentLayout(layoutId)

    }

    fun setErrorView(layoutId: Int) {
        baseNetModel.value = baseNetModel.value?.setErrorLayout(layoutId)
    }

    fun setLoadView(layoutId: Int) {
        baseNetModel.value = baseNetModel.value?.setLoadLayout(layoutId)
    }

    fun setTitle(title: String) {
        var baseTitleModel1 = BaseTitleModel()
        baseTitleModel1.mainTitle = title
        baseTitleModel.postValue(baseTitleModel1)
    }


}