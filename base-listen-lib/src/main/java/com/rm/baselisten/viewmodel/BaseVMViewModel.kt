package com.rm.baselisten.viewmodel

import androidx.lifecycle.MutableLiveData
import com.rm.baselisten.model.*
import com.rm.baselisten.mvvm.BaseViewModel

/**
 * desc   : MVVM模式的基类ViewModel
 * date   : 2020/08/11
 * version: 1.0
 */
open class BaseVMViewModel : BaseViewModel() {
    /**
     * 基类的布局数据
     */
    var baseLayoutModel = MutableLiveData<BaseNetLayoutModel>()

    /**
     * 基类的TitleBar数据
     */
    var baseTitleModel = MutableLiveData<BaseTitleModel>()

    /**
     * 基类的网络状态数据
     */
    var baseStatusModel = MutableLiveData<BaseStatusModel>()

    /**
     * 基类的页面跳转数据
     */
    var baseIntentModel = MutableLiveData<BaseIntentModel>()

    /**
     * 基类的销毁界面数据
     */
    var baseFinishModel = MutableLiveData<BaseFinishModel>(BaseFinishModel(false))

    /**
     * 基类的Toast数据
     */
    var baseToastModel = MutableLiveData<BaseToastModel>()


    fun startActivity(clazz: Class<*>) {
        baseIntentModel.postValue(BaseIntentModel(clazz = clazz))
    }

    fun startActivity(clazz: Class<*>, dataMap: HashMap<String, Any>) {
        baseIntentModel.postValue(BaseIntentModel(clazz = clazz, dataMap = dataMap))
    }

    fun startActivityForResult(clazz: Class<*>, requestCode: Int) {
        baseIntentModel.postValue(BaseIntentModel(clazz = clazz, requestCode = requestCode))
    }

    fun startActivityForResult(clazz: Class<*>, dataMap: HashMap<String, Any>, requestCode: Int) {
        baseIntentModel.postValue(
            BaseIntentModel(
                clazz = clazz,
                dataMap = dataMap,
                requestCode = requestCode
            )
        )
    }

    fun finish(){
        baseFinishModel.postValue(BaseFinishModel(finish = true))
    }

    fun setResultAndFinish(resultCode : Int){
        baseFinishModel.postValue(BaseFinishModel(finish = true,resultCode = resultCode))
    }

    fun setResultAndFinish(resultCode : Int,dataMap: HashMap<String, Any>){
        baseFinishModel.postValue(BaseFinishModel(finish = true,dataMap = dataMap,resultCode = resultCode))
    }

    fun showToast(content: String) {
        baseToastModel.postValue(BaseToastModel(content = content))
    }

    fun showToast(contentId: Int) {
        baseToastModel.postValue(BaseToastModel(contentId = contentId))
    }

    fun showContentView() {
        baseStatusModel.postValue(BaseStatusModel(BaseNetStatus.BASE_SHOW_CONTENT))
    }

    fun showNetError() {
        baseStatusModel.postValue(BaseStatusModel(BaseNetStatus.BASE_SHOW_NET_ERROR))
    }

    fun showDataEmpty() {
        baseStatusModel.postValue(BaseStatusModel(BaseNetStatus.BASE_SHOW_DATA_EMPTY))
    }

    fun showLoading() {
        baseStatusModel.postValue(BaseStatusModel(BaseNetStatus.BASE_SHOW_LOADING))
    }


}