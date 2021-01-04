package com.rm.baselisten.viewmodel

import android.content.Context
import android.content.ContextWrapper
import androidx.databinding.ObservableField
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.rm.baselisten.BaseApplication
import com.rm.baselisten.BaseApplication.Companion.CONTEXT
import com.rm.baselisten.R
import com.rm.baselisten.model.*
import com.rm.baselisten.mvvm.BaseViewModel
import com.rm.baselisten.util.NetWorkUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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
     * 基类的页面销毁返回数据
     */
    var baseResultModel = MutableLiveData<BaseResultModel>()

    /**
     * 基类的销毁界面数据
     */
    var baseFinishModel = MutableLiveData<BaseFinishModel>(BaseFinishModel(false))

    /**
     * 基类的Toast数据
     */
    var baseToastModel = MutableLiveData<BaseToastModel>()

    var baseCancelToastModel = MutableLiveData<Boolean>()

    /**
     * 基类的提示数据
     */
    val baseTipModel = MutableLiveData<BaseTipModel>()

    /**
     * 是否显示音乐按钮入口
     */
    var basePlayControlModel = ObservableField<BasePlayControlModel>(BasePlayControlModel())


    fun launchOnUI(block: suspend CoroutineScope.() -> Unit) {
        if (NetWorkUtils.isNetworkAvailable(BaseApplication.CONTEXT)) {
            viewModelScope.launch(Dispatchers.Main, CoroutineStart.DEFAULT) { block() }
        } else {
//            showNetError()
            showTip(CONTEXT.getString(R.string.base_empty_tips_netword), R.color.base_ff5e5e)

        }
    }

    fun <T> launchOnIO(block: suspend CoroutineScope.() -> T) {
        if (NetWorkUtils.isNetworkAvailable(BaseApplication.CONTEXT)) {
            viewModelScope.launch(Dispatchers.IO, CoroutineStart.DEFAULT) { block() }
        } else {
//            showNetError()
            showTip(CONTEXT.getString(R.string.base_empty_tips_netword), R.color.base_ff5e5e)
        }
    }


    fun <T> launchOnIO(block: suspend CoroutineScope.() -> T, netErrorBlock: () -> Unit = {}) {
        if (NetWorkUtils.isNetworkAvailable(BaseApplication.CONTEXT)) {
            viewModelScope.launch(Dispatchers.IO, CoroutineStart.DEFAULT) { block() }
        } else {
            netErrorBlock()
        }
    }

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

    fun finish() {
        baseFinishModel.postValue(BaseFinishModel(finish = true))
    }

    fun setResultAndFinish(resultCode: Int) {
        baseFinishModel.postValue(BaseFinishModel(finish = true, resultCode = resultCode))
    }

    fun setResultAndFinish(resultCode: Int, dataMap: HashMap<String, Any>) {
        baseFinishModel.postValue(
            BaseFinishModel(
                finish = true,
                dataMap = dataMap,
                resultCode = resultCode
            )
        )
    }

    fun setResult(resultCode: Int, dataMap: HashMap<String, Any>) {
        baseResultModel.postValue(
            BaseResultModel(
                dataMap = dataMap,
                resultCode = resultCode
            )
        )
    }

    fun showToast(content: String) {
        baseToastModel.postValue(BaseToastModel(content = content))
    }

    fun showErrorToast(content: String) {
        baseToastModel.postValue(BaseToastModel(content = content, colorId = R.color.base_ff5e5e))
    }

    fun showToast(content: String, colorId: Int) {
        baseToastModel.postValue(BaseToastModel(content = content, colorId = colorId))
    }

    fun showToast(contentId: Int) {
        baseToastModel.postValue(BaseToastModel(contentId = contentId))
    }

    fun showErrorToast(contentId: Int) {
        baseToastModel.postValue(
            BaseToastModel(
                contentId = contentId,
                colorId = R.color.base_ff5e5e
            )
        )
    }

    fun cancelToast() {
        baseCancelToastModel.postValue(true)
    }

    fun showToast(contentId: Int, colorId: Int) {
        baseToastModel.postValue(BaseToastModel(contentId = contentId, colorId = colorId))
    }

    fun showToast(toastModel: BaseToastModel) {
        baseToastModel.postValue(toastModel)
    }


    fun showContentView() {
        baseStatusModel.postValue(BaseStatusModel(BaseNetStatus.BASE_SHOW_CONTENT))
    }

    fun showServiceError() {
        baseStatusModel.postValue(BaseStatusModel(BaseNetStatus.BASE_SHOW_SERVICE_ERROR))
    }

    fun showNetError() {
        baseStatusModel.postValue(BaseStatusModel(BaseNetStatus.BASE_SHOW_NET_ERROR))
    }

    fun showDataEmpty() {
        baseStatusModel.postValue(BaseStatusModel(BaseNetStatus.BASE_SHOW_DATA_EMPTY))
    }

    fun showSearchDataEmpty() {
        baseStatusModel.postValue(BaseStatusModel(BaseNetStatus.BASE_SHOW_SEARCH_DATA_EMPTY))
    }

    fun showLoading() {
        baseStatusModel.postValue(BaseStatusModel(BaseNetStatus.BASE_SHOW_LOADING))
    }


    fun showTip(content: String) {
        showToast(content)
//        baseTipModel.postValue(BaseTipModel(content = content, contentColor = R.color.base_333))
    }

    fun showTip(content: String, color: Int) {
        showToast(content,color)
//        baseTipModel.postValue(BaseTipModel(content = content, contentColor = color))
    }

    /**
     * 根据context 获取activity对象
     */
    fun getActivity(context: Context): FragmentActivity? {
        if (context is FragmentActivity) {
            return context
        }
        return if (context is ContextWrapper) {
            val contextWrapper = context.baseContext
            getActivity(contextWrapper)
        } else {
            null
        }
    }

    fun getHasMap(): HashMap<String, Any> {
        return hashMapOf()
    }

}