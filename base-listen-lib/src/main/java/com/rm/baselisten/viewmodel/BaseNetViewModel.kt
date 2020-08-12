package com.rm.baselisten.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.rm.baselisten.model.BaseNetModel
import com.rm.baselisten.mvvm.BaseViewModel
import com.rm.baselisten.net.BaseNetStatus

/**
 * desc   :
 * date   : 2020/08/11
 * version: 1.0
 */
open class BaseNetViewModel(private val baseLayoutId: Int) : BaseViewModel(baseLayoutId) {
    private var _baseNetModel = MutableLiveData<BaseNetModel>()
    val baseNetModel: LiveData<BaseNetModel> get() = _baseNetModel

    init {
        _baseNetModel.value = BaseNetModel().setContentLayout(baseLayoutId)
    }

    fun showContent(){
        _baseNetModel.value = _baseNetModel.value?.setStatus(BaseNetStatus.BASE_SHOW_CONTENT)
    }

    fun showLoad(){
        _baseNetModel.value = _baseNetModel.value?.setStatus(BaseNetStatus.BASE_SHOW_LOADING)
    }

    fun showEmpty(){
        _baseNetModel.value = _baseNetModel.value?.setStatus(BaseNetStatus.BASE_SHOW_DATA_EMPTY)
    }

    fun showError(){
        _baseNetModel.value = _baseNetModel.value?.setStatus(BaseNetStatus.BASE_SHOW_NET_ERROR)
    }

    fun setEmptyView(layoutId:Int){
        _baseNetModel.value = _baseNetModel.value?.setEmptyLayout(layoutId)
    }

    fun setContentView(layoutId:Int){
        _baseNetModel.value = _baseNetModel.value?.setContentLayout(layoutId)

    }

    fun setErrorView(layoutId:Int){
        _baseNetModel.value = _baseNetModel.value?.setErrorLayout(layoutId)
    }

    fun setLoadView(layoutId:Int){
        _baseNetModel.value = _baseNetModel.value?.setLoadLayout(layoutId)
    }
}