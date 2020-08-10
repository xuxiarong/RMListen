package com.rm.baselisten

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.rm.baselisten.mvvm.BaseViewModel
import com.rm.baselisten.net.api.BaseRepository

/**
 * desc   :
 * date   : 2020/08/04
 * version: 1.0
 */
class BaseListenViewModel(
    private val repository: BaseRepository
) : BaseViewModel() {
    private val _uiState = MutableLiveData<UiState<*>>()
    val uiState: LiveData<UiState<*>>
        get() = _uiState
}