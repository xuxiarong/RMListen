package com.rm.baselisten.mvvm

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * desc   :
 * date   : 2020/08/05
 * version: 1.0
 */
open class BaseViewModel : ViewModel() {


    open class UiState<T>(
        val isLoading: Boolean = false,
        val isRefresh: Boolean = false,
        val isSuccess: T? = null,
        val isError: String? = null
    )

    val mException: MutableLiveData<Throwable> = MutableLiveData()


    fun launchOnUI(block: suspend CoroutineScope.() -> Unit) {
        viewModelScope.launch(Dispatchers.Main, CoroutineStart.DEFAULT) { block() }
    }

    fun <T> launchOnIO(block: suspend CoroutineScope.() -> T) {
        viewModelScope.launch(Dispatchers.IO, CoroutineStart.DEFAULT) { block() }
    }
}