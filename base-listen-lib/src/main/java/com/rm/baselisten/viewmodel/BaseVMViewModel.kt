package com.rm.baselisten.viewmodel

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import com.rm.baselisten.model.*
import com.rm.baselisten.mvvm.BaseViewModel

/**
 * desc   : MVVM模式的基类ViewModel
 * date   : 2020/08/11
 * version: 1.0
 */
open class BaseVMViewModel : BaseViewModel() {

    var baseLayoutModel = MutableLiveData<BaseNetLayoutModel>()

    var baseTitleModel = MutableLiveData<BaseTitleModel>()

    var baseStatusModel = MutableLiveData<BaseStatusModel>()

    var baseIntentModel = MutableLiveData<BaseIntentModel>()


    var baseToastStr = MutableLiveData<String>()
    var baseToastInt = MutableLiveData<Int>()


    fun startActivity(clazz: Class<*>){
        baseIntentModel.postValue(BaseIntentModel(clazz))
    }

    fun startActivity(clazz: Class<*>,bundle: Bundle){
        baseIntentModel.postValue(BaseIntentModel(clazz,bundle))
    }

    fun startActivityForResult(clazz: Class<*>, requestCode : Int){
        baseIntentModel.postValue(BaseIntentModel(clazz, Bundle(), requestCode))
    }

    fun startActivityForResult(clazz: Class<*>, bundle: Bundle,requestCode : Int){
        baseIntentModel.postValue(BaseIntentModel(clazz, bundle, requestCode))
    }

    fun showToast(content : String){
        baseToastStr.postValue(content)
    }

    fun showToast(contentId : Int){
        baseToastInt.postValue(contentId)
    }

    fun showContentView(){
        baseStatusModel.postValue(BaseStatusModel(BaseNetStatus.BASE_SHOW_CONTENT))
    }

    fun showNetError(){
        baseStatusModel.postValue(BaseStatusModel(BaseNetStatus.BASE_SHOW_NET_ERROR))
    }

    fun showDataEmpty(){
        baseStatusModel.postValue(BaseStatusModel(BaseNetStatus.BASE_SHOW_DATA_EMPTY))
    }

    fun showLoading(){
        baseStatusModel.postValue(BaseStatusModel(BaseNetStatus.BASE_SHOW_LOADING))
    }



}